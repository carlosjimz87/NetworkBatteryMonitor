package com.carlosjimz87.basfnetworkbatterymonitor.data.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.carlosjimz87.basfnetworkbatterymonitor.common.Constants.MIN_BATTERY_WARNING_VALUE
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.BatteryStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

 class BatteryMonitor(private val context: Context) {

     val batteryStatusFlow: Flow<BatteryStatus> = callbackFlow {

        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)

        // Retrieve and emit the latest battery status immediately (sticky broadcast returns last known Intent)
        val stickyIntent = context.registerReceiver(null, intentFilter)
        stickyIntent?.let { intent ->
            val level = intent.getIntExtra("level", -1)
            val isLow = level in 0..MIN_BATTERY_WARNING_VALUE
            trySend(BatteryStatus(level, isLow))
        }

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                // Emit battery status on every system update
                val level = intent?.getIntExtra("level", -1) ?: -1
                val isLow = level in 0..MIN_BATTERY_WARNING_VALUE
                trySend(BatteryStatus(level, isLow))
            }
        }

        // Register receiver for battery status changes
        ContextCompat.registerReceiver(
            context,
            receiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        // Cleanup when flow is closed
        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }
}
