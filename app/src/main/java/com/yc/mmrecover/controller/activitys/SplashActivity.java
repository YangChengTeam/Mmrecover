package com.yc.mmrecover.controller.activitys;

import android.Manifest;
import android.content.Intent;
import android.widget.TextView;

import com.kk.utils.VUiKit;
import com.yc.mmrecover.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class SplashActivity extends BasePermissionActivity {

    @BindView(R.id.tv_app_name)
    TextView appNameTextView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected List<String> getMustPermissions() {
        return Arrays.asList(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE);
    }

    @Override
    protected void onRequestPermissionSuccess() {
        VUiKit.postDelayed(900, () -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        });
    }
}
