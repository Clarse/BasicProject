package com.example.oms_android.utilities

/**
 * @author: Clarse
 * @date: 2022/7/9
 */
class ActivityManager {

    companion object {
        @Volatile
        private var instance: ActivityManager? = null

        fun getInstance(): ActivityManager {
            return instance ?: synchronized(this) {
                instance ?: ActivityManager().also { instance = it }
            }
        }
    }
}