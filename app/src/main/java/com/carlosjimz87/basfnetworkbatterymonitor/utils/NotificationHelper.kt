package com.carlosjimz87.basfnetworkbatterymonitor.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.carlosjimz87.basfnetworkbatterymonitor.R
import com.carlosjimz87.basfnetworkbatterymonitor.common.Constants

object NotificationHelper {
    private const val CHANNEL_ID = "battery_alert_channel"

    fun showBatteryLowNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Battery Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_battery_alert)
            .setContentTitle("Battery Alert")
            .setContentText("⚠️ Battery level is below ${Constants.MIN_BATTERY_WARNING_VALUE}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(1001, notification)
    }
}
