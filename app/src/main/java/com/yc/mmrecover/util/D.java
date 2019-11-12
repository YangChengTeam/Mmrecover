package com.yc.mmrecover.util;

import android.util.TypedValue;

import com.yc.mmrecover.App;

/**
 * Created by caokun on 2019/11/12 11:34.
 */

public class D {
    public static int getDp(float f) {
        return (int) TypedValue.applyDimension(1, f, App.getApp().getResources().getDisplayMetrics());
    }

    public static int getPx(float f) {
        return (int) TypedValue.applyDimension(0, f, App.getApp().getResources().getDisplayMetrics());
    }

    public static int getSp(float f) {
        return (int) TypedValue.applyDimension(2, f, App.getApp().getResources().getDisplayMetrics());
    }
}