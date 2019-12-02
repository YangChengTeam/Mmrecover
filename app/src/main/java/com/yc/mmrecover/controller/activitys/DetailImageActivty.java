package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding3.view.RxView;
import com.kk.utils.LogUtil;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.view.wdiget.BackgroundShape;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.view.wdiget.GestureImageView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class DetailImageActivty extends BaseActivity {

    @BindView(R.id.tv_time)
    TextView mTvTime;

    @BindView(R.id.tv_size)
    TextView mTvSize;

    @BindView(R.id.tv_measure)
    TextView mTvMeasure;

    @BindView(R.id.im_image)
    GestureImageView mImImage;

    @BindView(R.id.tv_recover)
    TextView mTvRecover;

    @BindView(R.id.tv_recovered)
    TextView mTvRecovered;

    @BindView(R.id.im_water_mark)
    ImageView mImWaterMark;

    private MediaInfo mediaInfo;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_detail;
    }

    @Override
    protected void initViews() {
        initTitle("照片详情");
        mTvRecover.setBackground(new BackgroundShape(this, 22, R.color.yellow_btn));
        mTvRecovered.setBackground(new BackgroundShape(this, 22, R.color.gray_button2));

        this.mediaInfo = (MediaInfo) getIntent().getSerializableExtra("info");
        String path = getIntent().getStringExtra("path");

        mTvTime.setText(Func.formatData("yyyy/MM/dd HH:mm:ss", mediaInfo.getLastModifyTime()));
        mTvSize.setText(mediaInfo.getStrSize());
        mTvMeasure.setText("尺寸:" + mediaInfo.getWidth() + "x" + mediaInfo.getHeight() + "(照片预览尺寸即恢复后尺寸)");

        Glide.with(this).load(this.mediaInfo.getPath()).into(mImImage);
        mImImage.setMaxZoom(10.0f);

        RxView.clicks(mTvRecover).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            File dir = new File(Environment.getExternalStorageDirectory() + "/" + path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File source = new File(mediaInfo.getPath());
            File dest = new File(dir.getAbsolutePath() + "/" + mediaInfo.getFileName());
            try {
                FileUtils.copyFile(source, dest);
                startActivity(new Intent(DetailImageActivty.this, RecoverImageActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.msg("照片恢复错误->" + e.getMessage());
            }
        });

        RxView.clicks(mTvRecovered).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            startActivity(new Intent(DetailImageActivty.this, RecoverImageActivity.class));
        });

        mImWaterMark.setVisibility(View.VISIBLE);
    }
}
