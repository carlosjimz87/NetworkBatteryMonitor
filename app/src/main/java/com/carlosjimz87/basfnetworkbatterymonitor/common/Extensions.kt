package com.carlosjimz87.basfnetworkbatterymonitor.common

import com.carlosjimz87.basfnetworkbatterymonitor.data.models.MonitoringState
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun MonitoringState.internetAvailability(): String {

    return if (this.network.hasInternet) {
        when (this.network.type) {
            NetworkType.OFFLINE -> ""
            else -> "(Online)"
        }
    } else {
        ""
    }

}

fun currentTime(): String {
    val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return format.format(Date())
}