package com.yc.mmrecover.util;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;

import androidx.core.content.ContextCompat;


/**
 * Created by caokun on 2019/11/12 11:34.
 */

public class BackgroundShape  extends GradientDrawable {
    public BackgroundShape(Context context, int i, int i2) {
        setCornerRadius((float) D.getDp((float) i));
        setColor(ContextCompat.getColor(context, i2));
    }

    public BackgroundShape(Context context, int i, int i2, int i3, int i4) {
        setCornerRadius((float) D.getDp((float) i));
        setColor(ContextCompat.getColor(context, i2));
        setStroke(D.getDp((float) i3), ContextCompat.getColor(context, i4));
    }
}