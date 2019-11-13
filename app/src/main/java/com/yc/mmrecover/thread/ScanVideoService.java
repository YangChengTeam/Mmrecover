package com.yc.mmrecover.thread;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.yc.mmrecover.eventbus.VideoEventBusMessage;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.utils.Func;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FilenameFilter;

public class ScanVideoService extends IntentService {
    public static boolean isServiceRun() {
        return mIsServiceRun;
    }

    private static boolean mIsServiceRun;

    public ScanVideoService() {
        super("ScanVideoService");
    }

    public void onCreate() {
        super.onCreate();
        mIsServiceRun = true;
    }

    public void onDestroy() {
        super.onDestroy();
        mIsServiceRun = false;
    }

    public static void stopService() {
        mIsServiceRun = false;
    }

    private void scanDisk(String str) {
        if (mIsServiceRun) {
            new File(str).listFiles(new FilenameFilter() {
                public boolean accept(File file, String str) {
                    File file2 = new File(file, str);
                    String absolutePath = file2.getAbsolutePath();
                    if (file2.isDirectory()) {
                        ScanVideoService.this.scanDisk(absolutePath);
                    } else {
                        long length = file2.length();
                        if (ScanVideoService.this.PathFilter(absolutePath)) {
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            try {
                                mediaMetadataRetriever.setDataSource(absolutePath);
                                Object extractMetadata = mediaMetadataRetriever.extractMetadata(19);
                                Object extractMetadata2 = mediaMetadataRetriever.extractMetadata(18);
                                Object extractMetadata3 = mediaMetadataRetriever.extractMetadata(9);
                                int parseInt = TextUtils.isEmpty(extractMetadata + "") ? 0 : Integer.parseInt(extractMetadata + "");
                                int parseInt2 = TextUtils.isEmpty(extractMetadata2 + "") ? 0 : Integer.parseInt(extractMetadata2 + "");
                                if ((TextUtils.isEmpty(extractMetadata3 + "") ? 0 : Integer.parseInt(extractMetadata3 + "")) > 1000 && parseInt2 > 0 && parseInt > 0) {
                                    MediaInfo mediaBean = new MediaInfo();
                                    mediaBean.setLastModifyTime((int) (file2.lastModified() / 1000));
                                    mediaBean.setPath(absolutePath);
                                    mediaBean.setSize(length);
                                    mediaBean.setStrSize(Func.getSizeString(length));
                                    mediaBean.setWidth(parseInt2);
                                    mediaBean.setHeight(parseInt);
                                    mediaBean.setFileName(file2.getName());
                                    EventBus.getDefault().post(new VideoEventBusMessage(mediaBean));
                                }
                                mediaMetadataRetriever.release();
                            } catch (Exception unused) {
                                mediaMetadataRetriever.release();
                            }
                        }
                    }
                    return false;
                }
            });
        }
    }

    private boolean PathFilter(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str) || str.endsWith(".jpg") || str.endsWith(".png") || str.endsWith(".gif") || str.endsWith(".txt") || str.endsWith(".db") || str.endsWith(".apk") || str.endsWith(".ppm") || str.endsWith(".zip") || str.endsWith(".slk") || str.endsWith(".json") || str.endsWith(".so") || str.endsWith(".js") || str.endsWith(".log") || str.endsWith(".html") || str.endsWith(".amr") || str.endsWith(".xlog") || str.endsWith(".xml")) {
            return false;
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        if (options.outWidth <= 0 || options.outHeight <= 0 || options.outMimeType == null || !options.outMimeType.toLowerCase().contains("image")) {
            z = true;
        }
        return z;
    }

    protected void onHandleIntent(@Nullable Intent intent) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        stringBuilder.append("/tencent/MicroMsg");
        scanDisk(stringBuilder.toString());
        EventBus.getDefault().post(new VideoEventBusMessage());
    }
}
