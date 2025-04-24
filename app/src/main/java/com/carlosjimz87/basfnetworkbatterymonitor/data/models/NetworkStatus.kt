package com.carlosjimz87.basfnetworkbatterymonitor.data.models

data class NetworkStatus(
    val type: NetworkType = NetworkType.OFFLINE,
    val connected: Boolean = false,
    val hasInternet: Boolean = false
)

enum class NetworkType {
    WIFI,
    CELLULAR,
    VPN,
    ETHERNET,
    OFFLINE
}