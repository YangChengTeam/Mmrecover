package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.mmrecover.R;
import com.yc.mmrecover.util.BackgroundShape;

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
                stringBuilder.append(deviceId); //TODO
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
