package com.gfq.usefuldemocollections;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        MyService.start( new Intent());

//adb shell pm grant 包名 android.permission.WRITE_SECURE_SETTINGS
//adb shell pm grant com.gfq.usefuldemocollections android.permission.REAL_GET_TASKS

//        Settings.Secure.putString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, "com.gfq.usefuldemocollections");
//        Settings.Secure.putString(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, "1");//1表示开启


        findViewById(R.id.bbb).setOnClickListener(v -> {
            requestSettingCanDrawOverlays();
        });

        //无障碍的服务不需要手动开启
//        findViewById(R.id.openService).setOnClickListener(v -> {
////            Intent intent = new Intent();
////            intent.setComponent(new ComponentName("com.gfq.usefuldemocollections", "MyAcceService"));
//            startService(new Intent(this,MyAcceService.class));
//        });

        findViewById(R.id.openAuto).setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        });
    }

    //权限打开
    private void requestSettingCanDrawOverlays() {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.O) {//8.0以上
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivityForResult(intent, 1);
        } else if (sdkInt >= Build.VERSION_CODES.M) {//6.0-8.0
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1);
        } else {//4.4-6.0以下
            //无需处理了
        }
    }

    private void openDD() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(new ComponentName("com.alibaba.android.rimet", "com.alibaba.android.rimet.biz.LaunchHomeActivity"));
//        intent.setComponent(new ComponentName("com.gfq.main","com.gfq.main.MainAppActivity"));
        startActivity(intent);
    }



}
