package com.carlosjimz87.basfnetworkbatterymonitor.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.MonitoringState
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkStatus
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


fun MonitoringState.internetAvailability(): String {

    return if (this.network.hasInternet) {
        when (this.network.type) {
            NetworkType.OFFLINE -> ""
            else -> "(Online)"
        }
    } else {
        ""
    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun currentTime() = flow {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    while (true) {
        emit(LocalDateTime.now().format(formatter))
        delay(1000L) // Update every second
    }
}

/**
 * Display network loss message if the connection changes and is now disconnected
 */
suspend fun showSnackbarMessage(
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