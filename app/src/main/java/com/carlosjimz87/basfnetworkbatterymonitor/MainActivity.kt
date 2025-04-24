package com.carlosjimz87.basfnetworkbatterymonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val viewModel: MainViewModel = koinViewModel()
                    val status by viewModel.uiState.collectAsState()

                    status?.let {
                        MainScreen(modifier = Modifier.padding(innerPadding), state = it)
                    }
                }
            }
        }
    }
}