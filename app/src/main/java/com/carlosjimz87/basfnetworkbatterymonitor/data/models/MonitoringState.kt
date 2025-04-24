package com.carlosjimz87.basfnetworkbatterymonitor.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MonitoringState(
    val network: NetworkStatus,
    val battery: BatteryStatus
): Parcelable