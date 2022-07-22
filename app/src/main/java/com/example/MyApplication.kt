package com.example

import android.app.Application
import com.drake.statelayout.StateConfig
import com.drake.statelayout.handler.FadeStateChangedHandler
import com.example.oms_android.R
import com.example.oms_android.api.RetrofitClient
import com.example.oms_android.netstate.NetWorkListenerHelper
import com.example.oms_android.utilities.ACCESS_TOKEN
import com.google.android.material.color.DynamicColors
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking

class MyApplication : Application() {

    companion object {
        @Volatile
        private var instance: MyApplication? = null

        fun getInstance(): MyApplication {
            return instance ?: synchronized(this) {
                instance ?: MyApplication().also { instance = it }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        //Apply dynamic colors to all activities in the app
        DynamicColors.applyToActivitiesIfAvailable(this)
        //初始化MMKV
        MMKV.initialize(this)
        //设置加载状态页面
        StateConfig.apply {
            emptyLayout = R.layout.layout_empty
            errorLayout = R.layout.layout_error
            loadingLayout = R.layout.layout_loading
            stateChangedHandler = FadeStateChangedHandler()
            setRetryIds(R.id.msg, R.id.iv)
        }
        //开启全局网络监听
        NetWorkListenerHelper.init(this).requestNetworkListener()
        //获取并保存token
        val defaultMMKV = MMKV.defaultMMKV()
        if (defaultMMKV.decodeString(ACCESS_TOKEN, "") == "") {
            runBlocking {
                flow {
                    emit(RetrofitClient.getInstance().getService().getToken())
                }.flowOn(Dispatchers.IO).catch { e ->
                    e.printStackTrace()
                }.collect {
                    defaultMMKV.encode(ACCESS_TOKEN, it.data?.token.toString())
                }
            }
        }
    }

}