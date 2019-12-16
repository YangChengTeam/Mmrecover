package com.yc.mmrecover.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.mmrecover.constant.Config;
import com.yc.mmrecover.model.bean.IndexInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by suns  on 2019/12/12 15:59.
 */
public class IndexEngine extends BaseEngin {
    public IndexEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.USER_URL;
    }


    public Observable<ResultInfo<IndexInfo>> getIndexInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        return rxpost(new TypeReference<ResultInfo<IndexInfo>>() {
        }.getType(), params, true, true, true);
    }
}
