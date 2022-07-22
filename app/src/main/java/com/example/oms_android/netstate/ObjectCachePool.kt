package com.example.oms_android.netstate

import com.example.oms_android.utilities.LogUtils
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ObjectCachePool {

    companion object {
        private val TAG = ObjectCachePool::class.java.simpleName
        @Volatile
        private var instance: ObjectCachePool? = null

        @Volatile
        private var OBJECT_CACHE: ConcurrentMap<String, Any>? = null

        fun getInstance(): ObjectCachePool {
            return instance ?: synchronized(this) {
                instance ?: ObjectCachePool().also { instance = it }
            }
        }
    }

    @Synchronized
    fun cache(obj: Any) {
        if (null == OBJECT_CACHE) {
            OBJECT_CACHE = ConcurrentHashMap()
        }
        val key = obj::class.java.simpleName
        LogUtils.d(TAG, "method:cache#key=$key, obj=$obj")
        OBJECT_CACHE?.put(key, obj)
    }

    @Synchronized
    fun cache(key: String, obj: Any) {
        if (null == OBJECT_CACHE) {
            OBJECT_CACHE = ConcurrentHashMap()
        }
        LogUtils.d(TAG, "method:cache#key=$key, obj=$obj")
        OBJECT_CACHE?.put(key, obj)
    }

    fun <T> getObj(clazz: Class<T>): Any? {
        LogUtils.d(TAG, "method:getObj#clazz=$clazz")
        val className = clazz.name
        return getObj(className)
    }

    fun getObj(key: String): Any? {
        LogUtils.d(TAG, "method:getObj#key=$key")
        if (null == OBJECT_CACHE) return null
        return OBJECT_CACHE?.get(key)
    }

    @Synchronized
    fun containsObj(key: String): Boolean {
        if (null == OBJECT_CACHE) return false
        val obj = getObj(key)
        if (null != obj) return true
        return false
    }

    @Synchronized
    fun <T> remove(clazz: Class<T>) {
        LogUtils.d(TAG, "method:getObj#clazz=$clazz")
        val className = clazz.name
        LogUtils.d(TAG, "method:getObj#className=$className")
        remove(className)
    }

    @Synchronized
    fun remove(key: String) {
        LogUtils.d(TAG, "method:remove#key=$key")
        if (null != OBJECT_CACHE && key.isNotEmpty()) OBJECT_CACHE!!.remove(key)
        LogUtils.d(TAG, "method:remove#OBJECT_CACHE=$OBJECT_CACHE")
    }

    @Synchronized
    fun evictAll() {
        LogUtils.d(TAG, "method:evictAll")
        if (null != OBJECT_CACHE) {
            OBJECT_CACHE?.clear()
            OBJECT_CACHE = null
        }
        LogUtils.d(TAG, "method:evictAll#OBJECT_CACHE=$OBJECT_CACHE")
    }

    @Synchronized
    fun <T> obtainInstance(clazz: Class<T>): Any? {
        LogUtils.d(TAG, "method:obtainInstance#in#arguments:clazz=$clazz")
        val className = clazz.name
        LogUtils.d(TAG, "method:obtainInstance#className=$className")
        val obj = getObj(className)
        LogUtils.d(TAG, "method:obtainInstance#obj=$obj")
        obj ?: synchronized(this) {
            obj ?: {
                val c0 = clazz.getDeclaredConstructor()
                c0.isAccessible = true
                c0.newInstance()
            }
        }
        if (null != obj) {
            cache(className, obj)
        }
        return obj
    }

}