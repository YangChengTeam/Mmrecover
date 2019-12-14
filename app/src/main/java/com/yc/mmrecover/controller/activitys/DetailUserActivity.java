package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding3.view.RxView;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.GlobalData;
import com.yc.mmrecover.model.bean.WxContactInfo;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.utils.MessageUtils;
import com.yc.mmrecover.view.wdiget.BackgroundShape;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by suns  on 2019/12/5 13:49.
 */
public class DetailUserActivity extends BaseActivity {


    @BindView(R.id.im_head)
    ImageView imHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_show_detail)
    TextView tvShowDetail;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.rl_buy)
    RelativeLayout rlBuy;
    private String mUid;
    private WxContactInfo mUserInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_user;
    }

    @Override
    protected void initViews() {
        initTitle("详细资料");
        this.mUid = getIntent().getStringExtra("uid");
        boolean booleanExtra = getIntent().getBooleanExtra("is_from_chat", false);
        this.mUserInfo = MessageUtils.getUserInfoById(this.mUid);
        if (this.mUserInfo != null) {
            final String stringExtra = getIntent().getStringExtra("self_uid");
            final String stringExtra2 = getIntent().getStringExtra("account_parent");
            Glide.with(this).load(this.mUserInfo.getHeadPath()).error(R.mipmap.user_head).into(imHead);
            if (TextUtils.isEmpty(this.mUserInfo.getMarkName())) {
                tvName.setText(this.mUserInfo.getName());
            } else {
                String stringBuilder = this.mUserInfo.getName() +
                        "(" +
                        this.mUserInfo.getMarkName() +
                        ")";
                tvName.setText(stringBuilder);
            }
            if (!TextUtils.isEmpty(this.mUserInfo.getPhone())) {
                tvPhone.setText(this.mUserInfo.getPhone());
            }

            if (booleanExtra) {
                tvShowDetail.setVisibility(View.GONE);
            } else {
                tvShowDetail.setBackgroundDrawable(new BackgroundShape(this, 20, R.color.blue));
                tvShowDetail.setOnClickListener(view -> {
                    Intent intent = new Intent(DetailUserActivity.this, MessageChatActivity.class);
                    intent.putExtra("uid", DetailUserActivity.this.mUid);
                    intent.putExtra("self_uid", stringExtra);
                    intent.putExtra("account_parent", stringExtra2);
                    intent.putExtra("name", mUserInfo.getName());
                    DetailUserActivity.this.startActivity(intent);
                });
            }
//            TextView textView2 = (TextView) findViewById(C0810R.C0809id.tv_buy);
            tvBuy.setBackgroundDrawable(new BackgroundShape(this, 3, R.color.blue));

            StringBuilder stringBuilder;
            if (GlobalData.vipType == 3) {
                rlBuy.setVisibility(View.GONE);

                stringBuilder = new StringBuilder();
                stringBuilder.append("微信号:");
                stringBuilder.append(this.mUserInfo.getWxId());
                tvUserId.setText(stringBuilder.toString());
                return;
            }

            stringBuilder = new StringBuilder();
            stringBuilder.append("微信号:");
            stringBuilder.append(Func.makeStringHeadMix(this.mUserInfo.getWxId()));
            tvUserId.setText(stringBuilder.toString());

        }

        initListener();




    }

    private void initListener() {
        RxView.clicks(tvBuy).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            Intent intent = new Intent(DetailUserActivity.this, PayActivity.class);
            intent.putExtra("sta_type", 2009);
            startActivity(intent);
        });


    }

}
