package com.pengyang.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pengyang.musicplayer.R;
import com.pengyang.musicplayer.pojo.Music;

import java.util.ArrayList;

public class LocalMusicAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Music> mList;

    public LocalMusicAdapter (Context context, ArrayList<Music> list) {
        mInflater = LayoutInflater.from(context);
        if (list == null) {
            mList = new ArrayList<Music>();
        } else {
            mList = list;
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.music_item, null);
            holder.tvMusicName = convertView.findViewById(R.id.tv_music_name);
            holder.tvMusicInfo = convertView.findViewById(R.id.tv_music_info);
            holder.ivInfo = convertView.findViewById(R.id.iv_info);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvMusicName.setText(mList.get(position).getTitle());
        holder.tvMusicInfo.setText(mList.get(position).getSize() + " " + mList.get(position).getSinger() + "-" + mList.get(position).getAlbum());
        holder.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView tvMusicName;
        private TextView tvMusicInfo;
        private ImageView ivInfo;
    }
}
