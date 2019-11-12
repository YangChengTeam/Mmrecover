package com.yc.mmrecover.controller.activitys;

import android.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.GlobalData;
import com.yc.mmrecover.util.BackgroundShape;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class PayActivity extends BaseActivity {


    @BindViews({R.id.rl_price1, R.id.rl_price2})
    public List<RelativeLayout> rlPrices;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.im_read)
    ImageView imRead;
    @BindViews({R.id.im_wx_select, R.id.im_ali_select})
    List<ImageView> ivPayTypes;
//    @BindViews({R.id.rl_pay_wx, R.id.rl_pay_wx})
//    List<RelativeLayout> rlPayContainer;

    @BindView(R.id.tv_28)
    TextView tv28;
    @BindView(R.id.tv_98)
    TextView tv98;


    private String TAG = "mmrecover_log_PayActivity";
    private int mPayType = 2;  //支付类型  2 微信  1 支付宝
    private boolean mIs98 = true; //是否为支付98元购买会员
    private boolean mIsRead = true; //我已阅读用户须知


    @OnClick({R.id.im_back, R.id.tv_pay, R.id.rl_pay_wx, R.id.rl_pay_ali, R.id.rl_price1, R.id.rl_price2, R.id.tv_note, R.id.ll_read})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                showNormalDialog();
                break;
            case R.id.tv_pay:
                if (!PayActivity.this.mIsRead) {
                    Toast.makeText(PayActivity.this, "请阅读会员须知", Toast.LENGTH_SHORT).show();
                    return;
                }
                // TODO to pay
                break;
            case R.id.rl_pay_wx: //微信支付
                ivPayTypes.get(0).setImageDrawable(PayActivity.this.getResources().getDrawable(R.mipmap.pay_check));
                ivPayTypes.get(1).setImageDrawable(PayActivity.this.getResources().getDrawable(R.mipmap.pay_un_check));
                PayActivity.this.mPayType = 2;
                break;
            case R.id.rl_pay_ali: //支付宝支付
                ivPayTypes.get(0).setImageDrawable(PayActivity.this.getResources().getDrawable(R.mipmap.pay_un_check));
                ivPayTypes.get(1).setImageDrawable(PayActivity.this.getResources().getDrawable(R.mipmap.pay_check));
                PayActivity.this.mPayType = 1;
                break;
            case R.id.rl_price1:  //98元会员框
                PayActivity.this.mIs98 = true;
                rlPrices.get(0).setBackgroundDrawable(new BackgroundShape(PayActivity.this, 6, R.color.white, 1, R.color.yellow_btn));
                rlPrices.get(1).setBackgroundDrawable(new BackgroundShape(PayActivity.this, 6, R.color.white, 1, R.color.gray_bk2));
                changeTvPayText(GlobalData.payCount3);
                break;
            case R.id.rl_price2: //28元会员框
                PayActivity.this.mIs98 = false;
                rlPrices.get(0).setBackgroundDrawable(new BackgroundShape(PayActivity.this, 6, R.color.white, 1, R.color.gray_bk2));
                rlPrices.get(1).setBackgroundDrawable(new BackgroundShape(PayActivity.this, 6, R.color.white, 1, R.color.yellow_btn));
                changeTvPayText(GlobalData.payCount2);
                break;
            case R.id.tv_note: //用户须知
                Intent intent = new Intent(PayActivity.this, WebActivity.class);
                intent.putExtra("web_title", "会员须知");
                intent.putExtra("web_url", "http://wxapp.leshu.com/home/userNotice");
                PayActivity.this.startActivity(intent);
                break;
            case R.id.ll_read: //我已阅读用户须知
                if (PayActivity.this.mIsRead) {
                    PayActivity.this.mIsRead = false;
                    imRead.setImageDrawable(PayActivity.this.getResources().getDrawable(R.mipmap.pay_un_check));
                    return;
                }
                PayActivity.this.mIsRead = true;
                imRead.setImageDrawable(PayActivity.this.getResources().getDrawable(R.mipmap.pay_read));
                break;
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void initViews() {
        tvTitle.setText("订单支付");
        tvPay.setBackgroundDrawable(new BackgroundShape(this, 22, R.color.yellow_btn));

        initPayNum();

        tvNote.getPaint().setFlags(8);
        tvNote.getPaint().setAntiAlias(true);
        tvNote.setText(Html.fromHtml("《会员须知》"));

    }


    private void initPayNum() {
        StringBuilder stringBuilder;
        if (GlobalData.vipType == 2) {
            findViewById(R.id.rl_price2).setVisibility(View.GONE);
            stringBuilder = new StringBuilder();
            stringBuilder.append(GlobalData.payCount4);
            stringBuilder.append("");
            tv98.setText(stringBuilder.toString());

            changeTvPayText(GlobalData.payCount4);
            return;
        }

        stringBuilder = new StringBuilder();
        stringBuilder.append(GlobalData.payCount2);
        stringBuilder.append("元");
        tv28.setText(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(GlobalData.payCount3);
        stringBuilder.append("元");
        tv98.setText(stringBuilder.toString());

        changeTvPayText(GlobalData.payCount3);

        rlPrices.get(0).setBackgroundDrawable(new BackgroundShape(this, 6, R.color.white, 1, R.color.yellow_btn));
        rlPrices.get(1).setBackgroundDrawable(new BackgroundShape(this, 6, R.color.white, 1, R.color.gray_bk2));
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("GlobalData.vipType = ");
        stringBuilder2.append(GlobalData.vipType);
        Log.d(TAG, "initPayNum: " + stringBuilder2.toString());
    }

    private void changeTvPayText(String payCount) {
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("确认支付 ¥");
        stringBuilder2.append(payCount);
        stringBuilder2.append("元");
        tvPay.setText(stringBuilder2.toString());
    }


    private void showNormalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((CharSequence) "温馨提示");
        builder.setMessage((CharSequence) "如没有及时恢复，误删的微信数据会被系统碎片覆盖掉造成永久丢失。\n数据无价，尽快恢复，确定要放弃恢复吗？");
        builder.setPositiveButton((CharSequence) "继续恢复", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton((CharSequence) "放弃恢复", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
//                CacheSave.setValue(PayActivity.this, "order_id", ""); //TODO
//                StModel.event(2005);
                PayActivity.this.finish();
            }
        });
        builder.show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        }
        showNormalDialog();
        return true;
    }

}
