package com.example.oms_android.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.widget.RemoteViews
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.example.oms_android.R
import com.example.oms_android.base.BaseActivity
import com.example.oms_android.base.BaseViewModel
import com.example.oms_android.databinding.ActivityNotificationBinding
import com.example.oms_android.main.MainActivity
import com.example.oms_android.utilities.LogUtils
import com.example.oms_android.utilities.clicks

/**
 * @author: Clarse
 * @date: 2022/7/15
 */
class NotificationActivity : BaseActivity<ActivityNotificationBinding, BaseViewModel>() {

    companion object {
        private val TAG = NotificationActivity::class.java.simpleName
    }

    //渠道id
    private val channelId = "test"

    //渠道名称
    private val channelName = "测试通知"

    //渠道重要等级
    private val importance = NotificationManagerCompat.IMPORTANCE_DEFAULT

    //通知管理者
    private lateinit var notificationManager: NotificationManager

    //通知
    private lateinit var notification: Notification

    //通知id
    private val notificationId = 1

    //回复通知id
    private val replyNotificationId = 2

    //回复通知
    private lateinit var replyNotification: Notification

    //横幅通知id
    private val bannerNotificationId = 3

    //横幅通知
    private lateinit var bannerNotification: Notification

    //常驻通知id
    private val permanentNotificationId = 4

    //常驻通知
    private lateinit var permanentNotification: Notification

    //自定义通知id
    private val customNotificationId = 5

    //自定义通知
    private lateinit var customNotification: Notification

    //开启横幅通知返回
    private val bannerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                LogUtils.d(TAG, "返回结果")
            }
        }

    override fun initView() {
        //获取系统通知服务
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        initNotification()

        initReplyNotification()

        initBannerNotification()

        showPermanentNotification()

        initCustomNotification()

        vb.showNotification clicks {
            notificationManager.notify(notificationId, notification)
        }
        vb.showReplyNotification clicks {
            notificationManager.notify(replyNotificationId, replyNotification)
        }
        vb.showBannerNotification clicks {
            if (openBannerNotification()) {
                notificationManager.notify(bannerNotificationId, bannerNotification)
            }
        }
        vb.showCustomNotification clicks {
            notificationManager.notify(customNotificationId, customNotification)
        }
    }

    override fun initViewStates() {

    }

    override fun initViewEvents() {

    }

    //创建通知渠道
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) =
        notificationManager.createNotificationChannel(
            NotificationChannel(
                channelId,
                channelName,
                importance
            )
        )

    //构建普通通知
    private fun initNotification() {
        val title = "打工人"
        val content = "我有两个梦想，一个是有很多钱，另一个也是有很多钱"

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("title", title)
            putExtra("content", content)
        }

        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        notification = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            createNotificationChannel(channelId, channelName, importance)
            NotificationCompat.Builder(this, channelId)
        } else {
            NotificationCompat.Builder(this)
        }.apply {
            setSmallIcon(R.drawable.ic_android_black_24dp)
            setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_android_black_24dp))
            setContentTitle(title)//标题
            setContentText(content)//内容
            setContentIntent(pendingIntent)//
            setAutoCancel(true)//设置自动取消
//            setStyle(NotificationCompat.BigTextStyle().bigText(content))
            setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(BitmapFactory.decodeResource(resources, R.mipmap.test))
            )
        }.build()
    }

    //构建回复通知
    private fun initReplyNotification() {
        //远程输入
        val remoteInput = RemoteInput.Builder("key_text_reply").setLabel("快速回复").build()
        //构建回复pendingIntent
        val replyIntent = Intent(this, ReplyMessageReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                0,
                replyIntent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        //点击通知的发送按钮
        val action =
            NotificationCompat.Action.Builder(0, "回复", pendingIntent).addRemoteInput(remoteInput)
                .build()

        //构建通知
        replyNotification = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            createNotificationChannel("reply", "回复消息", importance)
            NotificationCompat.Builder(this, "reply")
        } else {
            NotificationCompat.Builder(this)
        }.apply {
            setSmallIcon(R.drawable.ic_android_black_24dp)
            setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_android_black_24dp))
            setContentTitle("10086")
            setContentText("你的账号已欠费2万元！")
            addAction(action)
        }.build()
    }

    //检查通知的重要级别判断是否开启横幅通知
    private fun openBannerNotification() = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
        val bannerImportance = notificationManager.getNotificationChannel("banner").importance
        if (bannerImportance == NotificationManager.IMPORTANCE_DEFAULT) {
            val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                .putExtra(Settings.EXTRA_CHANNEL_ID, "banner")
            bannerLauncher.launch(intent);false
        } else true
    } else true

    //构建横幅通知渠道
    private fun createBannerNotificationChannel(
        channelId: String,
        channelName: String,
        importance: Int
    ) = notificationManager.createNotificationChannel(
        NotificationChannel(
            channelId,
            channelName,
            importance
        ).apply {
            description = "提醒式通知"//渠道描述
            enableLights(true) //开启闪光灯
            lightColor = Color.BLUE//闪光灯颜色
            enableVibration(true)//开启震动
            vibrationPattern = longArrayOf(0, 1000, 500, 1000)//震动模式
            setSound(null, null)//没有提示音
        })

    //构建横幅通知
    private fun initBannerNotification() {
        //构建通知
        bannerNotification = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            createBannerNotificationChannel("banner", "提醒消息", importance)
            NotificationCompat.Builder(this, "banner")
        } else {
            NotificationCompat.Builder(this)
        }.apply {
            setSmallIcon(R.drawable.ic_android_black_24dp)//设置小图标
            setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_android_black_24dp
                )
            )//设置大图标
            setContentText("落魄Android在线炒粉")//设置标题
            setContentText("不要9块9，不要6块9，只要3块9。")//设置内容
            setWhen(System.currentTimeMillis())//通知显示时间
            setAutoCancel(true)//设置自动取消
        }.build()
    }

    //展示常驻通知
    private fun showPermanentNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        permanentNotification = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            createNotificationChannel("permanent", "我一直存在", importance)
            NotificationCompat.Builder(this, "permanent")
        } else {
            NotificationCompat.Builder(this)
        }.apply {
            setSmallIcon(R.drawable.ic_android_black_24dp)
            setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_android_black_24dp))
            setContentTitle("你在努力搞些什么")
            setContentText("搞钱！搞钱！还是搞钱！")
            setWhen(System.currentTimeMillis())
            setContentIntent(pendingIntent)
        }.build()
        permanentNotification.flags = Notification.FLAG_ONGOING_EVENT
        notificationManager.notify(permanentNotificationId, permanentNotification)
    }

    //构建自定义通知
    private fun initCustomNotification() {
        //RemoteView
        val remoteViews = RemoteViews(packageName, R.layout.layout_custom_notification)
        val bigRemoteView = RemoteViews(packageName, R.layout.layout_custom_notification_big)
        customNotification = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            createNotificationChannel("custom", "自定义通知", importance)
            NotificationCompat.Builder(this, "custom")
        } else {
            NotificationCompat.Builder(this)
        }.apply {
            setSmallIcon(R.drawable.ic_android_black_24dp)
            setCustomContentView(remoteViews)
            setCustomBigContentView(bigRemoteView)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setOnlyAlertOnce(true)
            setOngoing(true)
        }.build()
    }
}