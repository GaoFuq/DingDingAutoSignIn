package com.gfq.usefuldemocollections;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.litepal.crud.LitePalSupport;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DataBean extends LitePalSupport {
    private String date ;
    private boolean isShangBanDaka = false;
    private boolean isXiaBanDaka = false;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getShangBanDaka() {
        return isShangBanDaka;
    }

    public void setShangBanDaka(Boolean shangBanDaka) {
        isShangBanDaka = shangBanDaka;
    }

    public Boolean getXiaBanDaka() {
        return isXiaBanDaka;
    }

    public void setXiaBanDaka(Boolean xiaBanDaka) {
        isXiaBanDaka = xiaBanDaka;
    }
}
