package com.yc.mmrecover.bean.engin;

import android.content.Context;

import com.kk.securityhttp.engin.BaseEngin;
import com.yc.mmrecover.constant.Config;

public class InitEngin extends BaseEngin {

    public InitEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.INIT_URL;
    }
}
