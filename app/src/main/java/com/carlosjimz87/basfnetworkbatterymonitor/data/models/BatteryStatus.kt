package com.carlosjimz87.basfnetworkbatterymonitor.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BatteryStatus(
    val level: Int = -1,
    val isLow: Boolean = false
) : Parcelable {
    override fun toString(): String = if (isLow) "🔋 $level% (Low)" else "🔋 $level%"
}