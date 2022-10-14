package com.example.oms_android.base

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.oms_android.netstate.NetWorkListenerHelper
import com.example.oms_android.netstate.NetworkStatus
import com.example.oms_android.utilities.ActivityManager
import com.example.oms_android.utilities.LogUtils
import com.example.oms_android.utilities.toast
import com.tencent.mmkv.MMKV
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity(),
    NetWorkListenerHelper.NetWorkConnectedListener {

    lateinit var mContext: AppCompatActivity
    lateinit var vb: VB
    lateinit var vm: VM
    lateinit var mmkv: MMKV
    private var time = 0L

    companion object {
        private val TAG = BaseActivity::class.java.simpleName
    }

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

        EventBus.getDefault().register(this)

        //添加activity栈
        ActivityManager.getInstance().addActivity(this)

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
        EventBus.getDefault().unregister(this)
        //移除activity栈
        ActivityManager.getInstance().deleteActivity(this)
    }

    abstract fun initView()
    abstract fun initViewStates()
    abstract fun initViewEvents()

    override fun onNetWorkConnected(isConnected: Boolean, networkStatus: NetworkStatus) {
        if (isConnected) {
            when (networkStatus) {
                NetworkStatus.MOBILE -> {
                    toast("当前正在使用移动网络，请注意流量消耗")
                }
                NetworkStatus.WIFI -> {

                }
                else -> {

                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
        }
        return true
    }

    private fun exit() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis()
            toast("再按一次退出应用程序")
        } else {
            ActivityManager.getInstance().finishAllActivity()
        }
    }

}