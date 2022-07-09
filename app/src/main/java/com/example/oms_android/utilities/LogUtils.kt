package com.example.oms_android.utilities

import android.util.Log

object LogUtils {

    fun i(TAG: String, message: String) {
        Log.i(TAG, message)
    }

    fun e(TAG: String, message: String) {
        Log.e(TAG, message)
    }

    fun w(TAG: String, message: String) {
        Log.w(TAG, message)
    }

    fun d(TAG: String, message: String) {
        Log.d(TAG, message)
    }

    fun v(TAG: String, message: String) {
        Log.v(TAG, message)
    }

}