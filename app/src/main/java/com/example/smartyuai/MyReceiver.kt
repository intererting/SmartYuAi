package com.example.smartyuai

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Process
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

/**
 * @author    yiliyang
 * @date      2023/5/14 下午3:30
 * @version   1.0
 * @since     1.0
 */
class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        println("BroadcastReceiver onReceive")
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var exists = false
        for (process in activityManager.runningAppProcesses) {
            if (process.processName.contains(":yuliyang")) {
                exists = true
                break
            }
        }
        if (!exists) {
            println("消息进程不存在")
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
            val notification =
                NotificationCompat.Builder(context, channelId)
                    .setContentTitle("title")
                    .setContentText("text ${System.currentTimeMillis()}")
                    .setContentInfo("info")
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build()
            notificationManager.notify(1000, notification)
            println("发送通知完成")
        } else {
            println("消息进程存在")
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, MyReceiver::class.java).apply {
            action = "com.yly.remove"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1000,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 10000,
            pendingIntent
        )
    }
}