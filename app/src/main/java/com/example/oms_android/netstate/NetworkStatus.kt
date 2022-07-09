package com.example.oms_android.netstate

enum class NetworkStatus(code: Int, msg: String) {
    NONE(-1, "无网络连接"),
    MOBILE(0, "移动网络连接"),
    WIFI(1, "WIFI连接");

    private var status = code
    private var desc = msg

    fun getStatus(): Int {
        return status
    }

    fun getDesc(): String {
        return desc
    }

    override fun toString(): String {
        return "NetworkStatus{" +
                "status=" + status +
                ", desc='" + desc + '\'' +
                "} " + super.toString();
    }
}