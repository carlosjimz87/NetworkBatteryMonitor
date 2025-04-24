package com.carlosjimz87.basfnetworkbatterymonitor.data.repository

import com.carlosjimz87.basfnetworkbatterymonitor.data.models.BatteryStatus
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.MonitoringState
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkStatus
import com.carlosjimz87.basfnetworkbatterymonitor.domain.repository.StatusRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class FakeStatusRepository(
    private val networkFlow: Flow<NetworkStatus>,
    private val batteryFlow: Flow<BatteryStatus>
) : StatusRepository {
    override fun getStatus(): Flow<MonitoringState> = combine(
        networkFlow,
        batteryFlow
    ) { network, battery ->
        MonitoringState(network = network, battery = battery)
    }
}

// TODO: In a production environment these fakes would be injected with a Koin testModule