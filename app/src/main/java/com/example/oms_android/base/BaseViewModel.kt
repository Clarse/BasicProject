package com.example.oms_android.base

import androidx.lifecycle.ViewModel
import com.example.oms_android.api.BaseResponseModel
import com.example.oms_android.api.RetrofitClient
import com.google.gson.Gson
import retrofit2.HttpException

open class BaseViewModel : ViewModel() {
    val apiService = RetrofitClient.getInstance().getService()

    suspend fun <T> launcher(
        final: () -> Unit = {},
        block: suspend () -> BaseResponseModel<T>
    ): Result<T> {
        return try {
            block().getResult()
        } catch (e: Exception) {
            if (e is HttpException) {
                val json = Gson().fromJson(
                    e.response()?.errorBody()?.string(),
                    BaseResponseModel::class.java
                )
                Result.failure(Exception(json.msg))
            } else {
                Result.failure(e)
            }
        } finally {
            final()
        }
    }

}