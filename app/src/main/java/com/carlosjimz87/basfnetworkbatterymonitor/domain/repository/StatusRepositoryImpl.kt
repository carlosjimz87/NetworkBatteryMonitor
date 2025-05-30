package com.carlosjimz87.basfnetworkbatterymonitor.domain.repository

import com.carlosjimz87.basfnetworkbatterymonitor.data.battery.BatteryMonitor
import com.carlosjimz87.basfnetworkbatterymonitor.data.connectivity.NetworkMonitor
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.BatteryStatus
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.MonitoringState
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart

class StatusRepositoryImpl(
    private val networkMonitor: NetworkMonitor,
    private val batteryMonitor: BatteryMonitor
) : StatusRepository {

    override fun getStatus(): Flow<MonitoringState> = combine(
        // Emit a default NetworkStatus immediately before first real emission
        networkMonitor.networkStatusFlow
            .onStart { emit(NetworkStatus()) },

        // Emit a default BatteryStatus immediately before first real emission
        batteryMonitor.batteryStatusFlow
            .onStart { emit(BatteryStatus()) }

    ) { network, battery ->
        // Combine both flows into a unified MonitoringState
        MonitoringState(network = network, battery = battery)
    }
}
