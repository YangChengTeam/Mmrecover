package com.yc.mmrecover.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.yc.mmrecover.App;

/**
 * Created by suns  on 2019/12/19 15:23.
 */
public class UiUtils {
    public static String mAppName = "";

    public static String getAppName() {

        if (TextUtils.isEmpty(mAppName)) {
            Context context = App.getApp().getApplicationContext();
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (null != applicationInfo) {
                mAppName = (String) packageManager.getApplicationLabel(applicationInfo);
            } else {
                mAppName = context.getResources().getString(context.getApplicationInfo().labelRes);
            }
        }
        return mAppName;
    }
}
