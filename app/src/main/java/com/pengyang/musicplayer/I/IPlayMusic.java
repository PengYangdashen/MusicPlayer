package com.pengyang.musicplayer.I;

import com.pengyang.musicplayer.pojo.Music;

import java.util.ArrayList;
import java.util.logging.Handler;

public interface IPlayMusic {
    void play();
    void pausePlay();
    void next();
    void previous();
    void seekTo(int progress);
    void continuePlay();
    void setMusicList(ArrayList<Music> musicList);
    void setCurrentPosition (int positon);
    void setHandler (Handler handler);
}
