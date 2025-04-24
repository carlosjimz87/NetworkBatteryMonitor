package com.carlosjimz87.basfnetworkbatterymonitor.ui.events

sealed class UiEvent {
    data object ShowLowBatteryNotification : UiEvent()
}