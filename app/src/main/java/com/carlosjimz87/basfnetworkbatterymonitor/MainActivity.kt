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
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkStatus
import com.carlosjimz87.basfnetworkbatterymonitor.ui.main.MainScreen
import com.carlosjimz87.basfnetworkbatterymonitor.ui.main.MainViewModel
import com.carlosjimz87.basfnetworkbatterymonitor.ui.theme.BASFNetworkBatteryMonitorTheme
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

                LaunchedEffect(status?.network) {
                    val previous = previousStatus.value?.network
                    val current = status?.network

                    shouldShowSnackMessage(previous, current, snackbarHostState)

                    previousStatus.value = status
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

    private suspend fun shouldShowSnackMessage(
        previous: NetworkStatus?,
        current: NetworkStatus?,
        snackbarHostState: SnackbarHostState
    ) {
        if (previous != null && current != null && current != previous) {
            if (!current.connected) { // only showing lost connection for better UX
                snackbarHostState.showSnackbar("❌ Lost network connection")
            }
        }
    }
}