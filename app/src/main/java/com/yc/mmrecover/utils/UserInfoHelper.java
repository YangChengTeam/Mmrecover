package com.yc.mmrecover.utils;

import android.content.Context;
import android.content.Intent;
import android.net.rtp.RtpStream;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yc.mmrecover.constant.Config;
import com.yc.mmrecover.controller.activitys.PayActivity;
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

    public static int getVipType() {
        if (userInfo != null) {
            return userInfo.getIsVip();
        }
        return 0;//非vip
    }


    public static boolean gotoVip(Context context) {
        if (!(getVipType() == 1 || getVipType() == 2)) {
            context.startActivity(new Intent(context, PayActivity.class));
            return true;
        }
        return false;
    }
}
