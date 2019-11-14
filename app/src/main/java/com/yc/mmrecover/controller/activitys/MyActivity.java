package com.yc.mmrecover.controller.activitys;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yc.mmrecover.R;
import com.yc.mmrecover.utils.BackgroundShape;
import com.yc.mmrecover.utils.Func;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MyActivity extends BasePermissionActivity {

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
            case R.id.ll_contact:
                checkAndRequestPermission();
                break;
            case R.id.ll_help:
                Intent intent2 = new Intent(this, WebActivity.class);
                intent2.putExtra("web_title", "帮助");
                intent2.putExtra("web_url", "http://wxgj.wuhanup.com/help.html");
                startActivity(intent2);
                break;
            case R.id.ll_about:
                startActivity(new Intent(MyActivity.this, AboutUsActivity.class));
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


    }

    @Override
    protected List<String> getMustPermissions() {
        return Arrays.asList(Manifest.permission.CAMERA);
    }

    @Override
    protected void onRequestPermissionSuccess() {
        Intent intent = new Intent(MyActivity.this, WebActivity.class);
        intent.putExtra("web_title", "意见反馈");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://wxapp.leshu.com/home/enquiry?device_id=");
//        stringBuilder.append("http://wxgj.wuhanup.com/feedback.html?device_id=");
        stringBuilder.append(Func.getMachineCode(MyActivity.this));
        intent.putExtra("web_url", stringBuilder.toString());
        startActivity(intent);
    }
}
