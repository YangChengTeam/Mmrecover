package com.yc.mmrecover.utils;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import net.lingala.zip4j.util.InternalZipConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
    private static boolean mIsPlaying;
    private PlayCallback mCallback;
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

    public interface PlayCallback {
        void onStart(int i);

        void onStop();
    }

    public PlayVoiceTask(PlayCallback playCallback) {
        this.mCallback = playCallback;
    }

    protected String doInBackground(String... strArr) {
        String amr2mp3 = amr2mp3(strArr[0]);
        Log.d(TAG, "doInBackground: end amr2mp3 "+amr2mp3);
        return amr2mp3;
    }

    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onPostExecute result = ");
        stringBuilder.append(str);
        Log.d(TAG, "onPostExecute: stringBuilder.toString() " + stringBuilder.toString());
        playVoice(str);
    }

    public static String amr2mp3(String str) {
        Log.d(TAG, "amr2mp3: 666666 str "+str);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("amr2mp3 str = ");
        stringBuilder.append(str);
        Log.d(TAG, "onPostExecute: stringBuilder.toString() 222" + stringBuilder.toString());
        String replace = str.replace(".amr", ".mp3");
//        if (new File(replace).exists()) {
//            Log.d(TAG, "amr2mp3:  file exists replace "+replace);
//            return replace;
//        }
        File file = new File(str);
        if (file.exists()) {
            str = str.substring(str.lastIndexOf(InternalZipConstants.ZIP_FILE_SEPARATOR) + 1);
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
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("result = ");
                    stringBuilder3.append(str);
                    Log.d(TAG, "onPostExecute: stringBuilder.toString() 66" + stringBuilder3.toString());
                    Gson gson = new Gson();
                    hashMap = (Map) gson.fromJson(str, Map.class);
                    String str2 = (String) hashMap.get("success");
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("msg = ");
                    stringBuilder4.append(hashMap.get("msg"));
                    Log.d(TAG, "onPostExecute: stringBuilder.toString() 88" + stringBuilder4.toString());
                    if (str2.equals("1")) {
                        ObjResponseVo objResponseVo = (ObjResponseVo) gson.fromJson(str, ObjResponseVo.class);
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
        Log.d(TAG, "amr2mp3: 666666 replace "+replace);
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
                    this.mMediaPlayer.pause();
                    this.mMediaPlayer.seekTo(0);
//                    this.mMediaPlayer.stop();
                    this.mMediaPlayer.release();
                }
                this.mMediaPlayer.reset();
                this.mMediaPlayer.setDataSource(str);

                if (this.mCallback != null) {
                    this.mCallback.onStart(this.mMediaPlayer.getDuration());
                }
                Log.d(TAG, "playVoice: mMediaPlayer.getDuration() "+mMediaPlayer.getDuration());
                this.mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Log.d(TAG, "onCompletion: onCompletion= 播放结束 ");
                        PlayVoiceTask.mIsPlaying = false;
                        if (PlayVoiceTask.this.mCallback != null) {
                            PlayVoiceTask.this.mCallback.onStop();
                        }
                    }
                });
                this.mMediaPlayer.setOnErrorListener(new OnErrorListener() {
                    public boolean onError(MediaPlayer mediaPlayer, int event, int extra) {
                        String content = getErrorMessage(event);
                        Log.d(TAG, "onError: onError= 播放出错 ");
                        Log.d(TAG, "onError:  mp " + mediaPlayer + " extra " + extra + " event " + event + " content " + content);
                        PlayVoiceTask.mIsPlaying = false;
                        if (PlayVoiceTask.this.mCallback != null) {
                            PlayVoiceTask.this.mCallback.onStop();
                        }
                        return false;
                    }
                });
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        PlayVoiceTask.this.mMediaPlayer.start();
                    }
                });
                this.mMediaPlayer.prepareAsync();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("getVoiceSecond getDuration=");
                stringBuilder.append(this.mMediaPlayer.getDuration());
                Log.d(TAG, "playVoice: 96  stringBuilder.toString() " + stringBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
                if (this.mMediaPlayer.isPlaying()) {
                    this.mMediaPlayer.stop();
                    this.mMediaPlayer.release();
                }
                if (this.mCallback != null) {
                    this.mCallback.onStop();
                }
                Log.d(TAG, "playVoice: 播放异常  e " + e.toString());
                mIsPlaying = false;
            }
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