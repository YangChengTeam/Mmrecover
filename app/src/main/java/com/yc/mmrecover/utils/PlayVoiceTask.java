package com.yc.mmrecover.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.fulongbin.decoder.Silk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by suns  on 2019/12/6 15:06.
 */
public class PlayVoiceTask extends AsyncTask<String, String, String> {
    private static final String mDomain = "http://www.fulmz.com";
    private static boolean mIsPlaying;
    private PlayCallback mCallback;
    private MediaPlayer mMediaPlayer;

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

    public interface PlayCallback {
        void onStart(int i);

        void onStop();
    }

    public PlayVoiceTask( PlayCallback playCallback) {
        this.mCallback = playCallback;
//        this.mContex = context;
    }

//

    protected String doInBackground(String... strArr) {
        String amr2mp3 = amr2mp3(strArr[0]);
        Log.d("TAG", "PlayVoiceTask doInBackground end");
        return amr2mp3;
    }

    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onPostExecute result = ");
        stringBuilder.append(str);
        Log.d("TAG", stringBuilder.toString());
        playVoice(str);
    }

    public static String amr2mp3(String voicePath) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("amr2mp3 str = ");
        stringBuilder.append(voicePath);
//        String replace = voicePath.replace(".amr", ".mp3");
        Log.d("TAG", stringBuilder.toString());

        String fileName = voicePath.substring(voicePath.lastIndexOf("/") + 1);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/数据恢复助手/微信音频解码恢复/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        path = dir.getAbsolutePath() + "/" + fileName+".mp3";
        if (new File(path).exists() || Silk.convertSilkToMp3(voicePath, path)) {
            Log.d("TAG", "amr2mp3 file exists");
            return path;
        }

        return path;
    }

    public int getVoiceMiSecond(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        str = amr2mp3(str);
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(str);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer.getDuration();
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
        }
        mIsPlaying = false;
    }

    private void playVoice(String str) {
        if (!mIsPlaying) {
            mIsPlaying = true;
            this.mMediaPlayer = new MediaPlayer();
            try {
                if (this.mMediaPlayer.isPlaying()) {
                    this.mMediaPlayer.stop();
                    this.mMediaPlayer.release();
                }
                this.mMediaPlayer.reset();
//                this.mMediaPlayer.setDataSource(mContex, Uri.fromFile(new File(str)));
                this.mMediaPlayer.setDataSource(str);
                this.mMediaPlayer.prepare();
                if (this.mCallback != null) {
                    this.mCallback.onStart(this.mMediaPlayer.getDuration());
                }
                this.mMediaPlayer.setOnCompletionListener(mediaPlayer -> {
                    Log.d("TAG", "onCompletion= 播放结束");
                    PlayVoiceTask.mIsPlaying = false;
                    if (PlayVoiceTask.this.mCallback != null) {
                        PlayVoiceTask.this.mCallback.onStop();
                    }
                });
                this.mMediaPlayer.setOnErrorListener((mediaPlayer, i, i2) -> {
                    Log.d("TAG", "onError= 播放出错");
                    PlayVoiceTask.mIsPlaying = false;
                    if (PlayVoiceTask.this.mCallback != null) {
                        PlayVoiceTask.this.mCallback.onStop();
                    }
                    return false;
                });
                this.mMediaPlayer.start();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("getVoiceSecond getDuration=");
                stringBuilder.append(this.mMediaPlayer.getDuration());
                Log.d("TAG", stringBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
                if (this.mMediaPlayer.isPlaying()) {
                    this.mMediaPlayer.stop();
                    this.mMediaPlayer.release();
                }
                if (this.mCallback != null) {
                    this.mCallback.onStop();
                }
                Log.d("TAG", "播放异常");
                mIsPlaying = false;
            }
        }
    }
}
