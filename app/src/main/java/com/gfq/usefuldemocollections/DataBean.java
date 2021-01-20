package com.gfq.usefuldemocollections;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.litepal.crud.LitePalSupport;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DataBean extends LitePalSupport {
    private String date ;//日期
    private String sbTime ;//打卡时间
    private String xbTime ;//打卡时间
    private boolean isShangBanDaka = false;
    private boolean isXiaBanDaka = false;

    public String getSbTime() {
        return sbTime;
    }

    public void setSbTime(String sbTime) {
        this.sbTime = sbTime;
    }

    public String getXbTime() {
        return xbTime;
    }

    public void setXbTime(String xbTime) {
        this.xbTime = xbTime;
    }

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


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
