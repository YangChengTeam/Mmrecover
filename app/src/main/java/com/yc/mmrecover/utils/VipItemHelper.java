package com.yc.mmrecover.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yc.mmrecover.constant.Config;
import com.yc.mmrecover.model.bean.VipItemInfo;

import java.util.List;

/**
 * Created by suns  on 2019/12/16 09:37.
 */
public class VipItemHelper {

    private static List<VipItemInfo> vipItemInfos;

    public static List<VipItemInfo> getVipItemInfos() {
        if (vipItemInfos != null) {
            return vipItemInfos;
        }
        try {
            vipItemInfos = JSON.parseArray(SpUtils.getInstance().getString(Config.VIP_ITEMS), VipItemInfo.class);
        } catch (Exception e) {
            Log.e("TAG", "json 解析失败: " + e.getMessage());
        }


        return vipItemInfos;
    }

    public static void saveVipItemInfos(List<VipItemInfo> vipItemInfos) {
        VipItemHelper.vipItemInfos = vipItemInfos;
        try {

            String str = JSON.toJSONString(vipItemInfos);
            SpUtils.getInstance().putString(Config.VIP_ITEMS, str);

        } catch (Exception e) {
            Log.e("TAG", "json 转string失败: " + e.getMessage());
        }

    }
}
