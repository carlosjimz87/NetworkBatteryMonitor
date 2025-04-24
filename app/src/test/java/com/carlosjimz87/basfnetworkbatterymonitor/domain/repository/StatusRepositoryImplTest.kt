package com.carlosjimz87.basfnetworkbatterymonitor.domain.repository

import app.cash.turbine.test
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.BatteryStatus
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkStatus
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class StatusRepositoryImplTest {

    private val testNetworkFlow = MutableStateFlow(
        NetworkStatus(type = NetworkType.WIFI, connected = true, hasInternet = true)
    )
    private val testBatteryFlow = MutableStateFlow(
        BatteryStatus(level = 80, isLow = false)
    )

    private val repository = FakeStatusRepositoryImpl(
        networkMonitor = FakeNetworkMonitor(testNetworkFlow),
        batteryMonitor = FakeBatteryMonitor(testBatteryFlow)
    )

    @Test
    fun `emits initial combined MonitoringStatus`() = runTest {
        val result = repository.getStatus().first()
        assertEquals(NetworkType.WIFI, result.network.type)
        assertEquals(80, result.battery.level)
    }

    @Test
    fun `emits new status when network changes`() = runTest {
        repository.getStatus().test {
            val first = awaitItem()
            assertEquals(NetworkType.WIFI, first.network.type)

            testNetworkFlow.value = NetworkStatus(NetworkType.CELLULAR, false, false)
            val second = awaitItem()
            assertEquals(NetworkType.CELLULAR, second.network.type)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `emits new status when battery changes`() = runTest {
        repository.getStatus().test {
            val first = awaitItem()
            assertEquals(80, first.battery.level)

            testBatteryFlow.value = BatteryStatus(15, true)
            val second = awaitItem()
            assertEquals(15, second.battery.level)
            assertEquals(true, second.battery.isLow)
        }
    }
}