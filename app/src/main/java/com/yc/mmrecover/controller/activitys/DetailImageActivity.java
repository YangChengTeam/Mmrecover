package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.GlobalData;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.view.wdiget.BackgroundShape;
import com.yc.mmrecover.view.wdiget.GestureImageView;

import java.io.File;

import butterknife.BindView;

/**
 * Created by suns  on 2019/12/7 09:13.
 */
public class DetailImageActivity extends BaseActivity {

    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.tv_measure)
    TextView tvMeasure;
    @BindView(R.id.im_image)
    GestureImageView imImage;
    @BindView(R.id.im_water_mark)
    ImageView imWaterMark;
    @BindView(R.id.tv_recover)
    TextView tvRecover;
    @BindView(R.id.tv_recovered)
    TextView tvRecovered;
    private MediaInfo mediaInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_detail;
    }

    @Override
    protected void initViews() {
        initTitle("照片详情");

        this.mediaInfo = (MediaInfo) getIntent().getSerializableExtra("image_info");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("---path = ");
        stringBuilder.append(this.mediaInfo.getPath());
        Log.d("TAG", stringBuilder.toString());


        stringBuilder = new StringBuilder();
        stringBuilder.append("时间:");
        stringBuilder.append(Func.formatData("yyyy/MM/dd HH:mm:ss", (long) this.mediaInfo.getLastModifyTime()));
        tvTime.setText(stringBuilder.toString());

        stringBuilder = new StringBuilder();
        stringBuilder.append("大小:");
        stringBuilder.append(this.mediaInfo.getStrSize());
        tvSize.setText(stringBuilder.toString());

        stringBuilder = new StringBuilder();
        stringBuilder.append("尺寸:");
        stringBuilder.append(this.mediaInfo.getWidth());
        stringBuilder.append("x");
        stringBuilder.append(this.mediaInfo.getHeight());
        stringBuilder.append("(照片预览尺寸即恢复后尺寸)");
        tvMeasure.setText(stringBuilder.toString());

        Glide.with(this).load(this.mediaInfo.getPath()).into(imImage);
        imImage.setMaxZoom(10.0f);

        tvRecover.setBackgroundDrawable(new BackgroundShape(this, 22, R.color.yellow_btn));
        tvRecover.setOnClickListener(view -> {
            if (GlobalData.vipType == 1) {
                Intent intent = new Intent(DetailImageActivity.this, PayActivity.class);
                intent.putExtra("sta_type", 4003);
                DetailImageActivity.this.startActivity(intent);
                DetailImageActivity.this.finish();
                return;
            }
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append(Environment.getExternalStorageDirectory());
            stringBuilder1.append("/数据恢复管家/微信图片恢复/");
            String stringBuilder2 = stringBuilder1.toString();
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(Environment.getExternalStorageDirectory().getPath());
            stringBuilder3.append("/DCIM/Camera/");
            String stringBuilder4 = stringBuilder3.toString();
            File file = new File(stringBuilder2);
            if (!file.exists()) {
                file.mkdirs();
            }
            StringBuilder stringBuilder5 = new StringBuilder();
            stringBuilder5.append(stringBuilder2);
            stringBuilder5.append(DetailImageActivity.this.mediaInfo.getFileName());
            stringBuilder2 = stringBuilder5.toString();
            Func.copyFile(DetailImageActivity.this.mediaInfo.getPath(), stringBuilder2);
            stringBuilder5 = new StringBuilder();
            stringBuilder5.append(stringBuilder4);
            stringBuilder5.append(DetailImageActivity.this.mediaInfo.getFileName());
            stringBuilder4 = stringBuilder5.toString();
            Func.copyFile(DetailImageActivity.this.mediaInfo.getPath(), stringBuilder4);
            if (!(stringBuilder2.endsWith(".png") || stringBuilder2.endsWith(".jpg"))) {
                file = new File(stringBuilder2);
                StringBuilder stringBuilder6 = new StringBuilder();
                stringBuilder6.append(stringBuilder2);
                stringBuilder6.append(".jpg");
                file.renameTo(new File(stringBuilder6.toString()));
            }
            if (!(stringBuilder4.endsWith(".png") || stringBuilder4.endsWith(".jpg"))) {
                File file2 = new File(stringBuilder4);
                StringBuilder stringBuilder7 = new StringBuilder();
                stringBuilder7.append(stringBuilder4);
                stringBuilder7.append(".jpg");
                file2.renameTo(new File(stringBuilder7.toString()));
            }
            DetailImageActivity.this.startActivity(new Intent(DetailImageActivity.this, RecoverImageActivity.class));
        });

        tvRecovered.setBackgroundDrawable(new BackgroundShape(this, 22, R.color.gray_button2));
        tvRecovered.setOnClickListener(view -> {
            if (GlobalData.vipType == 1) {
                Intent intent = new Intent(DetailImageActivity.this, PayActivity.class);
                intent.putExtra("sta_type", 4003);
                DetailImageActivity.this.startActivity(intent);
                DetailImageActivity.this.finish();
                return;
            }
            DetailImageActivity.this.startActivity(new Intent(DetailImageActivity.this, RecoverImageActivity.class));
        });
        if (GlobalData.vipType != 1) {
            imWaterMark.setVisibility(View.GONE);
        }
    }


}
