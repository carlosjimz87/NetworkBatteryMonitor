package com.carlosjimz87.basfnetworkbatterymonitor.di
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.SavedStateHandle
import com.carlosjimz87.basfnetworkbatterymonitor.data.battery.BatteryMonitor
import com.carlosjimz87.basfnetworkbatterymonitor.data.connectivity.NetworkMonitor
import com.carlosjimz87.basfnetworkbatterymonitor.domain.repository.StatusRepository
import com.carlosjimz87.basfnetworkbatterymonitor.domain.repository.StatusRepositoryImpl
import com.carlosjimz87.basfnetworkbatterymonitor.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    // System services
    single { androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    // Monitors
    single { NetworkMonitor(get()) }
    single { BatteryMonitor(androidContext()) }

    // Repository
    single<StatusRepository> { StatusRepositoryImpl(get(), get()) }

    // ViewModels
    viewModel { (handle: SavedStateHandle) ->
        MainViewModel(repository = get(), savedStateHandle = handle)
    }

}