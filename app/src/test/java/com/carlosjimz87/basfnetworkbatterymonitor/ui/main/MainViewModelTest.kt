package com.carlosjimz87.basfnetworkbatterymonitor.ui.main

import com.carlosjimz87.basfnetworkbatterymonitor.data.models.BatteryStatus
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkStatus
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkType
import com.carlosjimz87.basfnetworkbatterymonitor.data.repository.FakeStatusRepository
import com.carlosjimz87.basfnetworkbatterymonitor.ui.events.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is null`() = runTest {
        val repo = FakeStatusRepository(
            MutableStateFlow(NetworkStatus(NetworkType.WIFI, true, true)),
            MutableStateFlow(BatteryStatus(50, false))
        )

        val vm = MainViewModel(repo)
        assertNull(vm.uiState.value)
    }


    @Test
    fun `does not emit low battery event if level is 25`() = runTest {
        val repo = FakeStatusRepository(
            MutableStateFlow(NetworkStatus(NetworkType.WIFI, true, true)),
            MutableStateFlow(BatteryStatus(25, false))
        )

        val vm = MainViewModel(repo)

        val events = mutableListOf<UiEvent>()
        val job = launch { vm.events.collect { events += it } }

        advanceUntilIdle()
        assertTrue(events.isEmpty())
        job.cancel()
    }


    @Test
    fun `emits low battery event only once even with multiple low values`() = runTest {
        val batteryFlow = MutableStateFlow(BatteryStatus(15, true))
        val networkFlow = MutableStateFlow(NetworkStatus(NetworkType.WIFI, true, true))

        val repo = FakeStatusRepository(networkFlow, batteryFlow)
        val vm = MainViewModel(repo)

        val events = mutableListOf<UiEvent>()
        val job = launch { vm.events.collect { events += it } }

        // First event (should trigger)
        advanceUntilIdle()

        // Still low → shouldn't trigger again
        batteryFlow.value = BatteryStatus(10, true)
        advanceUntilIdle()

        job.cancel()
        assertEquals(1, events.count { it is UiEvent.ShowLowBatteryNotification })
    }

    @Test
    fun `emits valid UI state when repo flows emit`() = runTest {
        val repo = FakeStatusRepository(
            MutableStateFlow(NetworkStatus(NetworkType.WIFI, true, true)),
            MutableStateFlow(BatteryStatus(50, false))
        )

        val vm = MainViewModel(repo)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertNotNull(state)
        assertEquals(50, state?.battery?.level)
        assertTrue(state?.network?.connected == true)
    }

    @Test
    fun `emits low battery event only once`() = runTest {
        val networkFlow =
            MutableStateFlow(NetworkStatus(type = NetworkType.WIFI, connected = true, hasInternet = true))
        val batteryFlow = MutableStateFlow(BatteryStatus(level = 15, isLow = true))

        val fakeRepo = FakeStatusRepository(networkFlow, batteryFlow)
        val viewModel = MainViewModel(fakeRepo)

        val events = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.events.collect { events += it }
        }

        // Simulate another low update, should not duplicate event
        batteryFlow.value = BatteryStatus(level = 10, isLow = true)

        advanceUntilIdle()
        job.cancel()

        assertEquals(1, events.count { it is UiEvent.ShowLowBatteryNotification })
    }


    @Test
    fun `network changes do not emit low battery event`() = runTest {
        val batteryFlow = MutableStateFlow(BatteryStatus(50, false))
        val networkFlow = MutableStateFlow(NetworkStatus(NetworkType.WIFI, true, true))

        val repo = FakeStatusRepository(networkFlow, batteryFlow)
        val vm = MainViewModel(repo)

        val events = mutableListOf<UiEvent>()
        val job = launch { vm.events.collect { events += it } }

        // Trigger network changes
        networkFlow.value = NetworkStatus(NetworkType.CELLULAR, false, false)
        networkFlow.value = NetworkStatus(NetworkType.WIFI, true, true)

        advanceUntilIdle()
        job.cancel()

        assertTrue(events.isEmpty())
    }

}