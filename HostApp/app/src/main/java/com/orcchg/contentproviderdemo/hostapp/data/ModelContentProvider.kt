package com.orcchg.contentproviderdemo.hostapp.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.orcchg.contentproviderdemo.hostapp.HostApp
import com.orcchg.contentproviderdemo.hostapp.domain.readModels
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class ModelContentProvider : ContentProvider() {

    private val dao by lazy { ModelDatabase.getInstance(context!!).modelDao() }

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
        if (dao.totalModels() <= 0) {
            Timber.i("Need to fill data before accessing ContentProvider")
            GlobalScope.launch {
                Timber.i("Coroutine [fill on access] started")
                readModels(context!!.assets).also { dao.insertModels(it) }
                Timber.i("Coroutine [fill on access] finished")
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
    ): Cursor? {
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
    }

    override fun getType(uri: Uri): String? {
    }
}
