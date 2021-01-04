package com.gfq.usefuldemocollections;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        MyService.start( new Intent());

//adb shell pm grant 包名 android.permission.WRITE_SECURE_SETTINGS
//adb shell pm grant com.gfq.usefuldemocollections android.permission.REAL_GET_TASKS

        Settings.Secure.putString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, "com.gfq.usefuldemocollections");
        Settings.Secure.putString(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, "1");//1表示开启

        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.gfq.usefuldemocollections","MyAccessibilityService"));
        startService(intent);

        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));

//        findViewById(R.id.bbb).setOnClickListener(v -> openDD());

    }

    private void openDD() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(new ComponentName("com.alibaba.android.rimet","com.alibaba.android.rimet.biz.LaunchHomeActivity"));
//        intent.setComponent(new ComponentName("com.gfq.main","com.gfq.main.MainAppActivity"));
        startActivity(intent);
    }

}
