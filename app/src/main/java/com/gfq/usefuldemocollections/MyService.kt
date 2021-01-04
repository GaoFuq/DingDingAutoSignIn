package com.gfq.usefuldemocollections

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.makeMainActivity
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.JobIntentService
import com.gfq.usefuldemocollections.kt.toast
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class MyService : JobIntentService() {

    companion object {
        @JvmStatic
        fun start(intent: Intent) {
            enqueueWork(App.context, MyService::class.java, 123, intent)
        }


    }


    override fun onHandleWork(intent: Intent) {
        intent.action = Intent.ACTION_MAIN
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        intent.component = ComponentName(
            "com.alibaba.android.rimet",
            "com.alibaba.android.rimet.biz.LaunchHomeActivity"
        )

        val activityManager = App.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            @SuppressLint("NewApi")
            override fun run() {
//                startActivity(intent)
                val name = activityManager.getRunningTasks(1)[0].topActivity
                val localDateTime = LocalTime.now()

                Log.e("onHandleWork", "$localDateTime ${name?.className}");

            }
        }, 0, 3000)
    }
}