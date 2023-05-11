package com.example.smartyuai

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast

/**
 * @author    yiliyang
 * @date      2023/4/16 下午8:57
 * @version   1.0
 * @since     1.0
 */
class MyService : Service() {

    private var destroy = true

    override fun onCreate() {
        destroy = false
        Handler(Looper.getMainLooper()).postDelayed({
            Thread {
                while (true) {
                    println("loop ${System.identityHashCode(this)}")
                    Thread.sleep(5000)
                }
            }.start()
        }, 1000)

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
        super.onDestroy()
        println("service onDestroy ${System.identityHashCode(this)}")
    }
}