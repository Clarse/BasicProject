package com.example.oms_android.api

/**
 * @author: Clarse
 * @date: 2022/7/9
 */
data class BaseResponseModel<T>(
    val code: Int = 0,
    val msg: String? = null,
    val id: Int? = null,
    val data: T? = null,
    val success: String? = null
) {
    fun getResult(): Result<T> {
        return if (code == 0) {
            Result.success(data!!)
        } else {
            Result.failure(Exception("$msg"))
        }
    }
}
