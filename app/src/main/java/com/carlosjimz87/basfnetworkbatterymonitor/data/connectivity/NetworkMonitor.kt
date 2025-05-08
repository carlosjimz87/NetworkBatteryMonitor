package com.carlosjimz87.basfnetworkbatterymonitor.data.connectivity


import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.content.Context
import android.database.ContentObserver
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Global.AIRPLANE_MODE_ON
import android.provider.Settings.Global.getInt
import android.provider.Settings.Global.getUriFor
import android.util.Log
import androidx.annotation.RequiresPermission
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkStatus
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkType
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class NetworkMonitor(
    private val context: Context,
    private val connectivityManager: ConnectivityManager
) {

    private var lastStatus: NetworkStatus? = null

    @RequiresPermission(ACCESS_NETWORK_STATE)
    val networkStatusFlow: Flow<NetworkStatus> = callbackFlow {

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                updateStatus()
            }

            override fun onLost(network: Network) {
                updateStatus()
            }

            override fun onUnavailable() {
                updateStatus()
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                updateStatus()
            }
        }

        // ContentObserver for airplane mode changes
        val airplaneModeObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                updateStatus()
            }
        }

        // Register network callback
        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        // Register ContentObserver for airplane mode
        context.contentResolver.registerContentObserver(
            getUriFor(AIRPLANE_MODE_ON),
            false,
            airplaneModeObserver
        )

        // Initial status
        getNetworkDetails()
            .takeIf { it != lastStatus }
            ?.let {
                trySend(it)
                lastStatus = it
            }

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
            context.contentResolver.unregisterContentObserver(airplaneModeObserver)
        }
    }.distinctUntilChanged()

    private fun ProducerScope<NetworkStatus>.updateStatus() {
        getNetworkDetails()
            .takeIf { it != lastStatus } // Avoid sending the same status
            ?.let {
                trySend(it)
                lastStatus = it
            }
    }

    private fun getNetworkDetails(): NetworkStatus {
        // Check airplane mode status
        val isAirplaneModeOn = getInt(
            context.contentResolver,
            AIRPLANE_MODE_ON,
            0) != 0

        // If airplane mode is on, return OFFLINE status
        if (isAirplaneModeOn) {
            Log.d("NetworkMonitor", "AirplaneMode ON")
            return NetworkStatus(
                connected = false,
                type = NetworkType.OFFLINE,
                hasInternet = false
            )
        }

        // Otherwise, check network details
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        val isConnected = capabilities != null
        val hasInternet = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        val type = when {
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> NetworkType.WIFI
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> NetworkType.CELLULAR
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> NetworkType.ETHERNET
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true -> NetworkType.VPN
            else -> NetworkType.OFFLINE
        }

        val result = NetworkStatus(connected = isConnected, type = type, hasInternet = hasInternet)
        Log.d("NetworkMonitor", "Network on: $result")
        return result
    }
}