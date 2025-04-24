package com.carlosjimz87.basfnetworkbatterymonitor.data.models

data class NetworkStatus(
    val type: String = "Unknown",
    val connected: Boolean = false,
    val hasInternet: Boolean = false
)