package com.pengyang.musicplayer.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {

    public static OkHttpClient mHttpClient = new OkHttpClient();

    public static void okHttpGetString(String url, final Handler handler, final int code) {
        Request request = new Request.Builder().url(url).build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("HttpUtils -> get失败：", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("HttpUtils -> get成功：", response.body().string());
                Message msg = Message.obtain();
                msg.obj = response.body().string();
                msg.what = code;
                handler.sendMessage(msg);
            }

        });
    }

    public static void okHttpPostString(String url, FormBody.Builder builder, final Handler handler, final int code) {
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("HttpUtils -> post失败：", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = Message.obtain();
                msg.obj = response.body().string();
                msg.what = code;
                handler.sendMessage(msg);
                Log.i("HttpUtils -> post成功：", response.body().string());
            }

        });
    }

    /**
     * 异步的Get请求
     *
     * @param urlStr
     * @param callBack
     */
    static String resultStr = "";
    private static final int TIMEOUT_IN_MILLIONS = 10000;

    public static String doGetAsyn(final String urlStr, final Handler handler,
                                   final int type) {

        new Thread() {
            public void run() {
                try {
                    resultStr = doGet(urlStr, handler, type);

                } catch (Exception e) {
                    Log.e("HttpUtils", e.getMessage());
                }
            }
        }.start();
        return resultStr;
    }

    public static String doGet(final String urlStr, Handler handler, int type) {
        Message msg = Message.obtain();
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            Log.i("HttpUtils", "HttpUtil---url--" + type);
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];
                Log.i("HttpUtils", "--------");
                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                Log.i("HttpUtils", "HttpUtil---doGet:" + baos.toString());
                resultStr = baos.toString();
                msg.what = type;
            } else {
                msg.what = Config.CODE_ERROR;
                Log.e("HttpUtils", "网络状态码不为200！");
            }

        } catch (MalformedURLException e) {
            // url错误的异常
            msg.what = Config.CODE_URL_ERROR;
            Log.e("HttpUtils", e.getMessage());
        } catch (SocketTimeoutException e) {
            msg.what = Config.CODE_TIMEOUT_ERROR;
            Log.e("HttpUtils", "SocketTimeoutException -> 网络请求超时");
            Log.e("HttpUtils", e.getMessage());
        } catch (IOException e) {
            // 网络错误异常
            msg.what = Config.CODE_NET_ERROR;
            Log.e("HttpUtils", "IOException -> 提示网络传输错误");
            Log.e("HttpUtils", e.getMessage());
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                Log.e("HttpUtils", e.getMessage());
            }
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                Log.e("HttpUtils", e.getMessage());
            }
            conn.disconnect();
        }
        msg.obj = resultStr;
        handler.sendMessage(msg);
        return resultStr;
    }
}
