package com.gfq.usefuldemocollections;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.litepal.crud.LitePalSupport;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DataBean extends LitePalSupport {
    private String date ;//日期
    private String time ;//打卡时间
    private boolean isShangBanDaka = false;
    private boolean isXiaBanDaka = false;

    public boolean isShangBanDaka() {
        return isShangBanDaka;
    }

    public void setShangBanDaka(boolean shangBanDaka) {
        isShangBanDaka = shangBanDaka;
    }

    public boolean isXiaBanDaka() {
        return isXiaBanDaka;
    }

    public void setXiaBanDaka(boolean xiaBanDaka) {
        isXiaBanDaka = xiaBanDaka;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
