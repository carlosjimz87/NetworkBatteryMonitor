package com.carlosjimz87.basfnetworkbatterymonitor.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.carlosjimz87.basfnetworkbatterymonitor.common.currentTime
import com.carlosjimz87.basfnetworkbatterymonitor.common.internetAvailability
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.MonitoringState

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    state: MonitoringState
) {
    val networkColor by animateColorAsState(
        targetValue = if (state.network.connected) Color(0xFF2E7D32) else Color(0xFFC62828),
        label = "NetworkColor"
    )

    val batteryColor by animateColorAsState(
        targetValue = if (state.battery.isLow) Color(0xFFFFA000) else Color(0xFF1976D2),
        label = "BatteryColor"
    )

    val time = remember { mutableStateOf(currentTime()) }
    LaunchedEffect(state) {
        time.value = currentTime()
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        val isCompact = maxWidth < 360.dp

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "🔌 Network Status",
                style = if (isCompact) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleMedium
            )
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

            AnimatedVisibility(visible = state.battery.isLow) {
                Text(
                    "⚠️ Battery is low!",
                    color = Color.Red,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "Last update: ${time.value}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}