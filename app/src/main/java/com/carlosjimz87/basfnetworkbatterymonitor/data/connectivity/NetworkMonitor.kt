package com.carlosjimz87.basfnetworkbatterymonitor.data.connectivity

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkStatus
import com.carlosjimz87.basfnetworkbatterymonitor.data.models.NetworkType
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

        // Build a request that listens only for networks with internet capability
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        // Register the network callback to start receiving updates
        connectivityManager.registerNetworkCallback(request, callback)

        // Emit initial status immediately if it's different from the last emitted one
        getNetworkDetails()
            .takeIf { it != lastStatus }
            ?.let {
                trySend(it)
                lastStatus = it
            }

        // Clean up callback when the flow is closed
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    // Helper to update the flow only when the network status changes
    private fun ProducerScope<NetworkStatus>.updateStatus() {
        getNetworkDetails()
            .takeIf { it != lastStatus }
            ?.let {
                trySend(it)
                lastStatus = it
            }
    }


    // Retrieves the current network details (type, connectivity, internet access)
    private fun getNetworkDetails(): NetworkStatus {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        val connection = capabilities != null
        val hasInternet = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        val type = when {
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> NetworkType.WIFI
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> NetworkType.CELLULAR
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> NetworkType.ETHERNET
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true -> NetworkType.VPN
            else -> NetworkType.OFFLINE // there are more types but we will consider it offline for this demo
        }

        return NetworkStatus(connected = connection, type = type, hasInternet = hasInternet)
    }
}