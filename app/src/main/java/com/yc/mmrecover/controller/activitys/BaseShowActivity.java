package com.yc.mmrecover.controller.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding3.view.RxView;
import com.kk.utils.LogUtil;
import com.kk.utils.ScreenUtil;
import com.kk.utils.TaskUtil;
import com.kk.utils.ToastUtil;
import com.kk.utils.VUiKit;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.utils.BackgroundShape;
import com.yc.mmrecover.utils.BeanUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
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

    protected boolean mIsScan;
    protected boolean mIsSelectAll;
    protected int mMaxProgress = 100;

    protected BaseQuickAdapter mAdapter;
    protected List<MediaInfo> mMediaList;

    @Override
    protected void initViews() {
        initActionBar();
        initProgressBar();
        mAdapter = initAdapter();

        RxView.clicks(mStartBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            if (!mIsScan) {
                scan();
            }
        });

        RxView.clicks(mTvRecover).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            if (mIsScan) return;
            mRlMask.setVisibility(View.VISIBLE);
            mTvMask.setText("正在恢复" + initTitle());
            File dir = new File(Environment.getExternalStorageDirectory() + "/" + initPath());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            boolean flag = false;
            for (MediaInfo mediaBean : BaseShowActivity.this.mMediaList) {
                if (mediaBean.isSelect()) {
                    File source = new File(mediaBean.getPath());
                    File dest = new File(dir.getAbsolutePath() + "/" + mediaBean.getFileName());
                    try {
                        FileUtils.copyFile(source, dest);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.msg(initTitle() + "恢复错误->" + e.getMessage());
                    }
                    mediaBean.setSelect(false);
                    flag = true;
                }
            }
            if (flag) {
                startActivity(new Intent(BaseShowActivity.this, RecoverVideoActivity.class));
            } else {
                ToastUtil.toast2(BaseShowActivity.this, "请选择要恢复的" + initTitle());
            }
            mRlMask.setVisibility(View.GONE);
        });

        RxView.clicks(mDelBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            if (mIsScan) return;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示");
            builder.setMessage("你确认删除选择的" + initTitle() + "吗？");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    mRlMask.setVisibility(View.VISIBLE);
                    mTvMask.setText("正在删除" + initTitle());
                    boolean flag = false;
                    for (int j = 0; j < mMediaList.size(); j++) {
                        MediaInfo mediaBean = mMediaList.get(j);
                        if (mediaBean.isSelect()) {
                            File source = new File(mediaBean.getPath());
                            FileUtils.deleteQuietly(source);
                            mMediaList.remove(mediaBean);
                            flag = true;
                        }
                    }
                    if (flag) {
                        ToastUtil.toast2(BaseShowActivity.this, initTitle() + "删除成功");
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.toast2(BaseShowActivity.this, "请选择要删除的" + initTitle());
                    }
                    mRlMask.setVisibility(View.GONE);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        });

        this.mTvRecover.setBackground(new BackgroundShape(this, 22, R.color.yellow_btn));
        this.mTvRecovered.setBackground(new BackgroundShape(this, 22, R.color.gray_button));
        this.mDelBtn.setBackground(new BackgroundShape(this, 22, R.color.red_word));

        this.mMediaList = new ArrayList<>();
        scan();
    }

    protected abstract String initTitle();

    protected abstract String initPath();

    public abstract BaseQuickAdapter initAdapter();


    protected void setTitle(String title) {
        mTvTitle.setText(title);
    }


    private void initActionBar() {
        mTvOperate.setText("全选");
        setTitle("全部" + initTitle());

        RxView.clicks(mBackBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            finish();
        });

        RxView.clicks(mTvOperate).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
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

    private void notifyDataSetChanged(MediaInfo mediaInfo) {
        if (mediaInfo != null) {
            this.mMediaList.add(mediaInfo);
            this.setTitle("全部" + initTitle() + "(" + this.mMediaList.size() + ")");
            this.mAdapter.setNewData(this.mMediaList);
            this.mAdapter.notifyDataSetChanged();
            mProgressBar.setProgress(this.mMediaList.size() % 100);
        } else {
            this.mIsScan = false;
            this.mRlMask.setVisibility(View.GONE);
            this.mTvStatus.setText("扫描完成");
            this.mStartBtn.setImageDrawable(getResources().getDrawable(R.mipmap.image_start));
            this.mProgressBar.setProgress(mMaxProgress);
        }
    }

    protected void scan() {
        this.mIsScan = true;
        this.mMediaList.clear();
        this.mAdapter.notifyDataSetChanged();
        this.mProgressBar.setProgress(0);
        this.mTvStatus.setText("扫描中");
        this.mRlMask.setVisibility(View.VISIBLE);
        this.mTvMask.setText(initTitle() + "扫描中");
        this.mStartBtn.setImageDrawable(getResources().getDrawable(R.mipmap.image_pause));

        TaskUtil.getImpl().runTask(() -> {
            scanDisk(Environment.getExternalStorageDirectory().getAbsolutePath() + "/tencent/MicroMsg");
            VUiKit.post(() -> {
                notifyDataSetChanged(null);
            });
        });
    }

    public abstract boolean filterExt(String path);

    public abstract MediaInfo getMediaInfo(File file);

    private void scanDisk(String str) {
        new File(str).listFiles(new FilenameFilter() {
            public boolean accept(File file, String str) {
                File file2 = new File(file, str);
                String absolutePath = file2.getAbsolutePath();
                if (file2.isDirectory()) {
                    scanDisk(absolutePath);
                } else {
                    if (filterExt(absolutePath)) {
                        MediaInfo mediaInfo = getMediaInfo(file2);
                        VUiKit.post(() -> {
                            if (mediaInfo.getFileName() != null) {
                                notifyDataSetChanged(mediaInfo);
                            }
                        });
                    }
                }
                return false;
            }
        });

    }


}
