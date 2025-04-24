package com.carlosjimz87.basfnetworkbatterymonitor.di
import android.content.Context
import android.net.ConnectivityManager
import com.carlosjimz87.basfnetworkbatterymonitor.data.connectivity.NetworkMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModules = module {

    // System services
    single { androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    // Monitors
    single { NetworkMonitor(get()) }

    single{ java.lang.String("Hola") }

}