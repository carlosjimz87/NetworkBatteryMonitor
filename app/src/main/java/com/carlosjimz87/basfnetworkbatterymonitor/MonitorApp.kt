package com.carlosjimz87.basfnetworkbatterymonitor

import android.app.Application
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext
import com.carlosjimz87.basfnetworkbatterymonitor.di.appModules

class MonitorApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MonitorApp)
            modules(appModules)
        }
    }
}
