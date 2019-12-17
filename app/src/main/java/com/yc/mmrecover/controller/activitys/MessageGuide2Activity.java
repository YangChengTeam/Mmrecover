package com.yc.mmrecover.controller.activitys;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding3.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ToastUtil;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.GlobalData;
import com.yc.mmrecover.model.engin.GuideEngine;
import com.yc.mmrecover.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import io.reactivex.functions.Consumer;
import kotlin.Unit;
import rx.Subscriber;


public class MessageGuide2Activity extends BaseActivity {

    @BindView(R.id.tv_backup)
    TextView backupBtn;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tv_step)
    TextView tvStep;
    @BindView(R.id.v_left)
    View vLeft;
    @BindView(R.id.v_right)
    View vRight;
    @BindView(R.id.tv_backup_err)
    TextView tvBackupErr;
    @BindView(R.id.tv_show_backup)
    TextView tvShowBackup;
    @BindView(R.id.tv_qa)
    TextView tvQa;
    private boolean mIsFromGuid3;
    private String[] mGuidImageList;
    private int mCurrentIndex;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_guide2;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initViews() {
        initTitle("恢复微信消息");


        this.mIsFromGuid3 = getIntent().getBooleanExtra("from_guid3", false);

//        if (this.mIsFromGuid3) {
//            GlobalData.isNeedConnectPC = true;
//
//        } else {
//            initViewPage(getGuidImagePath());
//        }

        getGuidImagePath();


        tvQa.getPaint().setFlags(8);
        tvQa.getPaint().setAntiAlias(true);
        tvQa.setText(Html.fromHtml("常见问题"));

        tvBackupErr.getPaint().setFlags(8);
        tvBackupErr.getPaint().setAntiAlias(true);
        tvBackupErr.setText(Html.fromHtml("无法备份微信数据？"));


        RxView.clicks(backupBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            MessageUtils.openBackup(MessageGuide2Activity.this);

        });
        RxView.clicks(tvShowBackup).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            if (!MessageUtils.isBackup()) {
                ToastUtil.toast2(MessageGuide2Activity.this, "您还没有备份，请先备份");
                return;
            }
            startActivity(new Intent(MessageGuide2Activity.this, MessageUserActivity.class));
