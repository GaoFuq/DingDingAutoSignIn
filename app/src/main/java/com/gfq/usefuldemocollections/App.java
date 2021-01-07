package com.gfq.usefuldemocollections;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;


public class App extends Application {
    @SuppressLint("StaticFieldLeak")
    @NotNull
    public static  Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        /*=================litepal数据库=====================*/
        LitePal.initialize(this);
        //获取到SQLiteDatabase的实例，创建数据库表
//        SQLiteDatabase db = LitePal.getDatabase();
//        DoraemonKit.install(this,"bb01a8f52ecdc98de903affb3aa7ba5e");
        CrashReport.initCrashReport(getApplicationContext(),"c30ed1484a",true);
    }
}
