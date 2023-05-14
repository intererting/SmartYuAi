package com.example.smartyuai

import android.Manifest
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.opengl.Matrix
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Process
import android.os.SystemClock
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlin.math.max
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val connection by lazy {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                println("onServiceConnected")
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                println("onServiceDisconnected")
            }

        }
    }

    /*   override fun onSaveInstanceState(outState: Bundle) {
           println("onSaveInstanceState")
           outState.putString("name", "yuliyang")
           super.onSaveInstanceState(outState)
       }

       override fun onRestoreInstanceState(savedInstanceState: Bundle) {
           println("onRestoreInstanceState")
           println(savedInstanceState.getString("name"))
           super.onRestoreInstanceState(savedInstanceState)
       }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        //闹钟
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(applicationContext, MyService::class.java)
//        val pendingIntent = PendingIntent.getService(
//            applicationContext,
//            1000,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
        val intent = Intent(applicationContext, MyReceiver::class.java).apply {
            action = "com.yly.remove"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            1000,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 10000,
            pendingIntent
        )


//        println("activity onCreate ${savedInstanceState == null}")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        findViewById<Button>(R.id.stopService).setOnClickListener {
            stopService(Intent(this, MyService::class.java))
        }

        findViewById<Button>(R.id.sendNotification).setOnClickListener {
            sendBroadcast(Intent("com.example.close").apply {
            })
        }

        findViewById<Button>(R.id.kill).setOnClickListener {
            stopService(Intent(this, MyService::class.java))
//            unbindService(connection)
//            stopService(Intent(this@MainActivity, MyService::class.java))
            val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (process in activityManager.runningAppProcesses) {
                if (process.processName.contains(":yuliyang")) {
                    Process.killProcess(process.pid)
                }
            }

        }

        findViewById<TextView>(R.id.test).setOnClickListener {
            stopService(Intent(this, MyService::class.java))
            val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (process in activityManager.runningAppProcesses) {
                if (process.processName.contains(":yuliyang")) {
                    Process.killProcess(process.pid)
                }
            }
            startService(Intent(this@MainActivity, MyService::class.java))
            bindService(
                Intent(this@MainActivity, MyService::class.java),
                connection, Service.BIND_IMPORTANT
            )
//            val matrix = android.graphics.Matrix()
//            matrix.postTranslate(10f, 20f)
//            matrix.postScale(2.0f, 3.0f)
//            matrix.postRotate(30f)
//            println(matrix)
//            val channelId = "100"
//            val channelName = "default channel"
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val channel = NotificationChannel(
//                    channelId,
//                    channelName,
//                    NotificationManager.IMPORTANCE_DEFAULT
//                )
//                notificationManager.createNotificationChannel(channel)
//            }
//
//            val num = Random(System.currentTimeMillis()).nextInt(0, 1000)
//            println("num $num")
//            val intent = Intent(this, MainActivity::class.java)
//            val contentIntent = PendingIntent.getActivity(
//                this@MainActivity, 100, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//            val notification = NotificationCompat.Builder(this, channelId)
//                .setContentInfo("hello")
//                .setAutoCancel(true)
//                .setContentIntent(contentIntent)
//                .setContentText("title")
//                .setWhen(System.currentTimeMillis())
//                .setNumber(num)
//                .setSmallIcon(application.applicationInfo.icon)
//                .build()
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                registerForActivityResult(
//                    ActivityResultContracts.RequestPermission(),
//                    object : ActivityResultCallback<Boolean> {
//                        override fun onActivityResult(result: Boolean) {
//                            println(result)
//                        }
//                    }).launch(Manifest.permission.POST_NOTIFICATIONS)
//            }
//            notificationManager.notify(num, notification)
        }


    }

    override fun onResume() {
        super.onResume()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    override fun onDestroy() {
        println("onDestroy")
//        stopService(Intent(this, MyService::class.java))
        super.onDestroy()
    }


}