package com.example.oms_android.netstate

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.example.oms_android.utilities.LogUtils
import java.util.concurrent.CopyOnWriteArrayList

class NetWorkListenerHelper(context: Context) {

    @Volatile
    private var mListenerList: CopyOnWriteArrayList<NetWorkConnectedListener>? = null
    private val mContext = context.applicationContext

    companion object {
        private val TAG = NetWorkListenerHelper::class.java.simpleName

        @Synchronized
        fun obtain(): NetWorkListenerHelper {
            return ObjectCachePool.getInstance()
                .getObj(NetWorkListenerHelper::class.java.simpleName) as NetWorkListenerHelper?
                ?: throw NullPointerException("The NetworkListenerHelper instance has not been initialized.")
        }

        @Synchronized
        fun init(context: Context): NetWorkListenerHelper {
            var obj = ObjectCachePool.getInstance()
                .getObj(NetWorkListenerHelper::class.java.simpleName) as NetWorkListenerHelper?
            if (null == obj) {
                obj = NetWorkListenerHelper(context)
            }
            ObjectCachePool.getInstance().cache(obj)
            return obj
        }

    }

    fun requestNetworkListener() {
        val cm =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (null == cm) {
            LogUtils.e(TAG, "registerNetworkListener#return#connectivityManager=$cm")
            return
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            cm.registerDefaultNetworkCallback(MyNetworkCallback())
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val builder = NetworkRequest.Builder()
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            val networkRequest = builder.build()
            cm.registerNetworkCallback(networkRequest, MyNetworkCallback())
        } else {
            val networkBroadcastReceiver = NetworkBroadcastReceiver()
            val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            mContext.registerReceiver(networkBroadcastReceiver, intentFilter)
            networkBroadcastReceiver.setBroadcastCallback(object : NetworkBroadcastCallback {
                override fun onNetworkBroadcastCallback(
                    isConnected: Boolean,
                    networkStatus: NetworkStatus
                ) {
                    notifyAllListeners(isConnected, networkStatus)
                }
            })
        }
    }

    /**
     * ?????????????????????
     * @param isConnected Boolean
     * @param networkStatus NetworkStatus
     */
    private fun notifyAllListeners(isConnected: Boolean, networkStatus: NetworkStatus) {
        if (mListenerList?.isNotEmpty() == true) {
            mListenerList?.forEach {
                it.onNetWorkConnected(isConnected, networkStatus)
            }
        }
    }

    /**
     * ????????????????????????
     * @param listener NetWorkConnectedListener?
     */
    @Synchronized
    fun addListener(listener: NetWorkConnectedListener?) {
        if (null == listener) return
        if (null == mListenerList) mListenerList = CopyOnWriteArrayList()
        if (mListenerList?.contains(listener) == false) mListenerList?.add(listener)
    }

    /**
     * ????????????????????????
     * @param listener NetWorkConnectedListener?
     */
    @Synchronized
    fun removeListener(listener: NetWorkConnectedListener?) {
        if (null != listener && mListenerList?.isNotEmpty() == true) mListenerList?.remove(listener)
    }

    fun unregisterNetWorkCallback() {
        if (null == mContext) return
        val cm =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (null == cm) {
            LogUtils.e(TAG, "registerNetworkListener#return#connectivityManager=$cm")
            return
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            cm.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
        }
    }

    private inner class MyNetworkCallback : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            LogUtils.d(TAG, "onAvailable#network=$network")
            //????????????????????????????????????
            val netWorkState = NetworkUtils.getNetworkState(mContext)
            LogUtils.d(TAG, "onAvailable#netWorkState=$netWorkState")
            notifyAllListeners(true, netWorkState)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            LogUtils.d(TAG, "onLost#network=$network")
            val networkState = NetworkUtils.getNetworkState(mContext)
            LogUtils.d(TAG, "onLost#netWorkState=$networkState")
            notifyAllListeners(false, networkState)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            LogUtils.d(TAG, "onCapabilitiesChanged#network=$network")
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    LogUtils.d(TAG, "onCapabilitiesChanged#????????????")
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    LogUtils.d(TAG, "onCapabilitiesChanged#???????????????wifi")
                } else {
                    LogUtils.d(TAG, "onCapabilitiesChanged#????????????")
                }
            }
        }

    }

    interface NetWorkConnectedListener {
        fun onNetWorkConnected(isConnected: Boolean, networkStatus: NetworkStatus)
    }

}