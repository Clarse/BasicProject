package com.example.oms_android.api

/**
 * @author: Clarse
 * @date: 2022/7/9
 */
data class BaseResponseModel(val code: Int = 0, val msg: String? = null, val data: Data? = null)

data class Data(val token: String)