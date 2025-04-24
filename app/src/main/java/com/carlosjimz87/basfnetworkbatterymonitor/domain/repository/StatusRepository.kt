package com.carlosjimz87.basfnetworkbatterymonitor.domain.repository

import com.carlosjimz87.basfnetworkbatterymonitor.data.models.MonitoringStatus
import kotlinx.coroutines.flow.Flow

interface StatusRepository {
    fun getStatus(): Flow<MonitoringStatus>
}