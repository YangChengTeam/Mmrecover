package com.yc.mmrecover.pay.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.utils.ToastUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by zhangkai on 2017/3/18.
 */

public class IAliPay1Impl extends IPayImpl {


    private static String APPID = "";
    private static String PARTNERID = "";
    private static String EMAIL = "";
    private static String PRIVATE_KEY = "";
    private static String NOTIFY_URL = "";

    public IAliPay1Impl(Activity context) {
        super(context);
    }

    @Override
    public void pay(OrderInfo orderInfo, IPayCallback iPayCallback) {
        if (orderInfo.getPayInfo() != null) {
            APPID = get(orderInfo.getPayInfo().getAppid(), APPID);
            PARTNERID = get(orderInfo.getPayInfo().getPartnerid(), PARTNERID);
            EMAIL = get(orderInfo.getPayInfo().getEmail(), EMAIL);
            PRIVATE_KEY = get(orderInfo.getPayInfo().getPrivatekey(), PRIVATE_KEY);
            NOTIFY_URL = get(orderInfo.getPayInfo().getNotify_url(), NOTIFY_URL);
        }
        alipay(orderInfo, iPayCallback);
    }

    /**
     * 支付宝支付
     */
    private void alipay(OrderInfo orderInfo, IPayCallback iPayCallback) {


//        String privatekey = GoagalInfo.get().getPublicKey(PRIVATE_KEY);
//        Map<String, String> params = buildOrderParamMap(money, theOrderName, theOrderDetail, ordeID);
//        String orderParam = buildOrderParam(params);//对订单地址排序
//        String sign = getSign(params, privatekey);
        try {
//            if (!TextUtils.isEmpty(sign)) {
//                // 完整的符合支付宝参数规范的订单信息
//                final String payInfo = orderParam + "&" + sign;
            //调用新线程支付
            new Thread(new AlipayRunnable(orderInfo, orderInfo.getPayInfo().getSign(), iPayCallback)).start();
//            } else {
//                new IllegalThreadStateException("签名错误");
//            }
        } catch (Exception e) {
        }
    }


    /**
     * 支付宝支付
     */

    private class AlipayRunnable implements Runnable {
        private OrderInfo orderInfo;
        private String mPayInfo;
        private IPayCallback iPayCallback;

        public AlipayRunnable(OrderInfo orderInfo, String payInfo, IPayCallback iPayCallback) {
            this.orderInfo = orderInfo;
            this.mPayInfo = payInfo;
            this.iPayCallback = iPayCallback;
        }

        @Override
        public void run() {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(mContext);

            // 调用支付接口，获取支付结果
            Map<String, String> result = alipay.payV2(mPayInfo, true);
            PayResult payResult = new PayResult(result);
            final String resultInfo = payResult.getResult();// 同步返回需要验证的信息

            String resultStatus = payResult.getResultStatus();
            if (TextUtils.equals(resultStatus, "9000")) {
                mContext.runOnUiThread(() -> {
                    orderInfo.setMessage("支付成功");
                    iPayCallback.onSuccess(orderInfo);

                });


            } else if (TextUtils.equals(resultStatus, "6001")) {

                mContext.runOnUiThread(() -> {
                    orderInfo.setMessage("支付取消");
                    iPayCallback.onFailure(orderInfo);
                });

            } else {
                mContext.runOnUiThread(() -> {
                    orderInfo.setMessage("支付失败");
                    iPayCallback.onFailure(orderInfo);
                });


            }
        }
    }


    private class PayResult {
        private String resultStatus;
        private String result;
        private String memo;

        public PayResult(Map<String, String> rawResult) {
            if (rawResult == null) {
                return;
            }

            for (String key : rawResult.keySet()) {
                if (TextUtils.equals(key, "resultStatus")) {
                    resultStatus = rawResult.get(key);
                } else if (TextUtils.equals(key, "result")) {
                    result = rawResult.get(key);
                } else if (TextUtils.equals(key, "memo")) {
                    memo = rawResult.get(key);
                }
            }
        }

        @Override
        public String toString() {
            return "resultStatus={" + resultStatus + "};memo={" + memo
                    + "};result={" + result + "}";
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }
    }
}
