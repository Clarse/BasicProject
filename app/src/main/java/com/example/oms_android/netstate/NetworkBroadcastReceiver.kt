package com.example.oms_android.netstate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.text.TextUtils
import com.example.oms_android.utilities.LogUtils

/**
 * @author: Clarse
 * @date: 2022/7/8
 */

private val TAG = NetworkBroadcastReceiver::class.java.simpleName

class NetworkBroadcastReceiver : BroadcastReceiver() {

    private var mBroadcastCallback: NetworkBroadcastCallback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (null == intent || null == intent.action) {
            LogUtils.e(TAG, "onReceive#intent=$intent")
            return
        }
        val action = intent.action
        if (TextUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)) {
            val isOnLine = NetworkUtils.isOnline(context)
            val networkState = NetworkUtils.getNetworkState(context)
            LogUtils.d(TAG, "onReceive#isOnline=$isOnLine, networkStatus=$networkState")
            if (null != mBroadcastCallback) {
                mBroadcastCallback?.onNetworkBroadcastCallback(isOnLine, networkState)
            }
        }
    }

    fun setBroadcastCallback(broadcastCallback: NetworkBroadcastCallback) {
        mBroadcastCallback = broadcastCallback
    }

}

interface NetworkBroadcastCallback {
    fun onNetworkBroadcastCallback(isConnected: Boolean, networkStatus: NetworkStatus)
}