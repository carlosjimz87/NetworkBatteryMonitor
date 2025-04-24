package com.carlosjimz87.basfnetworkbatterymonitor.domain.repository

import com.carlosjimz87.basfnetworkbatterymonitor.data.battery.BatteryMonitor
import com.carlosjimz87.basfnetworkbatterymonitor.data.connectivity.NetworkMonitor
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.MonitoringStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class StatusRepositoryImpl(
    private val networkMonitor: NetworkMonitor,
    private val batteryMonitor: BatteryMonitor
) : StatusRepository {

    override fun getStatus(): Flow<MonitoringStatus> = combine(
        networkMonitor.networkStatusFlow,
        batteryMonitor.batteryStatusFlow
    ) { network, battery ->
        MonitoringStatus(network = network, battery = battery)
    }
}