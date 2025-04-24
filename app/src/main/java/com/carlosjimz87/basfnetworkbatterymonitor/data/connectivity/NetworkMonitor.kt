package com.carlosjimz87.basfnetworkbatterymonitor.data.connectivity

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class NetworkMonitor(private val connectivityManager: ConnectivityManager) {

    @RequiresPermission(ACCESS_NETWORK_STATE)
    val networkStatusFlow: Flow<NetworkStatus> = callbackFlow  {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(NetworkStatus.Connected)
            }

            override fun onLost(network: Network) {
                trySend(NetworkStatus.Disconnected)
            }

            override fun onUnavailable() {
                trySend(NetworkStatus.Disconnected)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Emit initial state
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val isConnected = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        trySend(if (isConnected) NetworkStatus.Connected else NetworkStatus.Disconnected)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}