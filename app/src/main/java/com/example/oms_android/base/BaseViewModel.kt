package com.example.oms_android.base

import androidx.lifecycle.ViewModel
import com.example.oms_android.api.BaseResponseModel
import com.example.oms_android.api.RetrofitClient

open class BaseViewModel : ViewModel() {
    val apiService = RetrofitClient.getInstance().getService()

    suspend fun <T> launcher(
        final: () -> Unit = {},
        block: suspend () -> BaseResponseModel<T>
    ): Result<T> {
        return try {
            block().getResult()
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            final()
        }
    }

}