package com.yc.mmrecover.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.mmrecover.constant.Config;
import com.yc.mmrecover.model.bean.VipItemInfo;

import java.util.List;

import rx.Observable;


/**
 * Created by suns  on 2019/12/16 08:39.
 */
public class VipEngine extends BaseEngin {
    public VipEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.VIP_ITEM_URL;
    }


    public Observable<ResultInfo<List<VipItemInfo>>> getVipItemInfos() {
        return rxpost(new TypeReference<ResultInfo<List<VipItemInfo>>>() {
        }.getType(), null, true, true, true);
    }
}
