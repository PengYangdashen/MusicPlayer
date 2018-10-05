package com.pengyang.musicplayer.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pengyang.musicplayer.R;
import com.pengyang.musicplayer.util.Utils;

public class MusicFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.musicfragment, container, false);
            initview();
        } else {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }

    private void initview() {
        activity = getActivity();
        view.findViewById(R.id.ll_local).setOnClickListener(this);
        TextView tvLocalCount = view.findViewById(R.id.tv_local_count);
        tvLocalCount.setText(Utils.getMusicList().size() + "");
        TextView tvRecentCount = view.findViewById(R.id.tv_recent_count);
        tvRecentCount.setText(Utils.getRecentMusicList().size() + "");
        view.findViewById(R.id.iv_music).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_local:
                Intent intent = new Intent(activity, LocalMusicActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_music:
                Intent intentMusic = new Intent(activity, PlayActivity.class);
                startActivity(intentMusic);
                break;
            default:
                break;
        }
    }
}
