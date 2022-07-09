package com.example.oms_android.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.oms_android.netstate.NetWorkListenerHelper
import com.example.oms_android.netstate.NetworkStatus
import com.tencent.mmkv.MMKV
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity(),
    NetWorkListenerHelper.NetWorkConnectedListener {

    lateinit var mContext: AppCompatActivity
    lateinit var vb: VB
    lateinit var vm: VM
    lateinit var mmkv: MMKV

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = javaClass.genericSuperclass as ParameterizedType

        val clazz0 = type.actualTypeArguments[0] as Class<VB>
        val method = clazz0.getMethod("inflate", LayoutInflater::class.java)
        vb = method.invoke(null, layoutInflater) as VB

        val clazz1 = type.actualTypeArguments[1] as Class<VM>
        vm = ViewModelProvider(this)[clazz1]

        setContentView(vb.root)
        mContext = this
        NetWorkListenerHelper.obtain().addListener(this)
        mmkv = MMKV.defaultMMKV()
        initView()
        initViewStates()
        initViewEvents()
    }

    abstract fun initView()
    abstract fun initViewStates()
    abstract fun initViewEvents()

    override fun onDestroy() {
        super.onDestroy()
        NetWorkListenerHelper.obtain().removeListener(this)
    }

    override fun onNetWorkConnected(isConnected: Boolean, networkStatus: NetworkStatus) {
        if (isConnected) {
            if (networkStatus == NetworkStatus.MOBILE) {

            } else if (networkStatus == NetworkStatus.WIFI) {

            } else {

            }
        }
    }

}