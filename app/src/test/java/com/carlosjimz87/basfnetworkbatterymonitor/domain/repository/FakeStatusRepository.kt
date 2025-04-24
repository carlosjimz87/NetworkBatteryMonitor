package com.carlosjimz87.basfnetworkbatterymonitor.domain.repository

import com.carlosjimz87.basfnetworkbatterymonitor.data.models.MonitoringState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class FakeStatusRepositoryImpl(
    private val networkMonitor: INetworkMonitor,
    private val batteryMonitor: IBatteryMonitor
) : StatusRepository {
    override fun getStatus(): Flow<MonitoringState> =
        combine(networkMonitor.networkStatusFlow, batteryMonitor.batteryStatusFlow) { net, bat ->
            MonitoringState(net, bat)
        }
}

// TODO: In a production environment these fakes would be injected with a Koin testModule