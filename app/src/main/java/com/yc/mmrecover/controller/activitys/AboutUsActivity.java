package com.yc.mmrecover.controller.activitys;

import android.widget.TextView;

import com.yc.mmrecover.R;

import butterknife.BindView;

public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.tv_app_name)
    TextView tvAppName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initViews() {
        initTitle("关于我们");
        tvAppName.setText(getString(R.string.app_name));
    }

}
