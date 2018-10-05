package com.pengyang.musicplayer.util;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.pengyang.musicplayer.pojo.Music;

import java.util.ArrayList;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 申请权限 第二个参数是一个 数组 说明可以同时申请多个权限
            } else {//已授权
                Utils.setMusicList(AudioUtil.getAllMusics(this));
            }
        } else {
            Utils.setMusicList(AudioUtil.getAllMusics(this));
        }
    }

}

