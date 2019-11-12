package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.yc.mmrecover.R;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        hideStatusBarBack();

        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initViews();
    }

    protected void initTitle(String str) {
        ((TextView) findViewById(R.id.tv_title)).setText(str);
        findViewById(R.id.im_back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BaseActivity.this.finish();
            }
        });
    }


    protected abstract int getLayoutId();

    protected abstract void initViews();

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void hideStatusBarBack() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(67108864);
            window.getDecorView().setSystemUiVisibility(1280);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        }
    }

}
