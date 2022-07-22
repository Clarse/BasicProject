package com.example.oms_android.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author: Clarse
 * @date: 2022/7/9
 */
class RetrofitClient {

    private val mService by lazy { getInstance().create() }

    init {
        retrofit = Retrofit.Builder()
            .client(getOkhttpClient())
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {
        private lateinit var retrofit: Retrofit
        var base_url = "http://112.15.71.78:8081/xilinmen/app/"

        @Volatile
        private var instance: RetrofitClient? = null

        fun getInstance(): RetrofitClient {
            return instance ?: synchronized(this) {
                instance ?: RetrofitClient().also { instance = it }
            }
        }
    }

    private fun getOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(CommonHeaderInterceptor())
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    private fun create(): ApiService = retrofit.create(ApiService::class.java)

    fun getService(): ApiService {
        return mService
    }

}