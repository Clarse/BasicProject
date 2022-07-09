package com.example.oms_android.base

import androidx.lifecycle.ViewModel
import com.example.oms_android.api.RetrofitClient

open class BaseViewModel : ViewModel() {

    val apiService by lazy { RetrofitClient.getInstance().getService() }

}