package com.gfq.usefuldemocollections;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

@SuppressLint("NewApi")
public class MyAccessibilityService extends AccessibilityService {

    Rect rect;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.e("xx", "onServiceConnected");
        rect = new Rect();

        openDemo();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        closeDingDing();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        openDingDing();

    }

    private void openDingDing() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.alibaba.android.rimet",
                "com.alibaba.android.rimet.biz.LaunchHomeActivity"));
        intent.setAction(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void closeDingDing() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses("com.alibaba.android.rimet");
    }

    private void openDemo() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.gfq.main",
                "com.gfq.main.MainAppActivity"));
        intent.setAction(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        Log.e("xx ", "eventType == " + eventType);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();

        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (nodeInfo != null) {
                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.alibaba.android.rimet:id/home_bottom_tab_button_work");
                for (AccessibilityNodeInfo item : list) {
                    item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.e("xx", "工作台 click");
                }
            }
        }

        if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            List<AccessibilityNodeInfo> listW = nodeInfo.findAccessibilityNodeInfosByViewId("com.alibaba.android.rimet:id/common_webview");
            Log.e("xx", "listW size " + listW.size());

            if (listW.size() > 0) {
                AccessibilityNodeInfo $0 = listW.get(0);
                if ($0 == null) return;
                AccessibilityNodeInfo $00 = $0.getChild(0);
                if ($00 == null) return;
                AccessibilityNodeInfo webViewNodeInfo = $00.getChild(0);
                if (webViewNodeInfo == null) return;

                CharSequence className = webViewNodeInfo.getClassName();
                Log.e("xx", "webView className " + className);

                AccessibilityNodeInfo dingapp = webViewNodeInfo.getChild(0);//dingapp
                AccessibilityNodeInfo daka = dingapp.getChild(3).getChild(0).getChild(2);//考勤打卡
                daka.getBoundsInScreen(rect);

                CharSequence description = daka.getChild(1).getContentDescription();
                Log.e("xx", "description " + description);
                Log.e("xx", rect.toString());

                Path path = new Path();
                path.moveTo(rect.centerX(), rect.centerY());
                dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription(path, 100, 50)).build(), null, null);

            }


            nodeInfo.recycle();

        }
    }


//    private void onTouchClick(View view,int x,int y){
//        final long downTime = SystemClock.uptimeMillis();
////        View b1 = findViewById(R.id.button);
//        MotionEvent downEvent = MotionEvent.obtain(downTime,downTime, MotionEvent.ACTION_DOWN,x,y,0);
//        MotionEvent upEvent = MotionEvent.obtain(downTime,SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,x,y,0);
//        b1.dispatchTouchEvent(downEvent);
//        b1.dispatchTouchEvent(upEvent);
//        downEvent.recycle();
//        upEvent.recycle();
//    }


    @Override
    public void onInterrupt() {
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    private void handleNotification(AccessibilityEvent event) {

    }

//
//    public AccessibilityNodeInfo findNode(AccessibilityNodeInfo node) {
//        if (node == null) {
//            return null;
//        }
//
//        for (int i = 0; i < node.getChildCount(); i++) {
//            if (node.getClassName().equals("android.webkit.WebView")) {
//                Log.e("xx", "node.getClassName == " + node.getClassName());
//                return node;
//            }else {
//                findNode(node.getChild(i));
//                Log.e("xx", "for -- ");
//            }
//        }
//        return null;
//    }
}