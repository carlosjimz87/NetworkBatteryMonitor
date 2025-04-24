package com.carlosjimz87.basfnetworkbatterymonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.carlosjimz87.basfnetworkbatterymonitor.common.showSnackbarMessage
import com.carlosjimz87.basfnetworkbatterymonitor.ui.events.UiEvent
import com.carlosjimz87.basfnetworkbatterymonitor.ui.main.MainScreen
import com.carlosjimz87.basfnetworkbatterymonitor.ui.main.MainViewModel
import com.carlosjimz87.basfnetworkbatterymonitor.ui.theme.BASFNetworkBatteryMonitorTheme
import com.carlosjimz87.basfnetworkbatterymonitor.utils.NotificationHelper
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BASFNetworkBatteryMonitorTheme {

                val viewModel: MainViewModel = koinViewModel()
                val status by viewModel.uiState.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }
                val previousStatus = remember { mutableStateOf(status) }

                // Show snackbar when network status changes (disconnect only for better UX)
                LaunchedEffect(status?.network) {
                    val previous = previousStatus.value?.network
                    val current = status?.network

                    showSnackbarMessage(previous, current, snackbarHostState)

                    previousStatus.value = status
                }

                // Collect UI events (like low battery warning) from the ViewModel
                LaunchedEffect(Unit) {
                    viewModel.events.collectLatest { event ->
                        when (event) {
                            UiEvent.ShowLowBatteryNotification -> {
                                NotificationHelper.showBatteryLowNotification(this@MainActivity)
                            }
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    status?.let {
                        MainScreen(
                            modifier = Modifier.padding(innerPadding),
                            state = it
                        )
                    }
                }
            }
        }
    }
}