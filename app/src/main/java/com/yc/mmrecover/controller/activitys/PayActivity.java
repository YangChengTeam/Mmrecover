package com.yc.mmrecover.controller.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.BroadcastInfo;
import com.yc.mmrecover.model.bean.GlobalData;
import com.yc.mmrecover.model.bean.VipItemInfo;
import com.yc.mmrecover.model.engin.VipEngine;
import com.yc.mmrecover.utils.VipItemHelper;
import com.yc.mmrecover.view.adapters.VipItemAdapter;
import com.yc.mmrecover.view.wdiget.BackgroundShape;
import com.yc.mmrecover.view.wdiget.VTextView;

import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class PayActivity extends BaseActivity {
    @BindViews({R.id.rl_price1, R.id.rl_price2})
    public List<RelativeLayout> rlPrices;
    @BindViews({R.id.tv_pay98_des, R.id.tv_pay28_des})
    public List<TextView> tvPayDess;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.im_read)
    ImageView imRead;
    @BindView(R.id.tv_broadcast)
    VTextView broadcastVTextView;
    @BindViews({R.id.im_wx_select, R.id.im_ali_select})
    List<ImageView> ivPayTypes;
//    @BindViews({R.id.rl_pay_wx, R.id.rl_pay_wx})
//    List<RelativeLayout> rlPayContainer;

    @BindView(R.id.tv_28)
    TextView tv28;
    @BindView(R.id.tv_98)
    TextView tv98;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private String[][] tmpBroadcastInfo;


    private String TAG = "mmrecover_log_PayActivity";
    private int mPayType = 2;  //支付类型  2 微信  1 支付宝
    private boolean mIs98 = true; //是否为支付98元购买会员
    private boolean mIsRead = true; //我已阅读用户须知
    private VipItemAdapter vipItemAdapter;
    private VipItemInfo vipItemInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay;
    }


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

                rlPrices.get(0).setBackground(new BackgroundShape(PayActivity.this, 50, R.color.white, 10, R.color.yellow_btn));
                rlPrices.get(1).setBackground(new BackgroundShape(PayActivity.this, 50, R.color.white, 10, R.color.gray_bk2));
                changeTvPayText(GlobalData.payCount3);
                break;
            case R.id.rl_price2: //28元会员框
                PayActivity.this.mIs98 = false;

                rlPrices.get(0).setBackground(new BackgroundShape(PayActivity.this, 50, R.color.white, 10, R.color.gray_bk2));
                rlPrices.get(1).setBackground(new BackgroundShape(PayActivity.this, 50, R.color.white, 10, R.color.yellow_btn));
                changeTvPayText(GlobalData.payCount2);
                break;
            case R.id.tv_note: //用户须知
                Intent intent = new Intent(PayActivity.this, WebActivity.class);
                intent.putExtra("web_title", "会员须知");
                intent.putExtra("web_url", "http://wxgj.wuhanup.com/userNotice.html");
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
    protected void initViews() {
        tvTitle.setText("订单支付");
        tvPay.setBackground(new BackgroundShape(this, 22, R.color.yellow_btn));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vipItemAdapter = new VipItemAdapter(null);
        recyclerView.setAdapter(vipItemAdapter);


        initPayNum();

        tvNote.getPaint().setFlags(8);
        tvNote.getPaint().setAntiAlias(true);
        tvNote.setText(Html.fromHtml("《会员须知》"));

        tvPayDess.get(0).setBackground(new BackgroundShape(this, 8, R.color.red_case));
        tvPayDess.get(1).setBackground(new BackgroundShape(this, 8, R.color.red_case));

        rlPrices.get(0).setBackground(new BackgroundShape(this, 50, R.color.white, 10, R.color.yellow_btn));
        rlPrices.get(1).setBackground(new BackgroundShape(this, 50, R.color.white, 10, R.color.gray_bk2));

        initBoradInfo();

        getData();
        initListener();
    }


    private void initListener() {
        vipItemAdapter.setOnItemClickListener((adapter, view, position) -> {
            vipItemInfo = vipItemAdapter.getItem(position);
            if (vipItemInfo != null) {
                vipItemAdapter.setSelected(position);
            }

        });
    }

    private void initBoradInfo() {
        String[][] r1 = new String[15][];
        r1[0] = new String[]{"华为Mate8用户", "恢复微信聊天记录", "9分钟前"};
        r1[1] = new String[]{"红米Note8用户", "恢复微信视频", "7分钟前"};
        r1[2] = new String[]{"华为P9用户", "恢复46张微信照片", "13分钟前"};
        r1[3] = new String[]{"华为Mate7用户", "恢复微信聊天记录", "26分钟前"};
        r1[4] = new String[]{"小米4A用户", "恢复微信聊天记录", "16分钟前"};
        r1[5] = new String[]{"一加5t用户", "恢复微信聊天记录", "6分钟前"};
        r1[6] = new String[]{"荣耀畅玩4X用户", "恢复微信聊天记录", "8分钟前"};
        r1[7] = new String[]{"OPPOA57用户", "恢复16张微信照片", "15分钟前"};
        r1[8] = new String[]{"红米Note3用户", "恢复微信聊天记录", "25分钟前"};
        r1[9] = new String[]{"华为畅享8用户", "恢复18张微信照片", "5分钟前"};
        r1[10] = new String[]{"红米Note4用户", "恢复微信聊天记录", "32分钟前"};
        r1[11] = new String[]{"VivoX9用户", "恢复微信视频", "15分钟前"};
        r1[12] = new String[]{"魅族Note9用户", "恢复微信聊天记录", "16分钟前"};
        r1[13] = new String[]{"OPPOA5用户", "恢复132张微信照片", "12分钟前"};
        r1[14] = new String[]{"OPPOR17用户", "恢复微信聊天记录", "3分钟前"};
        tmpBroadcastInfo = r1;

        List arrayList = new ArrayList();
        for (int i = 0; i < this.tmpBroadcastInfo.length; i++) {
            BroadcastInfo broadcastInfo = new BroadcastInfo();
            broadcastInfo.setUserName(this.tmpBroadcastInfo[i][0]);
            broadcastInfo.setContent(this.tmpBroadcastInfo[i][1]);
            broadcastInfo.setTime(this.tmpBroadcastInfo[i][2]);
            arrayList.add(broadcastInfo);
        }

        this.broadcastVTextView.setText(14.0f, 5, getResources().getColor(R.color.black));
        this.broadcastVTextView.setTextStillTime(5000);
        this.broadcastVTextView.setAnimTime(500);
        this.broadcastVTextView.startAutoScroll();

        ArrayList list = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(((BroadcastInfo) arrayList.get(i)).getUserName());
            stringBuilder2.append(" ");
            stringBuilder2.append(((BroadcastInfo) arrayList.get(i)).getContent());
            stringBuilder2.append("   ");
            stringBuilder2.append(((BroadcastInfo) arrayList.get(i)).getTime());
            CharSequence stringBuilder3 = stringBuilder2.toString();
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append("content = ");
            stringBuilder4.append(stringBuilder3);
            int length = ((BroadcastInfo) arrayList.get(i)).getUserName().length() + 1;
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(stringBuilder3);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_word)), 0, length, 34);
            Log.d(TAG, "initBoradInfo: stringBuilder3 " + stringBuilder3);
            Log.d(TAG, "initBoradInfo: stringBuilder4 " + stringBuilder4);
            list.add(spannableStringBuilder);
        }
        this.broadcastVTextView.setTextList(list);
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
        tv28.setText(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(GlobalData.payCount3);
        tv98.setText(stringBuilder.toString());

        changeTvPayText(GlobalData.payCount3);


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

    private void getData() {

        List<VipItemInfo> vipItemInfoList = VipItemHelper.getVipItemInfos();
        if (vipItemInfoList != null && vipItemInfoList.size() > 0) {
            vipItemAdapter.setNewData(vipItemInfoList);
            vipItemInfo = vipItemInfoList.get(0);
        }

        VipEngine vipEngine = new VipEngine(this);
        vipEngine.getVipItemInfos().subscribe(new Subscriber<ResultInfo<List<VipItemInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<VipItemInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.getCode() == HttpConfig.STATUS_OK && listResultInfo.getData() != null) {
                    List<VipItemInfo> data = listResultInfo.getData();
                    vipItemAdapter.setNewData(data);
                    VipItemHelper.saveVipItemInfos(data);
                    if (data.size() > 0) {
                        vipItemInfo = data.get(0);
                    }

                }
            }
        });
    }


}
