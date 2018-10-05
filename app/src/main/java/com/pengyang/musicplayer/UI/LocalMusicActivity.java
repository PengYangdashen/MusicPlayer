package com.pengyang.musicplayer.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pengyang.musicplayer.R;
import com.pengyang.musicplayer.adapter.LocalMusicAdapter;
import com.pengyang.musicplayer.pojo.Music;
import com.pengyang.musicplayer.service.PlayMusicService;
import com.pengyang.musicplayer.util.AudioUtil;
import com.pengyang.musicplayer.util.Utils;

import java.util.ArrayList;

public class LocalMusicActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = getClass().getSimpleName();

    private ListView lvLocalMusic;
    private LocalMusicAdapter adapter;
    private ArrayList<Music> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localmusic);
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 申请权限 第二个参数是一个 数组 说明可以同时申请多个权限
                this.requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {//已授权
                mList = AudioUtil.getAllMusics(this);
                Utils.setMusicList(mList);
                Log.i(TAG, "本地音乐大小1：" + mList.size());
            }
        } else {
            mList = AudioUtil.getAllMusics(getApplicationContext());
            Utils.setMusicList(mList);
            Log.i(TAG, "本地音乐大小2：" + mList.size());
        }
        initview();
    }


    private void initview() {
        lvLocalMusic = findViewById(R.id.lv_musiclist);
        lvLocalMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LocalMusicActivity.this, PlayActivity.class);
                intent.putExtra("music", position);
                if (PlayMusicService.isPlaying) {
                    intent.putExtra("play", false);
                } else {
                    intent.putExtra("play", true);
                }
                startActivity(intent);
                Log.i(TAG, "跳转到播放控制页");
            }
        });
        adapter = new LocalMusicAdapter(this, mList);
        lvLocalMusic.setAdapter(adapter);
        Log.i(TAG, "本地音乐列表刷新");

        findViewById(R.id.iv_music).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_music:
                Intent intent = new Intent(LocalMusicActivity.this, PlayActivity.class);
                startActivity(intent);
            default:
                break;
        }
    }
}
