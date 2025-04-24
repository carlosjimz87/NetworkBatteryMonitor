package com.carlosjimz87.basfnetworkbatterymonitor.domain.repository

import com.carlosjimz87.basfnetworkbatterymonitor.data.models.BatteryStatus
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface INetworkMonitor {
    val networkStatusFlow: Flow<NetworkStatus>
}

interface IBatteryMonitor {
    val batteryStatusFlow: Flow<BatteryStatus>
}

class FakeNetworkMonitor(flow: MutableStateFlow<NetworkStatus>) :
    INetworkMonitor {
    override val networkStatusFlow = flow
}

class FakeBatteryMonitor(flow: MutableStateFlow<BatteryStatus>) :
    IBatteryMonitor {
    override val batteryStatusFlow = flow
}

// TODO: In a production environment these fakes would be injected with a Koin testModule