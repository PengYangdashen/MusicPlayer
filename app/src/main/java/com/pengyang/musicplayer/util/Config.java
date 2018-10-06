package com.pengyang.musicplayer.util;

public class Config {

    //	type = 1-新歌榜,2-热歌榜,11-摇滚榜,12-爵士,16-流行,21-欧美金曲榜,22-经典老歌榜,23-情歌对唱榜,24-影视金曲榜,25-网络歌曲榜
    //  size:   返回歌曲数量
    //  offset: 获取偏移
    // "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billList&format=json&type=1&size=10&offset=0";
    public static final String MUSIC_URL = "https://c.y.qq.com/v8/fcg-bin/fcg_myqq_toplist.fcg?g_tk=5381&uin=0&format=json&inCharset=utf-8&outCharset=utf-8&notice=0&platform=h5&needNewCode=1";
    public static final String MUSICLIST_URL = "https://c.y.qq.com/v8/fcg-bin/fcg_myqq_toplist.fcg?g_tk=5381&uin=0&format=json&inCharset=utf-8&outCharset=utf-8&notice=0&platform=h5&needNewCode=1";

    public static final int NEXT = 1001;
    public static final int PROGRESS = 1002;
    public static final int CHANGE = 1003;

    public static final int CODE_NEWMUSIC = 2001;
    public static final int CODE_OLDMUSIC = 2002;
    public static final int CODE_HOTMUSIC = 2003;
    public static final int CODE_TOPMUSIC = 2004;
    public static final int CODE_MUSICLIST = 2005;

    public static final int CODE_URL_ERROR = 1;
    public static final int CODE_NET_ERROR = 2;
    public static final int CODE_TIMEOUT_ERROR = 5;
    public static final int CODE_ERROR = 6;
}
