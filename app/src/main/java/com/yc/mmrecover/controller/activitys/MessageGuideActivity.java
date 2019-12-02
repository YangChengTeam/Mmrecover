package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.yc.mmrecover.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class MessageGuideActivity extends BaseActivity {

    @BindView(R.id.tv_next)
    TextView nextBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_guide;
    }

    @Override
    protected void initViews() {
        RxView.clicks(nextBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            startActivity(new Intent(MessageGuideActivity.this, MessageGuide2Activity.class));
        });
    }
}
