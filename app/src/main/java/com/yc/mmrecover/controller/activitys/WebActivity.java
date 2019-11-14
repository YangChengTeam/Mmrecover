package com.yc.mmrecover.controller.activitys;

import com.yc.mmrecover.R;

import android.annotation.TargetApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by caokun on 2019/11/12 13:40.
 */

public class WebActivity extends BaseActivity {

    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.tv_title)
    TextView tvTitle;


    private String mTitle;
    private String mUrl;
    private String TAG = "mmrecover_log_WebActivity";


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
        return R.layout.activity_web;
    }

    private void initIntent() {
        this.mTitle = getIntent().getStringExtra("web_title");
        this.mUrl = getIntent().getStringExtra("web_url");

    }


    @Override
    protected void initViews() {

        initIntent();

        tvTitle.setText(mTitle);

        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.loadUrl(this.mUrl);
        this.mWebView.addJavascriptInterface(new JsInterface(), "AndroidWebView");
        this.mWebView.setWebChromeClient(new WebChromeClient());
        this.mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        this.mWebView.setWebViewClient(new WebViewClient() {
            @TargetApi(19)
            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
            }

            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                return super.shouldOverrideUrlLoading(webView, str);
            }
        });
    }


    private class JsInterface {
        @JavascriptInterface
        public void showChannel() {
        }

        JsInterface() {
        }

        @JavascriptInterface
        public void closeWindow() {
            WebActivity.this.finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (this.mWebView.canGoBack()) {
                this.mWebView.goBack();
                return true;
            }
            finish();
        }
        return false;
    }

}
