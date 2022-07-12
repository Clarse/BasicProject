package com.example.oms_android.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.oms_android.netstate.NetWorkListenerHelper
import com.example.oms_android.netstate.NetworkStatus
import com.example.oms_android.utilities.LogUtils
import com.tencent.mmkv.MMKV
import java.lang.reflect.ParameterizedType

private val TAG = BaseActivity::class.java.simpleName

abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity(),
    NetWorkListenerHelper.NetWorkConnectedListener {

    lateinit var mContext: AppCompatActivity
    lateinit var vb: VB
    lateinit var vm: VM
    lateinit var mmkv: MMKV

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.d(TAG, "${this.javaClass.simpleName}:onCreate()")
        val type = javaClass.genericSuperclass as ParameterizedType

        val clazz0 = type.actualTypeArguments[0] as Class<VB>
        val method = clazz0.getMethod("inflate", LayoutInflater::class.java)
        vb = method.invoke(null, layoutInflater) as VB

        val clazz1 = type.actualTypeArguments[1] as Class<VM>
        vm = ViewModelProvider(this)[clazz1]

        // Displaying edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(vb.root)
        mContext = this
        NetWorkListenerHelper.obtain().addListener(this)
        mmkv = MMKV.defaultMMKV()
        initView()
        initViewStates()
        initViewEvents()
    }

    override fun onStart() {
        super.onStart()
        LogUtils.d(TAG, "${this.javaClass.simpleName}:onStart()")
    }

    override fun onResume() {
        super.onResume()
        LogUtils.d(TAG, "${this.javaClass.simpleName}:onResume()")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.d(TAG, "${this.javaClass.simpleName}:onPause()")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.d(TAG, "${this.javaClass.simpleName}:onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d(TAG, "${this.javaClass.simpleName}:onDestroy()")
        NetWorkListenerHelper.obtain().removeListener(this)
    }

    abstract fun initView()
    abstract fun initViewStates()
    abstract fun initViewEvents()

    override fun onNetWorkConnected(isConnected: Boolean, networkStatus: NetworkStatus) {
        if (isConnected) {
            if (networkStatus == NetworkStatus.MOBILE) {

            } else if (networkStatus == NetworkStatus.WIFI) {

            } else {

            }
        }
    }

}