package com.yc.mmrecover.controller.activitys;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.mmrecover.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.tv_version)
    TextView tvVersion;


    @OnClick({R.id.im_back})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initViews() {
        // TODO add 团队介绍
        tvVersion.setText("团队介绍");
    }
}
