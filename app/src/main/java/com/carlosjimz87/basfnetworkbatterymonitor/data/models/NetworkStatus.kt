package com.carlosjimz87.basfnetworkbatterymonitor.data.models

data class NetworkStatus(
    val type: String,
    val connected: Boolean,
    val hasInternet: Boolean
)