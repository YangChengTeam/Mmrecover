package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.yc.mmrecover.R;
import com.yc.mmrecover.utils.MessageUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class MessageGuide2Activity extends BaseActivity {

    @BindView(R.id.tv_backup)
    TextView backupBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_guide2;
    }

    @Override
    protected void initViews() {
        RxView.clicks(backupBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            MessageUtils.openBackup(MessageGuide2Activity.this);
        });
    }
}
