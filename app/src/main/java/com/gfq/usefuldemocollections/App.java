package com.gfq.usefuldemocollections;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import org.jetbrains.annotations.NotNull;


public class App extends Application {
    @SuppressLint("StaticFieldLeak")
    @NotNull
    public static  Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
//        DoraemonKit.install(this,"bb01a8f52ecdc98de903affb3aa7ba5e");
    }
}
