package com.yc.mmrecover.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import androidx.core.content.FileProvider;

import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.utils.ToastUtil;

import java.io.File;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Func {
    public static String getSizeString(long j) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        float f;
        StringBuilder stringBuilder;
        if (j > 1073741824) {
            f = ((float) j) / 1.07374182E9f;
            stringBuilder = new StringBuilder();
            stringBuilder.append(decimalFormat.format((double) f));
            stringBuilder.append("G");
            return stringBuilder.toString();
        } else if (j > 1048576) {
            f = ((float) j) / 1048576.0f;
            stringBuilder = new StringBuilder();
            stringBuilder.append(decimalFormat.format((double) f));
            stringBuilder.append("M");
            return stringBuilder.toString();
        } else if (j > 1024) {
            f = ((float) j) / 1024.0f;
            stringBuilder = new StringBuilder();
            stringBuilder.append(decimalFormat.format((double) f));
            stringBuilder.append("K");
            return stringBuilder.toString();
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(decimalFormat.format(j));
            stringBuilder2.append("B");
            return stringBuilder2.toString();
        }
    }


    public static String formatData(String str, long j) {
        if (j == 0) {
            return "";
        }
        return new SimpleDateFormat(str, Locale.CHINA).format(new Date(j * 1000));
    }

    public static String getUsedInternalMemorySize(Context context) {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long j = 0;
        long blockCountLong = Build.VERSION.SDK_INT >= 18 ? statFs.getBlockCountLong() - statFs.getAvailableBlocksLong() : 0;
        if (Build.VERSION.SDK_INT >= 18) {
            j = statFs.getBlockSizeLong();
        }
        return Formatter.formatFileSize(context, blockCountLong * j);
    }

    public static String getInternalMemorySize(Context context) {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long j = 0;
        long blockSizeLong = Build.VERSION.SDK_INT >= 18 ? statFs.getBlockSizeLong() : 0;
        if (Build.VERSION.SDK_INT >= 18) {
            j = statFs.getBlockCountLong();
        }
        return Formatter.formatFileSize(context, j * blockSizeLong);
    }

    public static int getUsedMemoryPresent() {
        long blockCountLong;
        long blockCountLong2;
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        if (Build.VERSION.SDK_INT >= 18) {
            blockCountLong = statFs.getBlockCountLong() - statFs.getAvailableBlocksLong();
            blockCountLong2 = statFs.getBlockCountLong();
        } else {
            blockCountLong = 0;
            blockCountLong2 = 1;
        }
        return (int) ((blockCountLong * 100) / blockCountLong2);
    }
    public static String md5(String str) {
        if (str == null) {
            return "";
        }
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"));
            StringBuilder stringBuilder = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                int i = b & 255;
                if (i < 16) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(Integer.toHexString(i));
            }
            return stringBuilder.toString();
        } catch (Throwable e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }
    }


    public static void openFile(Context context, String path) {
        if (path == null)
            return;

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        String[][] MATCH_ARRAY = {
                {".doc", "application/msword"},
                {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
                {".xls", "application/vnd.ms-excel"},
                {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
                {".txt", "text/plain"},
                {".pptx", "tapplication/vnd.openxmlformats-officedocument.presentationml.presentation"},
                {".ppt", "application/vnd.ms-powerpoint"},
                {".xml", "text/plain"},
        };
        String type = "";
        for (int i = 0; i < MATCH_ARRAY.length; i++) {
            if (path.contains(MATCH_ARRAY[i][0])) {
                type = MATCH_ARRAY[i][1];
                break;
            }
        }
        try {
            Uri uri = FileProvider.getUriForFile(context, "com.yc.mmrecover.myFileProvider", new File(path));
            intent.setDataAndType(uri, type);

            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.toast2(context, "无法打开该格式文件!");
            e.printStackTrace();
        }
    }

    public static String getMachineCode(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GoagalInfo.get().uuid);
        stringBuilder.append(Build.SERIAL);
        stringBuilder.append("mmrecovery");
        return md5(stringBuilder.toString()).substring(12);
    }



}
