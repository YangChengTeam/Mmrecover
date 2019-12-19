package com.yc.mmrecover.controller.activitys;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.UserInfo;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.utils.UiUtils;
import com.yc.mmrecover.utils.UserInfoHelper;
import com.yc.mmrecover.view.wdiget.BackgroundShape;

import butterknife.BindView;
import butterknife.OnClick;


public class MyActivity extends BaseActivity {

    @BindView(R.id.im_head)
    ImageView ivHead;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_copy)
    TextView tvCopy;

    @OnClick({R.id.ll_contact, R.id.ll_help, R.id.ll_about, R.id.tv_copy, R.id.im_back})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.tv_copy:
                ClipboardManager clipboardManager = (ClipboardManager) MyActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData newPlainText = ClipData.newPlainText("Label", tvCode.getText().toString());
                if (clipboardManager != null) {
                    clipboardManager.setPrimaryClip(newPlainText);
                }
                Toast.makeText(MyActivity.this, "机器码已复制到剪贴板", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_help:
                Intent intent2 = new Intent(this, WebActivity.class);
                intent2.putExtra("web_title", "帮助");
                intent2.putExtra("web_url", "http://uu.zhanyu22.com/html/help.html?name="+ UiUtils.getAppName());
                startActivity(intent2);
                break;
            case R.id.ll_about:
                startActivity(new Intent(MyActivity.this, AboutUsActivity.class));
                break;

            case R.id.ll_contact:
                startActivity(new Intent(MyActivity.this, AddSuggestActivity.class));
                break;

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my;
    }

    @Override
    protected void initViews() {
        tvCopy.setBackground(new BackgroundShape(this, 14, R.color.gray_button));
        tvCode.setText(Func.getMachineCode(MyActivity.this));

        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (userInfo != null) {
//            int isVip = userInfo.getIsVip();
//            String vipStr = "普通用户";
//            if (isVip==1||isVip==2){
//                vipStr = userInfo.getVip_name();
//            }

            tvUser.setText(userInfo.getVip_name());
        }
    }

}
