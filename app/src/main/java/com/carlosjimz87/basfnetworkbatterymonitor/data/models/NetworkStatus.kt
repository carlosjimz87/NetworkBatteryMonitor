package com.carlosjimz87.basfnetworkbatterymonitor.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NetworkStatus(
    val type: NetworkType = NetworkType.OFFLINE,
    val connected: Boolean = false,
    val hasInternet: Boolean = false
) : Parcelable

enum class NetworkType {
    WIFI,
    CELLULAR,
    VPN,
    ETHERNET,
    OFFLINE
}