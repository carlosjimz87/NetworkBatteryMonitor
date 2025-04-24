package com.carlosjimz87.basfnetworkbatterymonitor.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.MonitoringState
import com.carlosjimz87.basfnetworkbatterymonitor.domain.repository.StatusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: StatusRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MonitoringState?>(null)
    val uiState: StateFlow<MonitoringState?> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getStatus().collectLatest { status ->
                _uiState.value = status
            }
        }
    }
}