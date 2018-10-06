package com.pengyang.musicplayer.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pengyang.musicplayer.R;
import com.pengyang.musicplayer.util.Config;
import com.pengyang.musicplayer.util.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FoundFragment extends Fragment implements View.OnClickListener {

    private String TAG = getClass().getSimpleName();

    private View view;
    private Activity activity;

    private TextView tvNewMusic1;
    private TextView tvNewMusic2;
    private TextView tvNewMusic3;
    private TextView tvHotMusic1;
    private TextView tvHotMusic2;
    private TextView tvHotMusic3;
    private TextView tvOldMusic1;
    private TextView tvOldMusic2;
    private TextView tvOldMusic3;

    private ArrayList<String> newMusicList;
    private ArrayList<String> oldMusicList;
    private ArrayList<String> hotMusicList;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String returnStr = "";
            if (msg != null) {
                returnStr = msg.obj.toString();
                Log.i(TAG, "returnStr:" + returnStr);
            }
            switch (msg.what) {
                case Config.CODE_TOPMUSIC:
                    try {
                        JSONObject object = new JSONObject(returnStr);
                        JSONObject object1 = object.getJSONObject("data");
                        JSONArray jsonArray = object1.getJSONArray("topList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object2 = (JSONObject) jsonArray.get(i);
                            int id = object2.getInt("id");
                            if (id == 26) {
                                int listenCount = object2.getInt("listenCount");
                                String topTitle = object2.getString("topTitle");
                                String picUrl = object2.getString("picUrl");
                                JSONArray jsonArray1 = object2.getJSONArray("songList");
                                tvHotMusic1.setText(((JSONObject) jsonArray1.get(0)).getString("singername") + " - " + ((JSONObject) jsonArray1.get(0)).getString("songname"));
                                tvHotMusic2.setText(((JSONObject) jsonArray1.get(1)).getString("singername") + " - " + ((JSONObject) jsonArray1.get(1)).getString("songname"));
                                tvHotMusic3.setText(((JSONObject) jsonArray1.get(2)).getString("singername") + " - " + ((JSONObject) jsonArray1.get(2)).getString("songname"));
                            } else if (id == 27) {
                                int listenCount = object2.getInt("listenCount");
                                String topTitle = object2.getString("topTitle");
                                String picUrl = object2.getString("picUrl");
                                JSONArray jsonArray1 = object2.getJSONArray("songList");
                                tvNewMusic1.setText(((JSONObject) jsonArray1.get(0)).getString("singername") + " - " + ((JSONObject) jsonArray1.get(0)).getString("songname"));
                                tvNewMusic2.setText(((JSONObject) jsonArray1.get(1)).getString("singername") + " - " + ((JSONObject) jsonArray1.get(1)).getString("songname"));
                                tvNewMusic3.setText(((JSONObject) jsonArray1.get(2)).getString("singername") + " - " + ((JSONObject) jsonArray1.get(2)).getString("songname"));
                            } else if (id == 36) {
                                int listenCount = object2.getInt("listenCount");
                                String topTitle = object2.getString("topTitle");
                                String picUrl = object2.getString("picUrl");
                                JSONArray jsonArray1 = object2.getJSONArray("songList");
                                tvOldMusic1.setText(((JSONObject) jsonArray1.get(0)).getString("singername") + " - " + ((JSONObject) jsonArray1.get(0)).getString("songname"));
                                tvOldMusic2.setText(((JSONObject) jsonArray1.get(1)).getString("singername") + " - " + ((JSONObject) jsonArray1.get(1)).getString("songname"));
                                tvOldMusic3.setText(((JSONObject) jsonArray1.get(2)).getString("singername") + " - " + ((JSONObject) jsonArray1.get(2)).getString("songname"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.foundfragment, container, false);
            initview();
            initdata();
        } else {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }

    private void initdata() {
        newMusicList = new ArrayList<>();
        hotMusicList = new ArrayList<>();
        oldMusicList = new ArrayList<>();
        HttpUtils.doGetAsyn(Config.MUSIC_URL, mHandler, Config.CODE_TOPMUSIC);
    }


    private void initview() {
        activity = getActivity();
        view.findViewById(R.id.iv_listen).setOnClickListener(this);
        view.findViewById(R.id.iv_music).setOnClickListener(this);
        view.findViewById(R.id.ll_search).setOnClickListener(this);
        view.findViewById(R.id.ll_old_music).setOnClickListener(this);
        view.findViewById(R.id.ll_new_music).setOnClickListener(this);
        view.findViewById(R.id.ll_hot_music).setOnClickListener(this);

        tvHotMusic1 = view.findViewById(R.id.tv_hot_music1);
        tvHotMusic2 = view.findViewById(R.id.tv_hot_music2);
        tvHotMusic3 = view.findViewById(R.id.tv_hot_music3);
        tvNewMusic1 = view.findViewById(R.id.tv_new_music1);
        tvNewMusic2 = view.findViewById(R.id.tv_new_music2);
        tvNewMusic3 = view.findViewById(R.id.tv_new_music3);
        tvOldMusic1 = view.findViewById(R.id.tv_old_music1);
        tvOldMusic2 = view.findViewById(R.id.tv_old_music2);
        tvOldMusic3 = view.findViewById(R.id.tv_old_music3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_listen:
                break;
            case R.id.iv_music:
                Intent intentMusic = new Intent(activity, PlayActivity.class);
                startActivity(intentMusic);
                break;
            case R.id.ll_hot_music:
                Intent intentHotTopList = new Intent(activity, TopActivity.class);
                intentHotTopList.putExtra("url", "https://c.y.qq.com/v8/fcg-bin/fcg_v8_toplist_cp.fcg?g_tk=5381&uin=0&format=json&inCharset=utf-8&outCharset=utf-8&notice=0&platform=h5&needNewCode=1&tpl=3&page=detail&type=top&topid=" + 26);
                intentHotTopList.putExtra("listName", "热歌榜");
                startActivity(intentHotTopList);
                break;
            case R.id.ll_new_music:
                Intent intentNewTopList = new Intent(activity, TopActivity.class);
                intentNewTopList.putExtra("url", "https://c.y.qq.com/v8/fcg-bin/fcg_v8_toplist_cp.fcg?g_tk=5381&uin=0&format=json&inCharset=utf-8&outCharset=utf-8&notice=0&platform=h5&needNewCode=1&tpl=3&page=detail&type=top&topid=" + 27);
                intentNewTopList.putExtra("listName", "新歌榜");
                startActivity(intentNewTopList);
                break;
            case R.id.ll_old_music:
                Intent intentOldTopList = new Intent(activity, TopActivity.class);
                intentOldTopList.putExtra("url", "https://c.y.qq.com/v8/fcg-bin/fcg_v8_toplist_cp.fcg?g_tk=5381&uin=0&format=json&inCharset=utf-8&outCharset=utf-8&notice=0&platform=h5&needNewCode=1&tpl=3&page=detail&type=top&topid=" + 36);
                intentOldTopList.putExtra("listName", "金曲榜");
                startActivity(intentOldTopList);
                break;
            default:
                break;
        }
    }
}
