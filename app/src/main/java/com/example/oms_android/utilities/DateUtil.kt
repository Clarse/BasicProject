package com.example.oms_android.utilities

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT_YY_MM_DD_HH_MM = "yyyy.MM.dd HH:mm"
private const val DATE_FORMAT_YEAR_MONTH_DATE = "yyyy-MM-dd"
private const val FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss"

object DateUtil {

    fun compareTime(startDate: Date?, endDate: Date?): Boolean? {
        return startDate?.before(endDate)
    }

    fun formatDateTime(date: Date?): String {
        val df = SimpleDateFormat(FORMAT_DATETIME)
        return df.format(date)
    }

    fun formatDate(date: Date?): String {
        val df = SimpleDateFormat(DATE_FORMAT_YEAR_MONTH_DATE)
        return df.format(date)
    }

    //"yyyy-MM-dd'T'HH:mm:ss.SSS"
    val dataTime: String
        get() {
            val df = SimpleDateFormat(DATE_FORMAT_YY_MM_DD_HH_MM)
            return df.format(Date())
        }

    val data: String
        get() {
            val df = SimpleDateFormat(DATE_FORMAT_YEAR_MONTH_DATE)
            return df.format(Date())
        }

    //获取当前时间格式为“yyyy-MM-dd HH:mm:ss”
    val audioTime: String
        get() {
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return df.format(Date())
        }

    fun getNowTime(l: Long): String {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
        return df.format(Date(l))
    }

    fun getStrToDate(str: String?): Date? {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date: Date? = null
        try {
            date = sdf.parse(str)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    fun beginEndtime(firstTime: Date): String {
        return getTime(Date(), firstTime)
    }

    //获取时间差方法    
    fun getTime(currentTime: Date, firstTime: Date): String {
        //计算时间差
        val diff = currentTime.time - firstTime.time //这样得到的差值是微秒级别
        var result = ""
        try {
            //计算天数
            val days = diff / (1000 * 60 * 60 * 24)
            //计算小时
            val hours = diff % (1000 * 60 * 60 * 24) / (1000 * 60 * 60)
            //计算分钟
            val minutes = diff % (1000 * 60 * 60) / (1000 * 60)
            //计算秒
            val seconds = diff % (1000 * 60) / 1000
            result = if (days == 0L) {
                hours.toString() + "时" + minutes + "分" + seconds + "秒"
            } else if (days == 0L && hours == 0L) {
                minutes.toString() + "分" + seconds + "秒"
            } else {
                days.toString() + "天" + hours + "时" + minutes + "分" + seconds + "秒"
            }
        } catch (e: Exception) {
        }
        return result
    }

    //七天前
    val weekTime: String
        get() {
            val dateFormat = SimpleDateFormat(DATE_FORMAT_YEAR_MONTH_DATE)
            val calendar = Calendar.getInstance()
            calendar[Calendar.SECOND] = calendar[Calendar.SECOND] - 604800
            return dateFormat.format(calendar.time)
        }

}