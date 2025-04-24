package com.carlosjimz87.basfnetworkbatterymonitor.data.connectivity

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkStatus
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class NetworkMonitor(private val connectivityManager: ConnectivityManager) {
    @Volatile
    private var lastStatus: NetworkStatus? = null

    @RequiresPermission(ACCESS_NETWORK_STATE)
    val networkStatusFlow: Flow<NetworkStatus> = callbackFlow  {
        val callback = object : ConnectivityManager.NetworkCallback() {
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

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        getNetworkDetails()
            .takeIf { it != lastStatus }
            ?.let {
                trySend(it)
                lastStatus = it
            }

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    private fun ProducerScope<NetworkStatus>.updateStatus() {
        getNetworkDetails()
            .takeIf { it != lastStatus }
            ?.let {
                trySend(it)
                lastStatus = it
            }
    }


    private fun getNetworkDetails(): NetworkStatus {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        val connection = capabilities != null
        val hasInternet = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        val type = when {
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> "Wi-Fi"
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> "Mobile"
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> "Ethernet"
            else -> "Unknown"
        }

        return NetworkStatus(connected = connection, type = type, hasInternet = hasInternet)
    }
}