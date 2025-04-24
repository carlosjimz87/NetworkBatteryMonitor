package com.carlosjimz87.basfnetworkbatterymonitor.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.carlosjimz87.basfnetworkbatterymonitor.common.internetAvailability
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.MonitoringState
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkType

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    state: MonitoringState
) {
    val networkColor = if (state.network.connected) Color(0xFF2E7D32) else Color(0xFFC62828)
    val batteryColor = if (state.battery.isLow) Color(0xFFFFA000) else Color(0xFF1976D2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🔌 Network Status", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "${state.network.type} ${state.internetAvailability()}",
            color = networkColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text("🔋 Battery Status", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "${state.battery.level}%",
            color = batteryColor,
            fontWeight = FontWeight.Bold
        )

        if (state.battery.isLow) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("⚠️ Battery is low!", color = Color.Red, fontWeight = FontWeight.SemiBold)
        }
    }
}

