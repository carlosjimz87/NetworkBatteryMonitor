package com.carlosjimz87.basfnetworkbatterymonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ReportFragment
import com.carlosjimz87.basfnetworkbatterymonitor.data.battery.BatteryMonitor
import com.carlosjimz87.basfnetworkbatterymonitor.data.connectivity.NetworkMonitor
import com.carlosjimz87.basfnetworkbatterymonitor.domain.repository.StatusRepository
import com.carlosjimz87.basfnetworkbatterymonitor.domain.repository.StatusRepositoryImpl
import com.carlosjimz87.basfnetworkbatterymonitor.ui.theme.BASFNetworkBatteryMonitorTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val repository: StatusRepository by inject()
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        repository.getStatus()
            .onEach { status ->
                println("🔥 STATUS: $status")
            }
            .launchIn(scope)

        setContent {
            BASFNetworkBatteryMonitorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Hola",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BASFNetworkBatteryMonitorTheme {
        Greeting("Android")
    }
}