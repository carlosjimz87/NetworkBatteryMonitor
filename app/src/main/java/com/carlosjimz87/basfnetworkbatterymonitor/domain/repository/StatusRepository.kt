package com.carlosjimz87.basfnetworkbatterymonitor.domain.repository

import com.carlosjimz87.basfnetworkbatterymonitor.data.models.MonitoringState
import kotlinx.coroutines.flow.Flow

interface StatusRepository {
    fun getStatus(): Flow<MonitoringState>
}