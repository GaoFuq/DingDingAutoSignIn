package com.gfq.usefuldemocollections

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Path
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class MyAcceService : AccessibilityService() {
    private var isDaka: Boolean = false // 是否已经打卡
    var rect = Rect()
    var count = 0 // 每失败5次，就重启钉钉
    var canStartService = true // 钉钉已打开，就设为false
    private var floatView: FloatView? = null
    var workTime = "上班" // 班次
    override fun onServiceConnected() {
        super.onServiceConnected()
        Toast.makeText(App.context, "自动打卡服务已开启", Toast.LENGTH_SHORT).show()
        rect = Rect()
        floatView = FloatView(this)

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (canStartService && checkTime()) {
                    closeAndRestartDingDing()
                }
                if (isDaka) {
                    checkTime()
                }
            }
        }, 1000, 5000)

        floatView?.show()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (!checkTime()) {
            return
        }


        val nodeInfo = rootInActiveWindow

        try {
            val eventType = event!!.eventType
            Log.e("xx ", "eventType == $eventType")


            Thread.sleep(1000)


            if (count > 6) {
                closeAndRestartDingDing()
                count = 0
                return
            }


            if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                val list = nodeInfo?.findAccessibilityNodeInfosByViewId("com.alibaba.android.rimet:id/home_bottom_tab_button_work") ?: return
                for (item in list) {
                    item.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    Log.e("xx", "工作台 click")
                    return
                }
            }

            if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                val listW = nodeInfo?.findAccessibilityNodeInfosByViewId("com.alibaba.android.rimet:id/common_webview") ?: return
                if (listW.size > 0) {
                    val webViewNodeInfo = listW[0]?.getChild(0)?.getChild(0)
                    val dingapp = webViewNodeInfo?.getChild(0) //dingapp
                    val daka = dingapp?.getChild(3)?.getChild(0)?.getChild(2) //考勤打卡按钮
                    daka?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    return
                }

                //打卡签到界面
                val list = nodeInfo.findAccessibilityNodeInfosByViewId("com.alibaba.android.rimet:id/webview_frame") ?: return
                val backLayout = nodeInfo.findAccessibilityNodeInfosByViewId("com.alibaba.android.rimet:id/back_layout") ?: return
                if (list.size > 0) {
                    val webViewNodeInfo = list[0]?.getChild(0)?.getChild(0)?.getChild(0)
                    val child = webViewNodeInfo?.getChild(1)?.getChild(2)
                    val shangBanButton = child?.getChild(0)?.getChild(0)?.getChild(2)//上班打卡按钮
                    val xiaBanButton = child?.getChild(1)?.getChild(0)?.getChild(2)//下班打卡按钮


                    when (workTime) {
                        "上班" -> handleClick(shangBanButton, backLayout)
                        "下班" -> handleClick(xiaBanButton, backLayout)
                    }
                }
                count++
            }

        } catch (e: Exception) {
            e.printStackTrace()
            count++
        } finally {
            nodeInfo?.recycle()
            Log.e("xx", "count == $count")
        }

    }

    private fun handleClick(button: AccessibilityNodeInfo?, backLayout: MutableList<AccessibilityNodeInfo>) {
        button.runCatching {
            isDaka = true
            return
        }
        val childCount = button?.childCount
        var description = ""
        if (childCount == 2) {//在考勤范围内
            description = button.getChild(0)?.contentDescription.toString()//正常打卡文本
        } else if (childCount == 3) { //外勤
            description = button.getChild(1)?.contentDescription.toString()//外勤打卡文本
        }
        Log.e("xx", "description $description")


        when (description) {
            "上班打卡" -> {
                button?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                floatView?.setText("上班打卡成功")
            }
            "下班打卡" -> {
                button?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                floatView?.setText("下班打卡成功")
            }
            else -> if (backLayout.size > 0) {
                Thread.sleep(3000)
                backLayout[0].performAction(AccessibilityNodeInfo.ACTION_CLICK) //返回上一界面
                floatView?.setText("未到上/下班时间 或 外勤")
            }
        }
    }

    private fun checkTime(): Boolean {
        val time = LocalTime.now()
        var timeEnough = false
        var desc = "不在打卡时间范围"
        if (time.hour == 17 && time.minute > 30) {
            timeEnough = true
            workTime = "下班"
            desc = "在下班打卡时间范围内"
        }
        if (time.hour in 18..20) {
            timeEnough = true
            workTime = "下班"
            desc = "在下班打卡时间范围内"
        }
        if (time.hour in 8..11) {
            desc = "在上班打卡时间范围内"
            workTime = "上班"
            timeEnough = true
        }
        if (time.hour in 0..7 || time.hour in 12..16 || time.hour in 20..23) {
            isDaka = false
        }
        if (isDaka) {
            desc = "$workTime 已打卡"
        }
        floatView?.setText(" 现在时间：$time \n $desc")
        return timeEnough
    }

    private fun performClick(xiaBanButton: AccessibilityNodeInfo?) {
        xiaBanButton?.getBoundsInScreen(rect)
        val path = Path()
        path.moveTo(rect.centerX().toFloat(), rect.centerY().toFloat())
        dispatchGesture(GestureDescription.Builder().addStroke(StrokeDescription(path, 100, 50)).build(), null, null)
    }

    override fun onInterrupt() {
        Toast.makeText(App.context, "onInterrupt", Toast.LENGTH_SHORT).show()

    }


    private fun openDingDing() {
        val intent = Intent()
        intent.component = ComponentName(
            "com.alibaba.android.rimet",
            "com.alibaba.android.rimet.biz.LaunchHomeActivity"
        )
        intent.action = Intent.ACTION_MAIN
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        Log.e("xx", "打开钉钉")
        canStartService = false

    }

    private fun closeAndRestartDingDing() {
        openWeiXin()
        sleep()
//        killDingDing()
        sleep()
        openDingDing()
    }

    private fun killDingDing() {
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        am.killBackgroundProcesses("com.alibaba.android.rimet")
    }

    private fun sleep() {
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun openWeiXin() {
        val intent = Intent()
        intent.component = ComponentName(
            "com.tencent.mm",
            "com.tencent.mm.ui.LauncherUI"
        )
        intent.action = Intent.ACTION_MAIN
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

        Log.e("xx", "打开微信")
    }

}