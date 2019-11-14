package com.yc.mmrecover.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PlayVoiceTask extends AsyncTask<String, String, String> {
    private static final String mDomain = "http://www.fulmz.com";
    private MediaPlayer mMediaPlayer;
    private static String TAG = "sssss_log_PlayVoiceTask";


    public class ObjResponseVo<T> {
        private String msg;
        private T obj;
        private String success;

        public ObjResponseVo(String str, String str2, T t) {
            this.success = str;
            this.msg = str2;
            this.obj = t;
        }

        public T getObj() {
            return this.obj;
        }
    }


    protected String doInBackground(String... strArr) {
        String amr2mp3 = amr2mp3(strArr[0]);
        Log.d(TAG, "doInBackground: end amr2mp3 " + amr2mp3);
        return amr2mp3;
    }

    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onPostExecute result = ");
        stringBuilder.append(str);
        Log.d(TAG, "onPostExecute: stringBuilder.toString() " + stringBuilder.toString());
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split("./");
            String url = split[split.length - 1];
            String netUrl = mDomain.concat("/upload/amr/").concat(url);
            Log.d(TAG, "onPostExecute: netUrl " + netUrl);
            playNetVoice(netUrl);
        }

    }

    private void playNetVoice(String path) {
        if (mMediaPlayer != null) {
            return;
        }
        stopPlay();
        if (TextUtils.isEmpty(path)) {
            new NullPointerException("音乐path为空");
            return;
        }
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); //设置音频流的类型。
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            }); // 准备好
            mMediaPlayer.setOnErrorListener(new OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int event, int extra) {
                    String content = getErrorMessage(event);
                    Log.d(TAG, "onError:  mp " + mediaPlayer + " extra " + extra + " event " + event + " content " + content);
                    return false;
                }
            });
            mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlay();
                }
            }); //播放完成事件监听器
            Class<MediaPlayer> clazz = MediaPlayer.class;
            Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
            Log.e(TAG, "playMusic: startPlay-->: ID: ,PATH:" + path);
            method.invoke(mMediaPlayer, path, null);

            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String amr2mp3(String str) {
        String replace = str.replace(".amr", ".mp3");
        if (new File(replace).exists()) {
            return replace;
        }
        File file = new File(str);
        if (file.exists()) {
            str = str.substring(str.lastIndexOf("/") + 1);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("str = ");
            stringBuilder2.append(str);
            Log.d(TAG, "onPostExecute: stringBuilder.toString() 333" + stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("f^#$2");
            stringBuilder2.append(str);
            stringBuilder2.append("32(&f");
            String md5 = Func.md5(stringBuilder2.toString());
            Map<String, String> hashMap = new HashMap();
            hashMap.put("_model", "Amr");
            hashMap.put("_key", md5);
            OkHttpClient build = new Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(1, TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).build();
            MultipartBody.Builder addFormDataPart = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file", str, RequestBody.create(MediaType.parse("application/octet-stream"), file));
            for (Entry entry : hashMap.entrySet()) {
                addFormDataPart.addFormDataPart((String) entry.getKey(), (String) entry.getValue());
            }
            try {
                Response execute = build.newCall(new Request.Builder().url("http://www.fulmz.com/v1/uploadFile").post(addFormDataPart.build()).build()).execute();
                if (execute.isSuccessful()) {
                    str = execute.body().string();
                    hashMap = (Map) JSON.parseObject(str, Map.class);
                    String str2 = (String) hashMap.get("success");
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("msg = ");
                    stringBuilder4.append(hashMap.get("msg"));
                    Log.d(TAG, "onPostExecute: stringBuilder.toString() 88" + stringBuilder4.toString());
                    if (str2.equals("1")) {
                        ObjResponseVo objResponseVo = (ObjResponseVo) JSON.parseObject(str, ObjResponseVo.class);
                        byte[] bArr = new byte[1024];
                        StringBuilder stringBuilder5 = new StringBuilder();
                        stringBuilder5.append(mDomain);
                        stringBuilder5.append(objResponseVo.getObj().toString());
                        execute = build.newCall(new Request.Builder().url(stringBuilder5.toString()).get().build()).execute();
                        File file2 = new File(replace);
                        if (execute.body() == null) {
                            return null;
                        }
                        InputStream byteStream = execute.body().byteStream();
                        if (!file2.getParentFile().exists()) {
                            file2.getParentFile().mkdirs();
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(file2);
                        while (true) {
                            int read = byteStream.read(bArr);
                            if (read == -1) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, read);
                        }
                        fileOutputStream.flush();
                        byteStream.close();
                        fileOutputStream.close();
                        Log.d(TAG, "amr2mp3: write file end");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return replace;
    }


    public void stopPlay() {
        if (this.mMediaPlayer != null) {
            try {
                if (this.mMediaPlayer.isPlaying()) {
                    this.mMediaPlayer.stop();
                    this.mMediaPlayer.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mMediaPlayer.reset();
        }
    }


    private String getErrorMessage(int event) {
        String content = "播放失败，未知错误";
        switch (event) {
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                content = "播放失败，未知错误";
                break;
            //收到次错误APP必须重新实例化新的MediaPlayer
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                content = "播放器内部错误";
                break;
            //流开始位置错误
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                content = "媒体流错误";
                break;
            //IO,超时错误
            case MediaPlayer.MEDIA_ERROR_IO:
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                content = "网络连接超时";
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                content = "请求播放失败：403";
                break;
            case -2147483648:
                content = "系统错误";
                break;
        }
        return content;
    }
}