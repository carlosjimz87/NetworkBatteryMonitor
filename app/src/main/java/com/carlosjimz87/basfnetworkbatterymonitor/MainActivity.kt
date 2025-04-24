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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                        println("🔥 NETWORK: ${it.network}")
                        println("🔋 BATTERY: ${it.battery}")
                    }

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