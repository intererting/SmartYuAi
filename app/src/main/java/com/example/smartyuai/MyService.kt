package com.example.smartyuai

import android.app.AlarmManager
import android.app.AlarmManager.INTERVAL_FIFTEEN_MINUTES
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.delay

/**
 * @author    yiliyang
 * @date      2023/4/16 下午8:57
 * @version   1.0
 * @since     1.0
 */
class MyService : Service() {

    private var destroy = true

    override fun onCreate() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        applicationContext.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                println("onReceive")
                notificationManager.cancel(1000)
            }

        }, IntentFilter().apply {
            addAction("com.example.close")
        })

        val channelId = "100"
        val channelName = "default channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        destroy = false
        Thread {
            while (true) {
                println("loop ${System.identityHashCode(this)}")
                val notification =
                    NotificationCompat.Builder(applicationContext, channelId)
                        .setContentTitle("title")
                        .setContentText("text ${System.currentTimeMillis()}")
                        .setContentInfo("info")
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .build()
                notificationManager.notify(1000, notification)
                Thread.sleep(3000)
            }
        }.start()

//        Handler(Looper.getMainLooper()).postDelayed({
////            stopSelf()
//        }, 10000)
        println("service onCreate ${System.identityHashCode(this)}")
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        println("onBind ${System.identityHashCode(this)}")
        return MyBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("onStartCommand ${System.identityHashCode(this)}")
//        Thread.sleep(10000)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        //不能在这关闭服务,必须手动关
        println("onUnbind ${System.identityHashCode(this)}")
        return true
    }

    override fun onRebind(intent: Intent?) {
        println("onRebind ${System.identityHashCode(this)}")
        super.onRebind(intent)
    }

    class MyBinder : Binder() {

    }

    override fun onDestroy() {
        destroy = true
        println("service onDestroy ${System.identityHashCode(this)}")
        super.onDestroy()
    }
}