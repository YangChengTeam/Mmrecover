package com.yc.mmrecover.utils;

import com.yc.mmrecover.pay.alipay.OrderInfo;

/**
 * Created by wanglin  on 2018/11/26 17:25.
 */
public interface PayListener {

    void onPayResult(OrderInfo orderInfo);
}
