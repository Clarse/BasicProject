package com.example.oms_android.utilities

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.*
import kotlin.system.exitProcess

/**
 * @author: Clarse
 * @date: 2022/7/9
 */
class ActivityManager {

    private var mActivityStack: Stack<WeakReference<Activity>>? = null

    companion object {
        @Volatile
        private var instance: ActivityManager? = null

        fun getInstance(): ActivityManager {
            return instance ?: synchronized(this) {
                instance ?: ActivityManager().also { instance = it }
            }
        }
    }

    fun addActivity(activity: Activity) {
        if (null == mActivityStack) {
            mActivityStack = Stack()
        }
        mActivityStack?.add(WeakReference(activity))
    }

    fun deleteActivity(activity: Activity): Boolean {
        if (null != mActivityStack) {
            val it = mActivityStack!!.iterator()
            while (it.hasNext()) {
                mActivityStack?.iterator()?.let {
                    val temp = it.next().get()
                    if (null == temp) {
                        it.remove()
                    }
                    if (temp == activity) {
                        it.remove()
                        return true
                    }
                }
            }
        }
        return false
    }

    fun checkWeakReference() {
        if (null != mActivityStack) {
            val it = mActivityStack!!.iterator()
            while (it.hasNext()) {
                val temp = it.next().get()
                if (temp == null) {
                    it.remove()
                }
            }
        }
    }

    fun currentActivity(): Activity? {
        checkWeakReference()
        if (mActivityStack?.isNotEmpty() == true) {
            return mActivityStack?.lastElement()?.get()
        }
        return null
    }

    fun finishSpecialActivity(activity: Activity) {
        if (deleteActivity(activity)) {
            activity.finish()
        }
    }

    fun finishCurrentActivity() {
        val currentActivity = currentActivity()
        if (null != currentActivity) {
            finishSpecialActivity(currentActivity)
        }
    }

    fun <T> finishSpecialActivity(clazz: Class<T>) {
        if (mActivityStack != null) {
            // Safely remove the activity with java's iterator.
            val it = mActivityStack!!.iterator()
            while (it.hasNext()) {
                val activityReference = it.next()
                val activity = activityReference.get()
                if (activity == null) {
                    it.remove()
                    continue
                }
                if (activity.javaClass == clazz) {
                    it.remove()
                    activity.finish()
                }
            }
        }
    }

    @Synchronized
    fun finishAllActivity() {
        if (mActivityStack != null) {
            try {
                for (activityReference in mActivityStack!!) {
                    val activity = activityReference.get()
                    activity?.finish()
                }
                mActivityStack!!.clear()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun exitApp() {
        try {
            finishAllActivity()
            /**
             * Exit jvm and release all the resources occupied the memory.
             * The zero means exiting the application normally.
             */
            exitProcess(0)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}