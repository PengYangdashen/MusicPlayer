package com.pengyang.musicplayer.util;

import android.util.Log;

import com.pengyang.musicplayer.pojo.Music;

import java.util.ArrayList;

public class Utils {

    private static String TAG = "Utils";
    private static ArrayList<Music> localMusicList = new ArrayList<>();
    private static ArrayList<Music> recentMusicList = new ArrayList<>();

    public static void setMusicList(ArrayList<Music> list) {
        localMusicList = list;
        Log.i(TAG, "设置本地音乐列表，大小：" + localMusicList.size());
    }

    public static ArrayList<Music> getMusicList() {
        Log.i(TAG, "获取本地音乐列表，大小：" + localMusicList.size());
        return localMusicList;
    }
    public static void setRecentMusicList(ArrayList<Music> list) {
        recentMusicList = list;
        Log.i(TAG, "设置最近播放音乐列表，大小：" + recentMusicList.size());
    }

    public static ArrayList<Music> getRecentMusicList() {
        Log.i(TAG, "获取最近播放音乐列表，大小：" + recentMusicList.size());
        return recentMusicList;
    }
    public static void setRecentMusic(Music music) {
        if (recentMusicList.size() > 100) {
            recentMusicList.remove(0);
            Log.i(TAG, "最近歌曲超出100，移除第一个");
        }
        recentMusicList.add(music);
        Log.i(TAG, "添加歌曲到最近播放：" + music.toString());
    }
}
