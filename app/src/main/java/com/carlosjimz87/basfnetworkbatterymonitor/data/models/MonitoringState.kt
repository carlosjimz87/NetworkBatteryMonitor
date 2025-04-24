package com.carlosjimz87.basfnetworkbatterymonitor.data.models

data class MonitoringState(
    val network: NetworkStatus,
    val battery: BatteryStatus
)