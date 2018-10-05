package com.pengyang.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pengyang.musicplayer.I.IPlayMusic;
import com.pengyang.musicplayer.UI.PlayActivity;
import com.pengyang.musicplayer.pojo.Music;
import com.pengyang.musicplayer.util.Config;
import com.pengyang.musicplayer.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class PlayMusicService extends Service {

    private String TAG = getClass().getSimpleName();

    private MediaPlayer player;
    private Timer timer;
    private ArrayList<Music> musicList;
    private Music currentMusic;
    private static int musicPosition;

    public static boolean isPlaying = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicPlayerControl();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (player == null) {
            if (musicList == null) musicList = Utils.getMusicList();
            player = new MediaPlayer();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.i(TAG, "播放完成：" + currentMusic.getTitle());
                    next();
                }
            });

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    addTimer();
                    Log.i(TAG, "准备完成，开始播放：" + currentMusic.getTitle());
                    Utils.setRecentMusic(currentMusic);
                    isPlaying = true;
                }
            });
        }
        Log.i(TAG, "创建播放服务");
    }

    public void play() {
        try {
            if (player == null) {
                player = new MediaPlayer();
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Log.i(TAG, "播放完成：" + currentMusic.getTitle());
                        next();
                    }
                });

                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        addTimer();
                        Log.i(TAG, "准备完成，开始播放：" + currentMusic.getTitle());
                        Utils.setRecentMusic(currentMusic);
                        isPlaying = true;
                    }
                });
            }
            player.reset();
            currentMusic = musicList.get(musicPosition);
            Log.i(TAG, "Play：" + currentMusic.getTitle());
            player.setDataSource(currentMusic.getFileUrl());
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void continuePlay() {
        if (!isPlaying) {
            player.start();
            isPlaying = true;
            Log.i(TAG, "continuePlay：" + currentMusic.getTitle());
        }
    }

    public void pausePlay() {
        if (isPlaying) {
            player.pause();
            isPlaying = false;
            Log.i(TAG, "pausePlay：" + currentMusic.getTitle());
        }
    }

    public void seekTo(int progress) {
        player.seekTo(progress);
    }

    public void next() {
        Log.i(TAG, "next：" + currentMusic.getTitle());
        musicPosition++;
        if (musicPosition >= musicList.size()) {
            musicPosition = 0;
        }
        player.stop();
        play();
        Message msg = PlayActivity.handler.obtainMessage();
        msg.what = Config.CHANGE;
        msg.arg1 = musicPosition;
        PlayActivity.handler.sendMessage(msg);
    }

    public void previous() {
        Log.i(TAG, "previous：" + currentMusic.getTitle());
        musicPosition--;
        if (musicPosition < 0) {
            musicPosition = musicList.size() - 1;
        }
        player.stop();
        play();
        Message msg = PlayActivity.handler.obtainMessage();
        msg.what = Config.CHANGE;
        msg.arg1 = musicPosition;
        PlayActivity.handler.sendMessage(msg);
    }

    private void addTimer() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int duration = player.getDuration();
                    int currentPosition = player.getCurrentPosition();
                    Message msg = PlayActivity.handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration", duration);
                    bundle.putInt("currentPosition", currentPosition);
                    msg.setData(bundle);
                    msg.what = Config.PROGRESS;
                    PlayActivity.handler.sendMessage(msg);
                }
            }, 5, 500);
        }
    }

    class MusicPlayerControl extends Binder implements IPlayMusic {

        @Override
        public void play() {
            PlayMusicService.this.play();
        }

        @Override
        public void pausePlay() {
            PlayMusicService.this.pausePlay();
        }

        @Override
        public void next() {
            PlayMusicService.this.next();
        }

        @Override
        public void previous() {
            PlayMusicService.this.previous();
        }

        @Override
        public void seekTo(int progress) {
            PlayMusicService.this.seekTo(progress);
        }

        @Override
        public void continuePlay() {
            PlayMusicService.this.continuePlay();
        }

        @Override
        public void setMusicList(ArrayList<Music> musiclist) {
            musicList = musiclist;
        }

        @Override
        public void setCurrentPosition(int positon) {
            musicPosition = positon;
        }

        @Override
        public void setHandler(Handler handler) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
        player = null;
        timer.cancel();
        timer = null;
    }
}
