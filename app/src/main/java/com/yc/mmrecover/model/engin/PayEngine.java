package com.yc.mmrecover.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.mmrecover.constant.Config;
import com.yc.mmrecover.pay.alipay.OrderInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by suns  on 2019/12/16 15:42.
 */
public class PayEngine extends BaseEngine {
    public PayEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<OrderInfo>> createOrder(String card_id, String payway) {
        Map<String, String> params = new HashMap<>();

        params.put("card_id", card_id);
        params.put("payway", payway);
        return HttpCoreEngin.get(mContext).rxpost(Config.ORDER_URL, new TypeReference<ResultInfo<OrderInfo>>() {
        }.getType(), params, true, true, true);
    }
}
