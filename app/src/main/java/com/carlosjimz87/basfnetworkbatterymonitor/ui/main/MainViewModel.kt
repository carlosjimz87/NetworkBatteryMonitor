package com.carlosjimz87.basfnetworkbatterymonitor.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.MonitoringState
import com.carlosjimz87.basfnetworkbatterymonitor.domain.repository.StatusRepository
import com.carlosjimz87.basfnetworkbatterymonitor.ui.events.UiEvent
import com.carlosjimz87.basfnetworkbatterymonitor.utils.NotificationHelper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: StatusRepository
) : ViewModel() {

    // Holds the combined state (network, battery)
    private val _uiState = MutableStateFlow<MonitoringState?>(null)
    val uiState: StateFlow<MonitoringState?> = _uiState.asStateFlow()

    // Emits one-time UI events (notifications in this case)
    private val _events = MutableSharedFlow<UiEvent>()
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    // Flag to avoid triggering duplicate low battery notifications
    private var lowBatteryNotified = false

    init {
        viewModelScope.launch {
            // Collect combined status and update state + fire events
            repository.getStatus().collectLatest { status ->
                _uiState.value = status
                shouldShowBatteryNotification(status)
            }
        }
    }

    // Emits an event if the battery is low and no notification has been sent yet
    private suspend fun shouldShowBatteryNotification(status: MonitoringState) {
        if (status.battery.isLow && !lowBatteryNotified) {
            _events.emit(UiEvent.ShowLowBatteryNotification)
            lowBatteryNotified = true
        }
    }
}