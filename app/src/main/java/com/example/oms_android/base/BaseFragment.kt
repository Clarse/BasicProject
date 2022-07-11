package com.example.oms_android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.oms_android.netstate.NetWorkListenerHelper
import com.example.oms_android.netstate.NetworkStatus
import java.lang.reflect.ParameterizedType

/**
 * @author: Clarse
 * @date: 2022/7/11
 */
abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment(),
    NetWorkListenerHelper.NetWorkConnectedListener {

    lateinit var vb: VB
    lateinit var vm: VM

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = javaClass.genericSuperclass as ParameterizedType
        val clazz1 = type.actualTypeArguments[0] as Class<VB>
        val method = clazz1.getMethod("inflate", LayoutInflater::class.java)
        vb = method.invoke(null, layoutInflater) as VB
        val clazz2 = type.actualTypeArguments[1] as Class<VM>
        vm = ViewModelProvider(this)[clazz2]

        NetWorkListenerHelper.obtain().addListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView()
        initViewStates()
        initViewEvents()
        return vb.root
    }

    abstract fun initView()
    abstract fun initViewStates()
    abstract fun initViewEvents()

    override fun onNetWorkConnected(isConnected: Boolean, networkStatus: NetworkStatus) {

    }

}