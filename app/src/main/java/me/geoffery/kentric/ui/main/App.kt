package me.geoffery.kentric.ui.main

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin

class App: Application() {

        override fun onCreate() {
            super.onCreate()
            startKoin {
                androidLogger()
                androidContext(this@App)
                workManagerFactory()
                modules(listOf(modules))
            }
        }
}