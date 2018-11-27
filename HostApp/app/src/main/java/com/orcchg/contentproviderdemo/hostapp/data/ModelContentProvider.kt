package com.orcchg.contentproviderdemo.hostapp.data

import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import com.orcchg.contentproviderdemo.hostapp.HostApp
import com.orcchg.contentproviderdemo.hostapp.domain.Model
import com.orcchg.contentproviderdemo.hostapp.domain.readModels
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class ModelContentProvider : ContentProvider() {

    companion object {
        const val CODE_MODELS_DIR = 1
        const val CODE_MODELS_ITEM = 2
        const val AUTHORITY = "com.orcchg.contentproviderdemo.hostapp.models_provider"
    }

    private val matcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        matcher.addURI(AUTHORITY, Model.TABLE_NAME, CODE_MODELS_DIR)
        matcher.addURI(AUTHORITY, "${Model.TABLE_NAME}/*", CODE_MODELS_ITEM)
    }

    private fun dao(context: Context) = ModelDatabase.getInstance(context).modelDao()

    @Suppress("CheckResult")
    override fun onCreate(): Boolean {
        /**
         * It is supposed that data for this [ContentProvider] has already been inserted into some
         * storage, like database. Otherwise, it does not make any sense for consumer app to access
         * the data that does not even exist. But for demo purposes querying the [ContentProvider]
         * should retrieve some valid data, though [HostApp] may not be launched before to generate
         * and store that data internally. So, instead of just returning empty [Cursor], we force
         * insertion of data.
         */
        context?.let {
            if (dao(it).totalModels() <= 0) {
                Timber.i("Need to fill data before accessing ContentProvider")
                GlobalScope.launch {
                    Timber.i("Coroutine [fill on access] started")
                    readModels(it.assets).also { dao(context!!).insert(it) }
                    Timber.i("Coroutine [fill on access] finished")
                }
            }
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = query(uri)

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        queryArgs: Bundle?,
        signal: CancellationSignal?
    ): Cursor? = query(uri)

    private fun query(uri: Uri): Cursor? =
        context?.let { context ->
            val cursor = when (matcher.match(uri)) {
                CODE_MODELS_DIR -> dao(context).modelsCursor()
                CODE_MODELS_ITEM -> uri.lastPathSegment?.let { dao(context).modelCursor(it) }
                else -> throw IllegalArgumentException("Unknown URI: $uri")
            }
            cursor?.setNotificationUri(context.contentResolver, uri)
            cursor
        }

    override fun insert(uri: Uri, values: ContentValues?): Uri? =
        context?.let { context ->
            if (values == null) throw IllegalArgumentException("No input values")

            when (matcher.match(uri)) {
                CODE_MODELS_DIR -> {
                    dao(context).insert(Model.from(values)!!)
                                .let { ContentUris.withAppendedId(uri, it) }
                                .also { context.contentResolver.notifyChange(uri, null) }
                }
                CODE_MODELS_ITEM -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
                else -> throw IllegalArgumentException("Unknown URI: $uri")
            }
        }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int =
        context?.let { context ->
            when (matcher.match(uri)) {
                CODE_MODELS_DIR -> throw IllegalArgumentException("Invalid URI, cannot update without ID: $uri")
                CODE_MODELS_ITEM ->
                    Model.from(values)
                            ?.let { dao(context).update(it) }
                             .also { context.contentResolver.notifyChange(uri, null) }
                else -> throw IllegalArgumentException("Unknown URI: $uri")
            }
        } ?: 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int =
        context?.let { context ->
            when (matcher.match(uri)) {
                CODE_MODELS_DIR -> throw IllegalArgumentException("Invalid URI, cannot update without ID: $uri")
                CODE_MODELS_ITEM ->
                    uri.lastPathSegment
                          ?.let { dao(context).delete(it) }
                           .also { context.contentResolver.notifyChange(uri, null) }
                else -> throw IllegalArgumentException("Unknown URI: $uri")
            }
        } ?: 0

    override fun getType(uri: Uri): String? =
            when (matcher.match(uri)) {
                CODE_MODELS_DIR -> "vnd.android.cursor.dir/$AUTHORITY.${Model.TABLE_NAME}"
                CODE_MODELS_ITEM -> "vnd.android.cursor.item/$AUTHORITY.${Model.TABLE_NAME}"
                else -> throw IllegalArgumentException("Unknown URI: $uri")
            }
}
