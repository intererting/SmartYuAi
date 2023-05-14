package com.example.smartyuai

import android.app.ActivityManager
import android.app.Application
import android.app.NotificationManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.os.Process
import android.text.TextUtils
import androidx.annotation.Nullable


/**
 * @author    yiliyang
 * @date      2023/5/14 上午11:15
 * @version   1.0
 * @since     1.0
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (packageName.equals(ProcessUtil.getCurrentProcessName(this))) {
            stopService(Intent(this, MyService::class.java))
            val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (process in activityManager.runningAppProcesses) {
                if (process.processName.contains(":yuliyang")) {
                    Process.killProcess(process.pid)
                }
            }
//            sendBroadcast(Intent("com.example.close").apply {
//            })

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            for (notification in notificationManager.activeNotifications) {
                println(notification.id)
            }
            notificationManager.cancelAll()

//            bindService(Intent(this, MyService::class.java), object : ServiceConnection {
//                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//                    println("MyApplication onServiceConnected")
//                }
//
//                override fun onServiceDisconnected(name: ComponentName?) {
//                }
//
//            }, Service.BIND_IMPORTANT)
        }
    }

    object ProcessUtil {
        private var currentProcessName: String? = null

        /**
         * @return 当前进程名
         */
        fun getCurrentProcessName(context: Context): String? {
            if (!TextUtils.isEmpty(currentProcessName)) {
                return currentProcessName
            }

            //1)通过Application的API获取当前进程名
            currentProcessName = currentProcessNameByApplication
            if (!TextUtils.isEmpty(currentProcessName)) {
                return currentProcessName
            }

            //2)通过反射ActivityThread获取当前进程名
            currentProcessName = currentProcessNameByActivityThread
            if (!TextUtils.isEmpty(currentProcessName)) {
                return currentProcessName
            }

            //3)通过ActivityManager获取当前进程名
            currentProcessName = getCurrentProcessNameByActivityManager(context)
            return currentProcessName
        }

        /**
         * 通过Application新的API获取进程名，无需反射，无需IPC，效率最高。
         */
        val currentProcessNameByApplication: String?
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                getProcessName()
            } else null

        /**
         * 通过反射ActivityThread获取进程名，避免了ipc
         */
        val currentProcessNameByActivityThread: String?
            get() {
                var processName: String? = null
                try {
                    val declaredMethod = Class.forName(
                        "android.app.ActivityThread", false,
                        Application::class.java.classLoader
                    ).getDeclaredMethod("currentProcessName", *emptyArray<Class<*>>())
                    declaredMethod.isAccessible = true
                    val invoke = declaredMethod.invoke(null, *emptyArray<Class<*>>())
                    if (invoke is String) {
                        processName = invoke
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
                return processName
            }

        /**
         * 通过ActivityManager 获取进程名，需要IPC通信
         */
        private fun getCurrentProcessNameByActivityManager(context: Context): String? {
            val pid = Process.myPid()
            val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            val runningAppList = am.runningAppProcesses
            if (runningAppList != null) {
                for (processInfo in runningAppList) {
                    if (processInfo.pid == pid) {
                        return processInfo.processName
                    }
                }
            }
            return null
        }
    }

}