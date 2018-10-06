package com.pengyang.musicplayer.UI;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.pengyang.musicplayer.R;
import com.pengyang.musicplayer.adapter.LocalMusicAdapter;
import com.pengyang.musicplayer.pojo.Music;
import com.pengyang.musicplayer.util.Config;
import com.pengyang.musicplayer.util.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class TopActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = getClass().getSimpleName();
    private ListView lvListMusic;
    private ArrayList<Music> musicTopList;
    private LocalMusicAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toplist);
        initview();
    }

    private void initview() {
        MyHandler mHandler = new MyHandler(this);
        musicTopList = new ArrayList<>();
        TextView tvListName = findViewById(R.id.tv_top_name);
        tvListName.setText(getIntent().getStringExtra("listName"));
        lvListMusic = findViewById(R.id.lv_toplist);

        findViewById(R.id.back).setOnClickListener(this);

        HttpUtils.doGetAsyn(getIntent().getStringExtra("url"), mHandler, Config.CODE_MUSICLIST);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    private class MyHandler extends Handler {

        private WeakReference<TopActivity> softActivity;

        public MyHandler(TopActivity activity) {
            softActivity = new WeakReference<TopActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.CODE_MUSICLIST:
                    String returnStr = msg.obj.toString();
                    try {
                        JSONObject object = new JSONObject(returnStr);
                        JSONArray jsonArray = object.getJSONArray("songlist");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Music music = new Music();
                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            JSONObject object1 = obj.getJSONObject("data");
                            String album = object1.getString("albumname");
                            music.setAlbum(album);

                            JSONArray singers = object1.getJSONArray("singer");
                            String singer = ((JSONObject) singers.getJSONObject(0)).getString("name");
                            music.setSinger(singer);
                            int singerId = ((JSONObject) singers.getJSONObject(0)).getInt("id");
                            music.setSingerid(singerId);

                            int songId = object1.getInt("songid");
                            music.setSongid(songId);
                            String songname = object1.getString("songname");
                            music.setTitle(songname);
                            musicTopList.add(music);
                            Log.i(TAG, "歌曲：" + music.toString());
                        }
                        Log.i(TAG, "巅峰榜单大小：" + musicTopList.size());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (musicTopList != null && musicTopList.size() > 0) {
                            adapter = new LocalMusicAdapter(softActivity.get(), musicTopList);
                            lvListMusic.setAdapter(adapter);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
