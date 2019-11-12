package com.yc.mmrecover.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;

import androidx.core.content.ContextCompat;

import com.kk.utils.ScreenUtil;

public class BackgroundShape extends GradientDrawable {
    public BackgroundShape(Context context, int i, int i2) {
        setCornerRadius((float) ScreenUtil.px2dip(context, (float) i));
        setColor(ContextCompat.getColor(context, i2));
    }

    public BackgroundShape(Context context, int i, int i2, int i3, int i4) {
        setCornerRadius((float)ScreenUtil.px2dip(context, (float) i));
        setColor(ContextCompat.getColor(context, i2));
        setStroke(ScreenUtil.px2dip(context, (float) i3), ContextCompat.getColor(context, i4));
    }
}
