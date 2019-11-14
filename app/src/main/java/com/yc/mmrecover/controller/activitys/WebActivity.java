package com.yc.mmrecover.controller.activitys;

import com.yc.mmrecover.R;
import com.yc.mmrecover.utils.MediaUtility;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


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
    private ValueCallback<Uri[]> mFilePathCallbacks;
    private ValueCallback<Uri> mFilePathCallback;
    private static final int REQUEST_LOAD_IMAGE_FROM_GALLERY = 0x10;


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

        Log.d(TAG, "initIntent: title " + mTitle + " url " + mUrl);
    }


    @Override
    protected void initViews() {

        initIntent();

        tvTitle.setText(mTitle);

        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.loadUrl(this.mUrl);
        this.mWebView.addJavascriptInterface(new JsInterface(), "AndroidWebView");
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

        this.mWebView.setWebChromeClient(new WebChromeClient() {


            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mFilePathCallbacks = filePathCallback;
                openGallery();
                return true;
            }

            // 5.0以下的文件上传监听方法
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                Log.e("Webview", "openFileChooser");
                mFilePathCallback = uploadMsg;
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "File Chooser"),
                REQUEST_LOAD_IMAGE_FROM_GALLERY);

//        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
//        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/jpeg");
//        startActivityForResult(intentToPickPic, REQUEST_LOAD_IMAGE_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_LOAD_IMAGE_FROM_GALLERY) {
            if (mFilePathCallback != null) {
                Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                        : intent.getData();
                if (result != null) {
                    String path = MediaUtility.getPath(WebActivity.this.getApplicationContext(),
                            result);
                    Uri uri = Uri.fromFile(new File(path));
                    mFilePathCallback
                            .onReceiveValue(uri);
                } else {
                    mFilePathCallback
                            .onReceiveValue(null);
                }
            }
            if (mFilePathCallbacks != null) {
                Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                        : intent.getData();
                if (result != null) {
                    String path = MediaUtility.getPath(WebActivity.this.getApplicationContext(),
                            result);
                    File file = new File(path);
                    if (file.isFile()) {
                        zipFile(file);
                    }
                } else {
                    mFilePathCallbacks
                            .onReceiveValue(null);
                    mFilePathCallback = null;
                    mFilePathCallbacks = null;
                }
            }
        }
    }

    private void zipFile(File file) {
        if (file == null) {
            Toast.makeText(WebActivity.this, "获取图片失败--file", Toast.LENGTH_SHORT).show();
            return;
        }
        Luban.with(WebActivity.this)
                .load(file)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
//                .setTargetDir(this.getFileStreamPath("certificatess").getAbsolutePath())                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        //  压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        Uri uri = Uri.fromFile(file);
                        mFilePathCallbacks
                                .onReceiveValue(new Uri[]{uri});
                        mFilePathCallback = null;
                        mFilePathCallbacks = null;
                    }

                    @Override
                    public void onError(Throwable e) { //  当压缩过程出现问题时调用
                        Log.d("mylog", "onError: " + e.toString());
                    }
                }).launch();    //启动压缩
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
