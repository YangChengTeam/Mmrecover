package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding3.view.RxView;
import com.kk.utils.ScreenUtil;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.thread.ScanVideoService;
import com.yc.mmrecover.utils.BackgroundShape;
import com.yc.mmrecover.utils.BeanUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public abstract class BaseShowActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.im_back)
    ImageView mBackBtn;

    @BindView(R.id.tv_operation)
    TextView mTvOperate;

    @BindView(R.id.tv_recover)
    TextView mTvRecover;

    @BindView(R.id.tv_recovered)
    TextView mTvRecovered;

    @BindView(R.id.tv_del)
    TextView mDelBtn;

    @BindView(R.id.tv_mask)
    TextView mTvMask;

    @BindView(R.id.rl_mask)
    RelativeLayout mRlMask;

    @BindView(R.id.tv_status)
    TextView mTvStatus;

    @BindView(R.id.im_start)
    ImageView mStartBtn;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.gridView)
    RecyclerView recyclerView;

    private String title;

    protected boolean mIsScan;
    protected boolean mIsSelectAll;
    protected int mMaxProgress = 100;

    protected BaseQuickAdapter mAdapter;
    protected List<MediaInfo> mMediaList;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initViews() {
        initActionBar();
        initProgressBar();

        RxView.clicks(mStartBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            if (!mIsScan) {
                scan();
            }
        });

        RxView.clicks(mTvRecover).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {

        });

        RxView.clicks(mTvRecovered).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {

        });

        RxView.clicks(mDelBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {

        });

        this.mTvRecover.setBackgroundDrawable(new BackgroundShape(this, 22, R.color.yellow_btn));
        this.mTvRecovered.setBackgroundDrawable(new BackgroundShape(this, 22, R.color.gray_button));
        this.mDelBtn.setBackgroundDrawable(new BackgroundShape(this, 22, R.color.red_word));
        this.mMediaList = new ArrayList<>();
        initData();
        scan();
    }

    protected abstract void initData();

    protected void initTitle(String title) {
        this.title = title;
    }

    protected void setTitle(String title) {
        mTvTitle.setText(title);
    }

    private void initActionBar() {
        mTvOperate.setText("全选");
        setTitle("全部" + title);
        RxView.clicks(mBackBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            finish();
        });

        mTvOperate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!mIsScan) {
                    if (mIsSelectAll) {
                        mIsSelectAll = false;
                        mTvOperate.setText("全选");
                        for (MediaInfo select : mMediaList) {
                            select.setSelect(false);
                        }
                    } else {
                        mIsSelectAll = true;
                        mTvOperate.setText("取消全选");
                        for (MediaInfo select : mMediaList) {
                            select.setSelect(true);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initProgressBar() {
        this.mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        BeanUtils.setFieldValue(this.mProgressBar, "mOnlyIndeterminate", new Boolean(false));
        this.mProgressBar.setIndeterminate(false);
        float[] fArr = new float[]{(float) ScreenUtil.dip2px(this, 7.0f), (float) ScreenUtil.dip2px(this, 7.0f), (float) ScreenUtil.dip2px(this, 7.0f), (float) ScreenUtil.dip2px(this, 7.0f), (float) ScreenUtil.dip2px(this, 7.0f), (float) ScreenUtil.dip2px(this, 7.0f), (float) ScreenUtil.dip2px(this, 7.0f), (float) ScreenUtil.dip2px(this, 7.0f)};
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(fArr, null, null));
        shapeDrawable.getPaint().setColor(getResources().getColor(R.color.yellow_word));
        this.mProgressBar.setProgressDrawable(new ClipDrawable(shapeDrawable, 3, 1));
        shapeDrawable = new ShapeDrawable(new RoundRectShape(fArr, null, null));
        shapeDrawable.getPaint().setColor(getResources().getColor(R.color.yellow_bk));
        this.mProgressBar.setBackgroundDrawable(shapeDrawable);
        this.mProgressBar.setIndeterminateDrawable(getResources().getDrawable(17301613));
        this.mProgressBar.setMax(this.mMaxProgress);
        this.mProgressBar.setProgress(0);
    }

    protected void scan() {
        this.mIsScan = true;
        this.mMediaList.clear();
        this.mAdapter.notifyDataSetChanged();
        this.mProgressBar.setProgress(0);
        this.mTvStatus.setText("扫描中");
        this.mRlMask.setVisibility(View.VISIBLE);
        this.mTvMask.setText(title + "扫描中");
        this.mStartBtn.setImageDrawable(getResources().getDrawable(R.mipmap.image_pause));
        startService(new Intent(this, ScanVideoService.class));
    }

    protected void stop() {
        this.mIsScan = false;
        this.mRlMask.setVisibility(View.GONE);
        ScanVideoService.stopService();
        this.mTvStatus.setText("扫描完成");
        this.mStartBtn.setImageDrawable(getResources().getDrawable(R.mipmap.image_start));
        this.mProgressBar.setProgress(mMaxProgress);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
