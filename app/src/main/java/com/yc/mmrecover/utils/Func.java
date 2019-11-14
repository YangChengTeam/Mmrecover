package com.yc.mmrecover.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

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



}
