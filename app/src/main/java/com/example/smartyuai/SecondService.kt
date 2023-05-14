package com.example.smartyuai

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * @author    yiliyang
 * @date      2023/5/14 下午9:19
 * @version   1.0
 * @since     1.0
 */
class SecondService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        println("second service onCreate")
        super.onCreate()
    }

    override fun onDestroy() {
        println("second service onDestroy")
        super.onDestroy()
    }
}