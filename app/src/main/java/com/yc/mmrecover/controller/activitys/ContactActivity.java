package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.GlobalData;
import com.yc.mmrecover.util.BackgroundShape;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.view.View.OnClickListener;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * 客服
 */

public class ContactActivity extends BaseActivity {

    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.ll_contact)
    LinearLayout llContact;

    private View gdView, qqView, wxView;


    @OnClick({R.id.im_back, R.id.tv_pay})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.tv_pay:
                Intent intent = new Intent(ContactActivity.this, PayActivity.class);
                intent.putExtra("sta_type", 7001);  //TODO  sta_type?
                startActivity(intent);
                break;
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalData.vipType == 1) {
            ((TextView) this.wxView.findViewById(R.id.tv_title)).setText("微信号：********");
            ((TextView) this.wxView.findViewById(R.id.tv_content)).setText("VIP专属客服，成为会员显示号码");
            ((TextView) this.qqView.findViewById(R.id.tv_title)).setText("QQ号：********");
            ((TextView) this.qqView.findViewById(R.id.tv_content)).setText("VIP专属客服，成为会员显示号码");
            this.wxView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Toast.makeText(ContactActivity.this, "成为会员显示号码", Toast.LENGTH_SHORT).show();
                }
            });
            this.qqView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Toast.makeText(ContactActivity.this, "成为会员显示号码", Toast.LENGTH_SHORT).show();
                }
            });
            this.tvPay.setVisibility(View.VISIBLE);
            return;
        }
        String wechatId = "123456";
        String qqId = "123456";
        TextView tvTitle = (TextView) this.wxView.findViewById(R.id.tv_title);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("微信号：");
//        stringBuilder.append(GlobalData.customer_wx); //TODO
        stringBuilder.append(wechatId);
        tvTitle.setText(stringBuilder.toString());
        ((TextView) this.wxView.findViewById(R.id.tv_content)).setText("专人专线，您的专属VIP客服");
        tvTitle = (TextView) this.qqView.findViewById(R.id.tv_title);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("QQ号：");
//        stringBuilder2.append(GlobalData.customer_qq);  //TODO
        stringBuilder2.append(qqId);
        tvTitle.setText(stringBuilder2.toString());
        ((TextView) this.qqView.findViewById(R.id.tv_content)).setText("专人专线，您的专属VIP客服");
        this.wxView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) ContactActivity.this.getSystemService("clipboard");
//                String wechatId= GlobalData.customer_wx; //TODO
                ClipData newPlainText = ClipData.newPlainText("Label", wechatId);
                if (clipboardManager != null) {
                    clipboardManager.setPrimaryClip(newPlainText);
                }
                Toast.makeText(ContactActivity.this, "微信号已复制到剪贴板", Toast.LENGTH_SHORT).show();
            }
        });
        this.qqView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) ContactActivity.this.getSystemService("clipboard");
//                String qqId = GlobalData.customer_qq;  //TODO
                ClipData newPlainText = ClipData.newPlainText("Label", qqId);
                if (clipboardManager != null) {
                    clipboardManager.setPrimaryClip(newPlainText);
                }
                Toast.makeText(ContactActivity.this, "QQ号已复制到剪贴板", Toast.LENGTH_SHORT).show();
            }
        });
        this.tvPay.setVisibility(View.GONE);
    }

    @Override
    protected void initViews() {
        tvPay.setBackgroundDrawable(new BackgroundShape(this, 25, R.color.yellow_btn));

        this.wxView = getCustomerButton("客服微信", R.mipmap.customer_wx);
        llContact.addView(this.wxView);
        this.qqView = getCustomerButton("客服QQ", R.mipmap.customer_qq);
        llContact.addView(this.qqView);
        this.gdView = getCustomerButton("客服工单", R.mipmap.customer_gd);
        llContact.addView(this.gdView);
        ((TextView) this.gdView.findViewById(R.id.tv_title)).setText("用户工单支持");
        ((TextView) this.gdView.findViewById(R.id.tv_content)).setText("全体用户可用，VIP优先回复");
        this.gdView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ContactActivity.this, WebActivity.class);
                intent.putExtra("web_title", "意见反馈");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("http://wxapp.leshu.com/home/enquiry?device_id=");
                String deviceId = "2";
//                stringBuilder.append(Func.getMachineCode(ContactActivity.this)); //TODO
                stringBuilder.append(deviceId);
                intent.putExtra("web_url", stringBuilder.toString());
                ContactActivity.this.startActivity(intent);
            }
        });
    }

    private View getCustomerButton(String str, int i) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_customer, null);
        ((TextView) inflate.findViewById(R.id.tv_name)).setText(str);
        ((ImageView) inflate.findViewById(R.id.im_icon)).setImageDrawable(getResources().getDrawable(i));
        return inflate;
    }

}
