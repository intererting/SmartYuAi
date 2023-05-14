package com.example.smartyuai

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * @author    yiliyang
 * @date      2023/5/14 下午9:18
 * @version   1.0
 * @since     1.0
 */
class SecondActivity : AppCompatActivity(R.layout.activity_second) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<Button>(R.id.bind).setOnClickListener {
            bindService(
                Intent(applicationContext, SecondService::class.java),
                object : ServiceConnection {
                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        println("onServiceConnected")
                    }

                    override fun onServiceDisconnected(name: ComponentName?) {
                        println("onServiceDisconnected")
                    }

                }, Service.BIND_AUTO_CREATE
            )
        }
    }
}