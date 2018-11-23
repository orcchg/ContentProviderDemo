package com.orcchg.contentproviderdemo.hostapp

import android.app.Application
import com.orcchg.contentproviderdemo.hostapp.data.ModelDatabase
import timber.log.Timber

class HostApp : Application() {

    val db: ModelDatabase by lazy { ModelDatabase.getInstance(applicationContext) }

    override fun onCreate() {
        super.onCreate()
        if (!BuildConfig.DEBUG) return

        Timber.plant(Timber.DebugTree())
    }
}
