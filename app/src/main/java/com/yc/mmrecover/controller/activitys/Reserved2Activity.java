package com.yc.mmrecover.controller.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kk.utils.LogUtil;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.GlobalData;
import com.yc.mmrecover.utils.BeanUtils;
import com.yc.mmrecover.view.wdiget.BackgroundShape;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.core.content.FileProvider;
import butterknife.BindView;

/**
 * Created by suns  on 2019/12/6 11:37.
 */
public class Reserved2Activity extends BaseActivity {
    @BindView(R.id.tv_step1)
    TextView tvStep1;
    @BindView(R.id.tv_uninstall)
    TextView tvUninstall;
    @BindView(R.id.ll_step1)
    LinearLayout llStep1;
    @BindView(R.id.tv_step2)
    TextView tvStep2;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_percent)
    TextView mTvPercent;
    @BindView(R.id.tv_install)
    TextView mTvInstall;
    @BindView(R.id.ll_step2)
    LinearLayout llStep2;
    @BindView(R.id.tv_step3)
    TextView tvStep3;
    @BindView(R.id.ll_step3)
    LinearLayout llStep3;
    private String mAppName;
    private boolean mIsCancel = false;
    private boolean mNeedUninstall;
    private int mProgress;

    private String mSavePath;

    private Handler mUpdateProgressHandler;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_reserved2;
    }


    /* renamed from: com.recover.wechat.app.view.Reserved2Activity$2 */
    class C09242 implements View.OnClickListener {
        C09242() {
        }

        public void onClick(View view) {
            Reserved2Activity.this.deleteApp();
        }
    }

    /* renamed from: com.recover.wechat.app.view.Reserved2Activity$3 */
    class C09253 implements View.OnClickListener {
        C09253() {
        }

        public void onClick(View view) {
            Reserved2Activity.this.downloadAPK();
        }
    }

    /* renamed from: com.recover.wechat.app.view.Reserved2Activity$4 */
    class C09264 implements Runnable {
        C09264() {
        }

        public void run() {
            try {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    File file = new File(Reserved2Activity.this.mSavePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(GlobalData.hwBackupUrl).openConnection();
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    int contentLength = httpURLConnection.getContentLength();
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(Reserved2Activity.this.mSavePath, Reserved2Activity.this.mAppName));
                    byte[] bArr = new byte[1024];
                    int i = 0;
                    while (!Reserved2Activity.this.mIsCancel) {
                        int read = inputStream.read(bArr);
                        i += read;
                        int i2 = (int) ((((float) i) / ((float) contentLength)) * 100.0f);
                        if (Reserved2Activity.this.mProgress != i2) {
                            Reserved2Activity.this.mProgress = i2;
                            Reserved2Activity.this.mUpdateProgressHandler.sendEmptyMessage(1);
                        }
                        if (read <= 0) {
                            Reserved2Activity.this.mUpdateProgressHandler.sendEmptyMessage(2);
                            break;
                        }
                        fileOutputStream.write(bArr, 0, read);
                    }
                    fileOutputStream.close();
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.recover.wechat.app.view.Reserved2Activity$5 */
    class C09275 extends Handler {
        C09275() {
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    if (Reserved2Activity.this.mProgressBar.getVisibility() == View.GONE) {
                        Reserved2Activity.this.mProgressBar.setVisibility(View.VISIBLE);
                        Reserved2Activity.this.mTvPercent.setVisibility(View.VISIBLE);
                        Reserved2Activity.this.mTvInstall.setVisibility(View.GONE);
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("progress = ");
                    stringBuilder.append(Reserved2Activity.this.mProgress);
                    Reserved2Activity.this.mProgressBar.setProgress(Reserved2Activity.this.mProgress);

                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(Reserved2Activity.this.mProgress);
                    stringBuilder2.append("%");
                    mTvPercent.setText(stringBuilder2.toString());
                    return;
                case 2:
                    Reserved2Activity.this.installAPK(new File(Reserved2Activity.this.mSavePath, Reserved2Activity.this.mAppName));
                    return;
                default:
                    return;
            }
        }
    }

    public Reserved2Activity() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory());
        stringBuilder.append("/数据恢复管家/");
        this.mSavePath = stringBuilder.toString();
        this.mAppName = "huaweibackup.apk";
        this.mNeedUninstall = true;
        this.mUpdateProgressHandler = new C09275();
//        Log.e("TAG", "Reserved2Activity: " + stringBuilder.toString());
    }


    @Override
    protected void initViews() {

        initTitle("无法备份微信数据?");
        this.mNeedUninstall = getIntent().getBooleanExtra("need_uninstall", true);


        tvUninstall.setBackgroundDrawable(new BackgroundShape(this, 22, R.color.yellow_btn));
        tvUninstall.setOnClickListener(new C09242());

        this.mTvInstall.setBackgroundDrawable(new BackgroundShape(this, 22, R.color.yellow_btn));
        this.mTvInstall.setOnClickListener(new C09253());
        initProgressBar();
        if (!this.mNeedUninstall) {
            llStep1.setVisibility(View.GONE);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.setMargins(getDp(25.0f), getDp(40.0f), 0, 0);
            llStep2.setLayoutParams(layoutParams);
            tvStep2.setText("第一步");
            tvStep3.setText("第二步");
        }
    }

    public int getDp(float f) {

        return (int) TypedValue.applyDimension(1, f, getResources().getDisplayMetrics());
    }


    private void initProgressBar() {


        BeanUtils.setFieldValue(this.mProgressBar, "mOnlyIndeterminate", new Boolean(false));
        this.mProgressBar.setIndeterminate(false);
        float[] fArr = new float[]{(float) getDp(7.0f), (float) getDp(7.0f), (float) getDp(7.0f), (float) getDp(7.0f), (float) getDp(7.0f), (float) getDp(7.0f), (float) getDp(7.0f), (float) getDp(7.0f)};
        Drawable shapeDrawable = new ShapeDrawable(new RoundRectShape(fArr, null, null));
        ((ShapeDrawable) shapeDrawable).getPaint().setColor(getResources().getColor(R.color.yellow_word));
        this.mProgressBar.setProgressDrawable(new ClipDrawable(shapeDrawable, 3, 1));
        shapeDrawable = new ShapeDrawable(new RoundRectShape(fArr, null, null));
        ((ShapeDrawable) shapeDrawable).getPaint().setColor(getResources().getColor(R.color.yellow_bk));
        this.mProgressBar.setBackgroundDrawable(shapeDrawable);
//        this.mProgressBar.setIndeterminateDrawable(getResources().getDrawable(17301613));
        this.mProgressBar.setMax(100);
        this.mProgressBar.setProgress(0);
    }

    @SuppressLint("WrongConstant")
    private void deleteApp() {
        Intent intent = new Intent("android.intent.action.DELETE");
        intent.setData(Uri.parse("package:com.huawei.KoBackup"));
        intent.addFlags(SQLiteDatabase.CREATE_IF_NECESSARY);
        startActivity(intent);
    }

    private void downloadAPK() {
        File file = new File(this.mSavePath, this.mAppName);
        if (file.exists()) {
            installAPK(file);
        } else {
            new Thread(new C09264()).start();
        }
    }

    @SuppressLint("WrongConstant")
    protected void installAPK(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mSavePath = ");
        stringBuilder.append(this.mSavePath);
//        C0839log.m1285d(stringBuilder.toString());
        if (file.exists()) {
            this.mProgressBar.setProgress(100);
            this.mTvPercent.setText("100%");
            Intent intent = new Intent("android.intent.action.VIEW");
            if (Build.VERSION.SDK_INT >= 24) {
                intent.setFlags(1);
                intent.setDataAndType(FileProvider.getUriForFile(this, "com.recover.ww.app.fileProvider", file), "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(SQLiteDatabase.CREATE_IF_NECESSARY);
            }
            startActivity(intent);
            return;
        }
        Log.d("TAG", "!apkFile.exists()");
    }

}
