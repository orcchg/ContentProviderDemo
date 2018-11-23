package com.orcchg.contentproviderdemo.hostapp.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.orcchg.contentproviderdemo.hostapp.HostApp
import com.orcchg.contentproviderdemo.hostapp.R
import com.orcchg.contentproviderdemo.hostapp.domain.readModels
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val modelAdapter = ModelAdapter()

    private val app by lazy { application as HostApp }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_items.adapter = modelAdapter

        GlobalScope.launch {
            Timber.i("Coroutine started")
            // first operation - read models from assets and insert them into database
            readModels(assets).also { app.db.modelDao().insertModels(it) }

            // second operation - get models from the database and show them in UI list
            app.db.modelDao().models()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ modelAdapter.submitList(it) }, Timber::e)
            Timber.i("Coroutine finished")
        }
    }
}
