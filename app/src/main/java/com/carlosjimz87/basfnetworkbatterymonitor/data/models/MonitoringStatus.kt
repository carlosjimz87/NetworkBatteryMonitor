package com.carlosjimz87.basfnetworkbatterymonitor.data.models

data class MonitoringStatus(
    val network: NetworkStatus,
    val battery: BatteryStatus
)