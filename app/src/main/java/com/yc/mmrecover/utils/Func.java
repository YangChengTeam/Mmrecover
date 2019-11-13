package com.yc.mmrecover.utils;

import java.text.DecimalFormat;

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



}
