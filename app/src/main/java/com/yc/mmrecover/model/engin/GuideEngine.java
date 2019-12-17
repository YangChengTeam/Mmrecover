package com.yc.mmrecover.model.engin;

import android.content.Context;
import android.os.Build;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.mmrecover.constant.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;


/**
 * Created by suns  on 2019/12/14 09:46.
 * 不同手机的备份引导
 */
public class GuideEngine extends BaseEngin {
    public GuideEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.GUIDE_URL;
    }


    public Observable<ResultInfo<List<String>>> getGuideImages() {

        Map<String, String> params = new HashMap<>();

        params.put("brand", Build.BRAND);

        return rxpost(new TypeReference<ResultInfo<List<String>>>() {
        }.getType(), params, true, true, true);
    }
}
