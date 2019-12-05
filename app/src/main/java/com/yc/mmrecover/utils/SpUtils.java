package com.yc.mmrecover.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yc.mmrecover.App;

/**
 * Created by suns  on 2019/12/5 14:59.
 */
public class SpUtils {

    private static SpUtils instance;
    private final SharedPreferences sp;

    private SpUtils() {
        sp = App.getApp().getSharedPreferences("wx_recover", Context.MODE_PRIVATE);
    }

    public static SpUtils getInstance() {
        if (instance == null) {
            synchronized (SpUtils.class) {
                instance = new SpUtils();
            }
        }
        return instance;
    }


    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String value) {

        return sp.getString(key, value);
    }
}