//            startActivity(new Intent(MessageGuide2Activity.this, MessageGuide3Activity.class));
        });
        RxView.clicks(tvQa).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((Consumer<? super Unit>) aVoid -> {
            Intent intent = new Intent(MessageGuide2Activity.this, WebActivity.class);
            intent.putExtra("web_title", "帮助");
            intent.putExtra("web_url", "http://uu.zhanyu22.com/html/help.html");
            MessageGuide2Activity.this.startActivity(intent);
        });
        RxView.clicks(tvBackupErr).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            Intent intent = new Intent(MessageGuide2Activity.this, Reserved2Activity.class);
            intent.putExtra("need_uninstall", isHasDiffBackup());
            startActivity(intent);

        });
    }


    private boolean isHasDiffBackup() {
        for (PackageInfo packageInfo : getPackageManager().getInstalledPackages(0)) {
            if (TextUtils.equals(packageInfo.packageName, "com.huawei.KoBackup")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("versionCode = ");
                stringBuilder.append(packageInfo.versionCode);

                if (packageInfo.versionCode != 80002301) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        if ((TextUtils.equals("huawei", GlobalData.brand) || TextUtils.equals("honor", GlobalData.brand)) && !isInstallOldBackup()) {
            tvBackupErr.setVisibility(View.VISIBLE);
        }
    }

    private boolean isInstallOldBackup() {
        for (PackageInfo packageInfo : getPackageManager().getInstalledPackages(0)) {
            if (TextUtils.equals(packageInfo.packageName, "com.huawei.KoBackup")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("versionCode = ");
                stringBuilder.append(packageInfo.versionCode);
                Log.e("TAG", stringBuilder.toString());
                if (packageInfo.versionCode == 80002301) {
                    return true;
                }
            }
        }
        return false;
    }


    private void initViewPage(final String[] strArr) {

        final List<ImageView> arrayList = new ArrayList<>();
        final String[] guidContent = getGuidContent();
        int i = 0;
        while (strArr != null && i < strArr.length && !TextUtils.isEmpty(strArr[i])) {
            ImageView imageView = new ImageView(this);
            Glide.with(this).load(strArr[i]).into(imageView);

            int finalI = i;
            imageView.setOnClickListener(view -> {
                Intent intent = new Intent(MessageGuide2Activity.this, ImagePageViewActivity.class);
                intent.putExtra("image_path", strArr);
                intent.putExtra("position", finalI);
                intent.putExtra("is_guid", true);
                intent.putExtra("title_list", guidContent);
                startActivity(intent);
            });
            arrayList.add(imageView);
            i++;
        }
        viewPager.setAdapter(new PagerAdapter() {
            public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
                return view == obj;
            }

            public int getCount() {
                return arrayList.size();
            }

            public void destroyItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object obj) {
                viewGroup.removeView(arrayList.get(i));
            }

            public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
                viewGroup.addView(arrayList.get(i));
                return arrayList.get(i);
            }
        });
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageMargin(getDp(10.0f));

        tvStep.setText(guidContent[0]);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageSelected(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
                if (i < guidContent.length) {
                    tvStep.setText(guidContent[i]);
                }
                MessageGuide2Activity.this.mCurrentIndex = i;
            }
        });
        viewPager.setPageTransformer(true, new ScalePageTransformer());
        vLeft.setOnClickListener(view -> {
            if (MessageGuide2Activity.this.mCurrentIndex - 1 >= 0) {
                viewPager.setCurrentItem(MessageGuide2Activity.this.mCurrentIndex - 1);
            }
        });
        vRight.setOnClickListener(view -> {
            if (MessageGuide2Activity.this.mCurrentIndex + 1 < arrayList.size()) {
                viewPager.setCurrentItem(MessageGuide2Activity.this.mCurrentIndex + 1);
            }
        });
    }


    public int getDp(float f) {
        return (int) TypedValue.applyDimension(1, f, getResources().getDisplayMetrics());
    }


    private String[] getGuidContent() {
        String[] strArr = new String[12];
        if (!GlobalData.isNeedConnectPC) {
            String str = GlobalData.brand;
            Object obj = -1;
            switch (str.hashCode()) {//3620012
                case -1206476313:
                    if (str.equals("huawei")) {
                        obj = 1;
                        break;
                    }
                    break;
                case -759499589:
                    if (str.equals("xiaomi")) {
                        obj = null;
                        break;
                    }
                    break;
                case 3418016:
                    if (str.equals("oppo")) {
                        obj = 3;
                        break;
                    }
                    break;
                case 99462250:
                    if (str.equals("honor")) {
                        obj = 2;
                        break;
                    }
                    break;
                case 103777484:
                    if (str.equals("meizu")) {
                        obj = 4;
                        break;
                    }
                    break;
            }

            if (obj == null) {
                strArr[0] = "第1步 新建备份（别点锁的图标）";
                strArr[1] = "第2步 展开应用 找到微信";
                strArr[2] = "第3步 找到微信并勾选";
                strArr[3] = "第4步 备份完成返回本软件";
            } else if (obj.equals(1) || obj.equals(2)) {
                strArr[0] = "第1步 点击 备份";
                strArr[1] = "第2步 选择 内部存储";
                strArr[2] = "第3步 展开 应用栏";
                strArr[3] = "第4步 勾选微信";
                strArr[4] = "第5步 跳过密码区";
                strArr[5] = "第6步 备份完成返回本软件";
            } else if (obj.equals(3)) {
                strArr[0] = "第1步 找到备份程序(新建备份)";
                strArr[1] = "第2步 展开应用 找到微信";
                strArr[2] = "第3步 耐心等待备份完成";
                strArr[3] = "第4步 备份完成返回本软件";
            } else if (obj.equals(4)) {
                strArr[0] = "第1步 找到备份程序(添加备份)";
                strArr[1] = "第2步 展开应用 找到微信";
                strArr[2] = "第3步 找到微信并勾选";
                strArr[3] = "第4步 耐心等待备份完成";
                strArr[4] = "第5步 备份完成返回本软件";
            } else if (TextUtils.equals("vivo", str)) {
                strArr[0] = "第1步（电脑上）用电脑下载并安装pc端微信恢复管家";
                strArr[1] = "第2步（手机上）手机连接电脑，并打开开发者选项，选择“设置”";
                strArr[2] = "第3步（手机上）进入“更多设置”";
                strArr[3] = "第4步（手机上）进入“关于手机”";
                strArr[4] = "第5步（手机上）进入“版本信息”";
                strArr[5] = "第6步（手机上）“软件版本号”快速点击5-7次，等待提示";
                strArr[6] = "第7步（手机上）再回到第二步“更多设置”拉倒最后,“开发者选项”";
                strArr[7] = "第8步（手机上）打开“开发者选项”和“USB调试”开关";
                strArr[8] = "第9步（电脑上）选择空间足够大的盘存放备份文件";
                strArr[9] = "第10步（电脑上）出现以下信息时，请在手机上确认";
                strArr[10] = "第11步（手机上）不要设置密码，点击“备份我的数据”";
                strArr[11] = "第12步（电脑上）备份完成";
            } else {
                strArr[0] = "第1步 点击 备份";
                strArr[1] = "第2步 选择 内部存储";
                strArr[2] = "第3步 展开 应用栏";
                strArr[3] = "第4步 勾选微信";
                strArr[4] = "第5步 跳过密码区";
                strArr[5] = "第6步 备份完成返回本软件";
            }
        }

        return strArr;
    }

    public class ScalePageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(@NonNull View view, float f) {
            if (f < -1.0f) {
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
            } else if (f <= 0.0f) {
                f = ((f + 1.0f) * 0.25f) + MIN_SCALE;
                view.setScaleX(f);
                view.setScaleY(f);
            } else if (f <= 1.0f) {
                float f2 = ((1.0f - f) * 0.25f) + MIN_SCALE;
                view.setScaleX(f2);
                view.setScaleY(f2);
            } else {
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
            }
        }
    }


    private List<String> initPageData() {
        List<String> pages = new ArrayList<>();
        pages.add("http://wxapp.leshu.com/public/image/vivo1.png");
        pages.add("http://wxapp.leshu.com/public/image/vivo2.png");
        pages.add("http://wxapp.leshu.com/public/image/vivo3.png");
        pages.add("http://wxapp.leshu.com/public/image/vivo4.png");
        pages.add("http://wxapp.leshu.com/public/image/vivo5.png");
        pages.add("http://wxapp.leshu.com/public/image/vivo6.png");
        pages.add("http://wxapp.leshu.com/public/image/vivo7.png");
        pages.add("http://wxapp.leshu.com/public/image/vivo8.png");
        pages.add("http://wxapp.leshu.com/public/image/vivo9.png");
        pages.add("http://wxapp.leshu.com/public/image/vivo10.png");
        pages.add("http://wxapp.leshu.com/public/image/vivo11.png");
        pages.add("http://wxapp.leshu.com/public/image/vivo12.png");
        return pages;
    }


    private void getGuidImagePath() {

        GuideEngine guideEngine = new GuideEngine(this);
        guideEngine.getGuideImages().subscribe(new Subscriber<ResultInfo<List<String>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<String>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.getCode() == HttpConfig.STATUS_OK && listResultInfo.getData() != null) {
                    initViewPage(listResultInfo.getData().toArray(new String[0]));
                }
            }
        });


    }

    public static void startActivityForPackage(Context context, String str, String str2) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(str, str2));
            intent.setAction("android.intent.action.VIEW");
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("TAG", "startActivityForPackage: " + e.getMessage());
        }
    }

}
