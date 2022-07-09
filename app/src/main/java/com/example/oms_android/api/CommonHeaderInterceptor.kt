package com.example.oms_android.api

import com.example.oms_android.utilities.ACCESS_TOKEN
import com.tencent.mmkv.MMKV
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author: Clarse
 * @date: 2022/7/9
 */
class CommonHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()//获取旧链接
        val newBuilder = oldRequest.newBuilder()//建立新的构建者
        //将旧的请求方式和请求体设置到新的请求中
        newBuilder.method(oldRequest.method, oldRequest.body)
        val headers = oldRequest.headers//获取旧请求头
        val names = headers.names()
        names.forEach {
            //将旧请求的头设置到新请求中
            newBuilder.addHeader(it, headers[it]!!)
        }
        //添加额外的自定义公共请求头
        if (MMKV.defaultMMKV().decodeString(ACCESS_TOKEN, "") != "") {
            newBuilder.header(
                "accessToken",
                MMKV.defaultMMKV().decodeString(ACCESS_TOKEN).toString()
            )
        }
        val request = newBuilder.build()
        return chain.proceed(request)
    }

}