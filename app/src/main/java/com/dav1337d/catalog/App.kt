package com.dav1337d.catalog

import android.app.Application
import android.content.Context
import com.dav1337d.catalog.di.applicationModule
import org.koin.android.ext.android.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(applicationModule))
        appContext = applicationContext
    }

    companion object {
        var appContext: Context? = null
            private set
    }
}