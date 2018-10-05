package com.pengyang.musicplayer.UI;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pengyang.musicplayer.I.IPlayMusic;
import com.pengyang.musicplayer.R;
import com.pengyang.musicplayer.service.PlayMusicService;
import com.pengyang.musicplayer.util.Config;
import com.pengyang.musicplayer.util.Utils;

public class PlayActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = getClass().getSimpleName();

    private static SeekBar sbProgress;
    private static TextView tvCurrent;
    private static TextView tvTotal;
    private static TextView tvSongName;
    private static TextView tvSinger;
    private CheckBox cbPlayorPause;

    private IPlayMusic IPM;
    private MusicConnection mc;
    private Intent intent;

    private static int currentMusicPosition;

    private boolean isPlay;

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.CHANGE:
                    currentMusicPosition = msg.arg1;
                    tvSongName.setText(Utils.getMusicList().get(currentMusicPosition).getTitle());
                    tvSinger.setText(Utils.getMusicList().get(currentMusicPosition).getSinger());
                    break;
                case Config.PROGRESS:
                    Bundle bundle = msg.getData();
                    int currentPosition = bundle.getInt("currentPosition");
                    int duration = bundle.getInt("duration");
                    sbProgress.setMax(duration);
                    sbProgress.setProgress(currentPosition);

                    //歌曲当前时长
                    int minute = currentPosition / 1000 / 60;
                    int second = currentPosition / 1000 % 60;
                    String minuteStr;
                    String secondStr;
                    if (minute < 10) {
                        minuteStr = "0" + minute;
                    } else {
                        minuteStr = minute + "";
                    }
                    if (second < 10) {
                        secondStr = "0" + second;
                    } else {
                        secondStr = second + "";
                    }
                    tvCurrent.setText(minuteStr + ":" + secondStr);

                    //歌曲总时长
                    minute = duration / 1000 / 60;
                    second = duration / 1000 % 60;
                    if (minute < 10) {
                        minuteStr = "0" + minute;
                    } else {
                        minuteStr = minute + "";
                    }
                    if (second < 10) {
                        secondStr = "0" + second;
                    } else {
                        secondStr = second + "";
                    }
                    tvTotal.setText(minuteStr + ":" + secondStr);
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initview();
        connectService();
    }

    private void connectService() {
        intent = new Intent(this, PlayMusicService.class);
        startService(intent);

        mc = new MusicConnection();
        bindService(intent, mc, BIND_AUTO_CREATE);
        isPlay = getIntent().getBooleanExtra("play", false);
        Log.i(TAG, "绑定播放服务");
    }

    private void initview() {
        Log.i(TAG, "开始初始化组件");
        currentMusicPosition = getIntent().getIntExtra("music", 0);
        sbProgress = findViewById(R.id.sb_progress);
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                IPM.seekTo(sbProgress.getProgress());
            }
        });
        tvCurrent = findViewById(R.id.tv_current);
        tvTotal = findViewById(R.id.tv_total);
        tvSongName = findViewById(R.id.tv_song);
        tvSinger = findViewById(R.id.tv_singer);
        findViewById(R.id.iv_cycle).setOnClickListener(this);
        findViewById(R.id.iv_previous).setOnClickListener(this);
        cbPlayorPause = findViewById(R.id.cb_play);
        if (!PlayMusicService.isPlaying && !isPlay) cbPlayorPause.setChecked(false);
        cbPlayorPause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "isChecked:" + isChecked);
                if (isChecked) {
                    IPM.continuePlay();
                } else {
                    Log.i(TAG, "isPlaying:" + PlayMusicService.isPlaying + "");
                    if (PlayMusicService.isPlaying) {
                        IPM.pausePlay();
                        Log.i(TAG, "pausePlay");
                    } else {
                        IPM.setCurrentPosition(currentMusicPosition);
                        IPM.setMusicList(Utils.getMusicList());
                        IPM.play();
                        Log.i(TAG, "play");
                    }
                }
            }
        });
        findViewById(R.id.iv_next).setOnClickListener(this);
        findViewById(R.id.iv_list).setOnClickListener(this);
        findViewById(R.id.iv_collect).setOnClickListener(this);
        findViewById(R.id.iv_downland).setOnClickListener(this);
        findViewById(R.id.iv_share).setOnClickListener(this);
        findViewById(R.id.iv_more).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);

        tvSongName.setText(Utils.getMusicList().get(currentMusicPosition).getTitle());
        tvSinger.setText(Utils.getMusicList().get(currentMusicPosition).getSinger());
        Log.i(TAG, "组件初始化完成");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_cycle:
                break;
            case R.id.iv_previous:
                IPM.previous();
                break;
            case R.id.iv_next:
                IPM.next();
                break;
            case R.id.iv_list:
                break;
            case R.id.iv_collect:
                break;
            case R.id.iv_downland:
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_more:
                break;
            default:
                break;
        }
    }

    class MusicConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //获取中间人对象
            IPM = (IPlayMusic) service;
            if (isPlay) {
                IPM.setCurrentPosition(currentMusicPosition);
                IPM.setMusicList(Utils.getMusicList());
                IPM.play();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    /**
     * 退出播放音乐按钮
     */
    public void exit() {
        unbindService(mc);
        stopService(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mc);
    }
}
