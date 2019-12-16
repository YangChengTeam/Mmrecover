package com.yc.mmrecover.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.mmrecover.constant.Config;
import com.yc.mmrecover.model.bean.UploadInfo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by suns  on 2019/12/14 10:03.
 */
public class SuggestEngine extends BaseEngin {
    public SuggestEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.SUGGEST_URL;
    }


    public Observable<ResultInfo<UploadInfo>> uploadPic(File file) {
        UpFileInfo upFileInfo = new UpFileInfo();
        upFileInfo.file = file;
        upFileInfo.filename = file.getName();
        upFileInfo.name = "file";


        return rxuploadFile(new TypeReference<ResultInfo<UploadInfo>>() {
        }.getType(), upFileInfo, null, false);
    }

    public Observable<ResultInfo<List<String>>> addSuggest(String userId, String content, String imgs) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("content", content);
        params.put("imgs", imgs);
        return rxpost(new TypeReference<ResultInfo<List<String>>>() {
        }.getType(), params, true, true, true);
    }
}
