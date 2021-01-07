package com.gfq.usefuldemocollections;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.litepal.LitePal;

import java.time.LocalDate;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static boolean mockShangWuTime = false;
    public static boolean mockXiaTime = false;
    public static boolean mockDayPlus1 = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
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


        findViewById(R.id.openAuto).setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        });

        findViewById(R.id.write).setOnClickListener(v -> {
            DataBean dataBean = new DataBean();
            dataBean.setDate(LocalDate.now().toString());
            dataBean.saveOrUpdate();
        });

        TextView tvContent = findViewById(R.id.content);

        findViewById(R.id.read).setOnClickListener(v -> {
            List<DataBean> all = LitePal.findAll(DataBean.class);
            StringBuilder builder = new StringBuilder();
            builder.append("数据条数：").append(all.size()).append("\n");
            for (int i = 0; i < all.size(); i++) {
                DataBean dataBean = all.get(i);
                String date = dataBean.getDate();
                Boolean xiaBanDaka = dataBean.getXiaBanDaka();
                Boolean shangBanDaka = dataBean.getShangBanDaka();
                Log.e("xx", date + xiaBanDaka + shangBanDaka);
                builder.append(i).append(":").append(date).append("上班：").append(shangBanDaka).append("  下班：").append(xiaBanDaka).append("\n");
            }
            tvContent.setText(builder.toString());
        });

        findViewById(R.id.clear).setOnClickListener(v -> {
            LitePal.deleteAll(DataBean.class);
        });

        findViewById(R.id.mockShangBan).setOnClickListener(v -> {
            DataBean bean = LitePal.findLast(DataBean.class);
            if (bean != null) {
                bean.setShangBanDaka(true);
                bean.saveOrUpdate();
            }
        });

        findViewById(R.id.mockXiaBan).setOnClickListener(v -> {
            DataBean bean = LitePal.findLast(DataBean.class);
            if (bean != null) {
                bean.setXiaBanDaka(true);
                bean.saveOrUpdate();
            }
        });

        findViewById(R.id.mockXiaBanTime).setOnClickListener(v -> {
            mockXiaTime = true;
        });
        findViewById(R.id.mockShangBanTime).setOnClickListener(v -> {
            mockShangWuTime = true;
        });
        findViewById(R.id.dayPlus1).setOnClickListener(v -> {
            mockDayPlus1 = true;
        });
        findViewById(R.id.mockClearTime).setOnClickListener(v -> {
            mockShangWuTime = false;
            mockXiaTime = false;
            mockDayPlus1 = false;
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


}
