package com.yc.mmrecover.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yc.mmrecover.constant.Config;
import com.yc.mmrecover.model.bean.UserInfo;

/**
 * Created by suns  on 2019/12/16 09:46.
 */
public class UserInfoHelper {

    private static UserInfo userInfo;

    public static UserInfo getUserInfo() {
        if (userInfo != null) {
            return userInfo;
        }
        try {
            userInfo = JSON.parseObject(SpUtils.getInstance().getString(Config.USER_INFO), UserInfo.class);
        } catch (Exception e) {
            Log.e("TAG", "json解析失败: " + e.getMessage());
        }
        return userInfo;

    }

    public static void saveUserInfo(UserInfo userInfo) {
        UserInfoHelper.userInfo = userInfo;
        try {
            String str = JSON.toJSONString(userInfo);
            SpUtils.getInstance().putString(Config.USER_INFO, str);
        } catch (Exception e) {
            Log.e("TAG", "json转换失败: " + e.getMessage());
        }
    }
}
