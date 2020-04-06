package com.github.keyrillanskiy.city_mobil_test.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService

@Suppress("DEPRECATION")
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = getSystemService(context, ConnectivityManager::class.java) ?: return false
    if (Build.VERSION.SDK_INT < 23) {
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected &&
                (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE)
    } else {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }
}