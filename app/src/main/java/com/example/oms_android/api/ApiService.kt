package com.example.oms_android.api

import retrofit2.http.GET

interface ApiService {

    @GET("login/get/token")
    suspend fun getToken(): BaseResponseModel

}