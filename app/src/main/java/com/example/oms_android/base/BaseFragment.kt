package com.example.oms_android.base

import android.content.Context
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
import com.example.oms_android.utilities.LogUtils
import java.lang.reflect.ParameterizedType

/**
 * @author: Clarse
 * @date: 2022/7/11
 */

private val TAG = BaseFragment::class.java.simpleName

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment(),
    NetWorkListenerHelper.NetWorkConnectedListener {

    lateinit var vb: VB
    lateinit var vm: VM

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LogUtils.d(TAG, "${this.javaClass.simpleName}：onAttach()")
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.d(TAG, "${this.javaClass.simpleName}：onCreate()")
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
        LogUtils.d(TAG, "${this.javaClass.simpleName}:onCreateView()")
        initView()
        initViewStates()
        initViewEvents()
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogUtils.d(TAG, "${this.javaClass.simpleName}：onViewCreated()")
    }

    override fun onStart() {
        super.onStart()
        LogUtils.d(TAG, "${this.javaClass.simpleName}：onStart()")
    }

    override fun onResume() {
        super.onResume()
        LogUtils.d(TAG, "${this.javaClass.simpleName}：onResume()")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.d(TAG, "${this.javaClass.simpleName}：onPause()")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.d(TAG, "${this.javaClass.simpleName}：onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogUtils.d(TAG, "${this.javaClass.simpleName}：onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d(TAG, "${this.javaClass.simpleName}：onDestroy()")
    }

    override fun onDetach() {
        super.onDetach()
        LogUtils.d(TAG, "${this.javaClass.simpleName}：onDetach()")
    }

    abstract fun initView()
    abstract fun initViewStates()
    abstract fun initViewEvents()

    override fun onNetWorkConnected(isConnected: Boolean, networkStatus: NetworkStatus) {

    }

}