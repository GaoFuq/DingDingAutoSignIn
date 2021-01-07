package com.gfq.usefuldemocollections

import android.accessibilityservice.AccessibilityService
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.litepal.LitePal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class MyAcceService : AccessibilityService() {
    private var count = 0
    private var floatView: FloatView? = null
    private val SHANG_BAN = "上班"
    private val XIA_BAN = "下班"
    private val DEFALUT = "打卡时间范围之外"
    private var workTime = DEFALUT
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.e("xx", "onServiceConnected")
        floatView = FloatView(this)

        floatView?.show()

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val data = LitePal.findLast(DataBean::class.java)
                if (data == null) {
                    createAndSaveBean()//第一次打开没有数据
                } else {
                    val now = LocalDate.now()
                    if (now.toString() != data.date) {
                        createAndSaveBean()//创建新的一天
                        floatView?.setText("新的一天")
                    } else {
                        //test
                        count++
//                        data.shangBanDaka = true
                        //处理逻辑
                        mainLogic(data)


                    }
                }
            }

            private fun mainLogic(data: DataBean) {
                floatView?.setText("当前日期：${data.date}")
                val isShangBanDaka = data.shangBanDaka
                val isXiaBanDaka = data.xiaBanDaka
                if (isShangBanDaka && isXiaBanDaka) {
                    floatView?.setText(" ${LocalDate.now()} \n ${LocalTime.now()} \n 都已打卡")
                    return
                }

                if (!isShangBanDaka) {
                    //上班打卡
                    if (checkShangBanTime()) {
                        closeAndRestartDingDing()
                    }
                }

                if (isShangBanDaka && !isXiaBanDaka) {//上班已打卡，下班未打卡
                    //下班打卡
                    if (checkXiaBanTime()) {
                        closeAndRestartDingDing()
                    }
                }
            }

            private fun checkXiaBanTime(): Boolean {
                val time = LocalTime.now()
//                val time = LocalTime.now().plusHours(2)
                var desc = "准备下班打卡"
                var timeEnough = false
                workTime = DEFALUT
                if (time.hour == 17 && time.minute > 30 || time.hour in 18..23) {
                    desc = "在下班打卡时间范围内"
                    workTime = XIA_BAN
                    timeEnough = true
                }
                floatView?.setText(" $time \n repeat: $count \n $desc")
                return timeEnough
            }

            private fun checkShangBanTime(): Boolean {
                val time = LocalTime.now().plusHours(-8)
                var desc = "准备上班打卡"
                var timeEnough = false
                workTime = DEFALUT
                if (time.hour == 8 && time.minute > 10 || time.hour in 9..10) {
                    desc = "在上班打卡时间范围内"
                    workTime = SHANG_BAN
                    timeEnough = true
                }
                floatView?.setText(" $time \n repeat: $count \n $desc")
                return timeEnough
            }


            private fun createAndSaveBean() {
                val dataBean = DataBean()
                dataBean.date = LocalDate.now().toString()
                dataBean.save()
            }
        }, 1000, 1000 * 10)
    }

    //跳转到打卡界面
    private fun jump2Daka(eventType: Int, nodeInfo: AccessibilityNodeInfo) {
        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val list = nodeInfo.findAccessibilityNodeInfosByViewId("com.alibaba.android.rimet:id/home_bottom_tab_button_work") ?: return
            for (item in list) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                return
            }
        }

        if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            val listW = nodeInfo.findAccessibilityNodeInfosByViewId("com.alibaba.android.rimet:id/common_webview") ?: return
            if (listW.size > 0) {
                val webViewNodeInfo = listW[0]?.getChild(0)?.getChild(0)
                val dingapp = webViewNodeInfo?.getChild(0) //dingapp
                val daka = dingapp?.getChild(3)?.getChild(0)?.getChild(2) //考勤打卡按钮
                daka?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                return
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val nodeInfo = rootInActiveWindow ?: return
        if (event == null) return

        val eventType = event.eventType
        try {
            sleep()
            if (workTime == DEFALUT) {
                //do nothing
            } else {
                jump2Daka(eventType, nodeInfo)
                //打卡签到界面
                val webFrame = nodeInfo.findAccessibilityNodeInfosByViewId("com.alibaba.android.rimet:id/webview_frame") ?: return
                val backLayout = nodeInfo.findAccessibilityNodeInfosByViewId("com.alibaba.android.rimet:id/back_layout") ?: return
                if (webFrame.size == 0) return
                if (backLayout.size == 0) return
                when (workTime) {
                    SHANG_BAN -> shangBanDaka(webFrame[0], backLayout[0])
                    XIA_BAN -> xiaBanDaka(webFrame[0], backLayout[0])
                }
            }
        } catch (e: Exception) {
            Log.e("xx ", "ERROR ${e.message}")
        }
        nodeInfo.recycle()
    }

    private fun xiaBanDaka(nodeInfo: AccessibilityNodeInfo, backLayout: AccessibilityNodeInfo) {
        val child = getDakaArea(nodeInfo)
        //        val shangBanButton = child?.getChild(0)?.getChild(0)?.getChild(2)//上班打卡按钮
        val xiaBanButton = child?.getChild(1)?.getChild(0)?.getChild(2)//下班打卡按钮
        val clicked = handleClick(xiaBanButton, backLayout)
        val dataBean = LitePal.findLast(DataBean::class.java)
        dataBean.xiaBanDaka = clicked
        dataBean.saveOrUpdate()
    }

    private fun shangBanDaka(nodeInfo: AccessibilityNodeInfo, backLayout: AccessibilityNodeInfo) {
        val child = getDakaArea(nodeInfo)
        val shangBanButton = child?.getChild(0)?.getChild(0)?.getChild(2)//上班打卡按钮
//        val xiaBanButton = child?.getChild(1)?.getChild(0)?.getChild(2)//下班打卡按钮
        val clicked = handleClick(shangBanButton, backLayout)
        val dataBean = LitePal.findLast(DataBean::class.java)
        dataBean.shangBanDaka = clicked
        dataBean.saveOrUpdate()
    }

    private fun getDakaArea(nodeInfo: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        val webViewNodeInfo = nodeInfo.getChild(0)?.getChild(0)?.getChild(0)
        val child0 = webViewNodeInfo?.getChild(1)
        val child = child0?.getChild(1)
        return child
    }


    private fun handleClick(button: AccessibilityNodeInfo?, backLayout: AccessibilityNodeInfo): Boolean {
        val childCount = button?.childCount
        var description = ""
        val b = when (childCount) {
            2 -> {//在考勤范围内
                description = button.getChild(0)?.contentDescription.toString()//正常打卡文本
                button.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                true
            }
            3 -> { //外勤
                description = button.getChild(1)?.contentDescription.toString()//外勤打卡文本
                false
            }
            else -> {
                backLayout.performAction(AccessibilityNodeInfo.ACTION_CLICK) //返回上一界面
                false
            }
        }
        Log.e("xx", "description $description")
        floatView?.setText(description)

        return b
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
    }

    private fun closeAndRestartDingDing() {
        openWeiXin()
        sleep()
        killDingDing()
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