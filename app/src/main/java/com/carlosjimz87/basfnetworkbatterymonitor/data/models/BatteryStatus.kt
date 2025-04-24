package com.carlosjimz87.basfnetworkbatterymonitor.data.models


data class BatteryStatus(
    val level: Int = -1,
    val isLow: Boolean = false
) {
    override fun toString(): String = if (isLow) "🔋 $level% (Low)" else "🔋 $level%"
}