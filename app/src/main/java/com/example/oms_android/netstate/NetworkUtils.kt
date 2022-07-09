package com.example.oms_android.netstate

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkUtils {

    /**
     *
     * @param context Context
     * @return NetworkStatus
     */
    fun getNetworkState(context: Context?): NetworkStatus {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            val wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            return if (null != mobileInfo && mobileInfo.isAvailable) {
                NetworkStatus.MOBILE
            } else if (null != wifiInfo && wifiInfo.isAvailable) {
                NetworkStatus.WIFI
            } else {
                NetworkStatus.NONE
            }
        } else {
            return if (isMobileConnected(context)) {
                NetworkStatus.MOBILE
            } else if (isWifiConnected(context)) {
                NetworkStatus.WIFI
            } else {
                NetworkStatus.NONE
            }
        }
    }

    /**
     * 判断网络是否连接
     * @param context Context
     * @return Boolean
     */
    fun isOnline(context: Context?): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val ni = cm.activeNetworkInfo
            return null != ni && ni.isAvailable
        } else {
            val network = cm.activeNetwork ?: return false
            val networkCapabilities = cm.getNetworkCapabilities(network)
            if (null != networkCapabilities)
                return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
        return false
    }

    /**
     *
     * @param context Context
     * @return Boolean
     */
    fun isWifiConnected(context: Context?): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (null != networkInfo) return networkInfo.isAvailable
        } else {
            val activeNetwork = cm.activeNetwork
            activeNetwork ?: return false
            val networkCapabilities = cm.getNetworkCapabilities(activeNetwork)
            if (null != networkCapabilities)
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
        return false
    }

    /**
     *
     * @param context Context
     * @return Boolean
     */
    fun isMobileConnected(context: Context?): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            if (null != networkInfo) return networkInfo.isAvailable
        } else {
            val activeNetwork = cm.activeNetwork
            activeNetwork ?: return false
            val networkCapabilities = cm.getNetworkCapabilities(activeNetwork)
            if (null != networkCapabilities)
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
        return false
    }

}