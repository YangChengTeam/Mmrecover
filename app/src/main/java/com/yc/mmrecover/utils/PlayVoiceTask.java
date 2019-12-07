package com.yc.mmrecover.utils;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;

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

    public PlayVoiceTask(PlayCallback playCallback) {
        this.mCallback = playCallback;
    }

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

    public static String amr2mp3(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("amr2mp3 str = ");
        stringBuilder.append(str);
        Log.d("TAG", stringBuilder.toString());
        String replace = str.replace(".amr", ".mp3");
        if (new File(replace).exists()) {
            Log.d("TAG", "amr2mp3 file exists");
            return replace;
        }
        File file = new File(str);
        if (file.exists()) {
            str = str.substring(str.lastIndexOf(File.separator) + 1);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("str = ");
            stringBuilder2.append(str);
            Log.d("TAG", stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("f^#$2");
            stringBuilder2.append(str);
            stringBuilder2.append("32(&f");
            String md5 = Func.md5(stringBuilder2.toString());
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("_model", "Amr");
            hashMap.put("_key", md5);
            OkHttpClient build = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(1, TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).build();
            MultipartBody.Builder addFormDataPart = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file", str, RequestBody.create(MediaType.parse("application/octet-stream"), file));
            for (Map.Entry entry : hashMap.entrySet()) {
                addFormDataPart.addFormDataPart((String) entry.getKey(), (String) entry.getValue());
            }
            try {
                Response execute = build.newCall(new Request.Builder().url("http://www.fulmz.com/v1/uploadFile").post(addFormDataPart.build()).build()).execute();
                if (execute.isSuccessful()) {
                    str = execute.body().string();
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("result = ");
                    stringBuilder3.append(str);
                    Log.d("TAG", stringBuilder3.toString());
//                    Gson gson = new Gson();
                    hashMap = JSON.parseObject(str, Map.class);
//                    hashMap = (Map) gson.fromJson(str, Map.class);
                    String str2 = (String) hashMap.get("success");
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("msg = ");
                    stringBuilder4.append(hashMap.get("msg"));
                    Log.d("TAG", stringBuilder4.toString());
                    if (str2.equals("1")) {
//                        ObjResponseVo objResponseVo = (ObjResponseVo) gson.fromJson(str, ObjResponseVo.class);
                        ObjResponseVo objResponseVo = JSON.parseObject(str, ObjResponseVo.class);
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
                        Log.d("TAG", "write file end");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return replace;
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
