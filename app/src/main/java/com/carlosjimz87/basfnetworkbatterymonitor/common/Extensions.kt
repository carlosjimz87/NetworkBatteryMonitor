package com.carlosjimz87.basfnetworkbatterymonitor.common

import com.carlosjimz87.basfnetworkbatterymonitor.data.models.MonitoringState
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkType


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