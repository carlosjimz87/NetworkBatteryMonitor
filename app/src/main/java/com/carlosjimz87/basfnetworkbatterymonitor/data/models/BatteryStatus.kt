package com.carlosjimz87.basfnetworkbatterymonitor.data.models


data class BatteryStatus(
    val level: Int,
    val isLow: Boolean
) {
    override fun toString(): String = if (isLow) "🔋 $level% (Low)" else "🔋 $level%"
}