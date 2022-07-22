package com.example.oms_android.notification

import android.app.NotificationManager
import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.oms_android.R
import com.example.oms_android.utilities.LogUtils
import java.util.*
import kotlin.concurrent.schedule

/**
 * @author: Clarse
 * @date: 2022/7/15
 */
class ReplyMessageReceiver : BroadcastReceiver() {

    companion object {
        private val TAG = ReplyMessageReceiver::class.java.simpleName
    }

    override fun onReceive(context: Context, intent: Intent) {

        //获取消息回复的内容
        val inputContent =
            RemoteInput.getResultsFromIntent(intent)?.getCharSequence("key_text_reply")?.toString()
        LogUtils.d(TAG, "onReceive:$inputContent")

        if (null == inputContent) {
            LogUtils.d(TAG, "onReceive:没有回复消息！")
            return
        }

        //构建回复消息通知
        val repliedNotification = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, "reply")
        } else {
            NotificationCompat.Builder(context)
        }.apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle("10086")
            setContentText("消息发送成功")
        }.build()

        val notificationManager =
            context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, repliedNotification)
        Timer().schedule(1000) {
            notificationManager.cancel(2)
        }
    }

}