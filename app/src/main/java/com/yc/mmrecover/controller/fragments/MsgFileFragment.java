package com.yc.mmrecover.controller.fragments;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.kk.utils.LogUtil;
import com.kk.utils.TaskUtil;
import com.kk.utils.ToastUtil;
import com.kk.utils.VUiKit;
import com.yc.mmrecover.R;
import com.yc.mmrecover.controller.activitys.BaseShowActivity;
import com.yc.mmrecover.controller.activitys.RecoverFileActivity;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.utils.UserInfoHelper;
import com.yc.mmrecover.view.adapters.VerticalFileAdapter;
import com.yc.mmrecover.view.wdiget.BackgroundShape;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Created by suns  on 2019/12/4 11:51.
 */
public class MsgFileFragment extends BaseScanFragment {
    @BindView(R.id.gridView)
    RecyclerView recyclerView;
    @BindView(R.id.rl_mask)
    RelativeLayout rlMask;
    @BindView(R.id.tv_mask)
    TextView mTvMask;
    @BindView(R.id.tv_recover)
    TextView tvRecover;
    @BindView(R.id.tv_recovered)
    TextView tvRecovered;
    private boolean mIsOperate;
    private List<MediaInfo> mMediaList;
    private VerticalFileAdapter mAdapter;
    private List<MediaInfo> selectList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_file;
    }

    @Override
    protected void initViews() {

        this.tvRecover.setBackground(new BackgroundShape(getActivity(), 22, R.color.yellow_btn));
        this.tvRecovered.setBackground(new BackgroundShape(getActivity(), 22, R.color.gray_button));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new VerticalFileAdapter(null);

        recyclerView.setAdapter(mAdapter);

        mMediaList = new ArrayList<>();
        selectList = new ArrayList<>();
        initData();
        initListener();
    }

    private void initListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mIsOperate) {
                return;
            }
            MediaInfo mediaInfo = (MediaInfo) adapter.getData().get(position);
            if (mediaInfo.isSelect()) {
                view.findViewById(R.id.im_select).setVisibility(View.GONE);
                selectList.remove(mediaInfo);
            } else {
                view.findViewById(R.id.im_select).setVisibility(View.VISIBLE);
                selectList.add(mediaInfo);
            }
            mediaInfo.setSelect(!mediaInfo.isSelect());
        });

        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            start2RecoverActivity();

            return false;
        });


        RxView.clicks(tvRecover).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {

            if (!UserInfoHelper.gotoVip(getActivity())) {


                if (mIsOperate) return;

                mIsOperate = true;

                rlMask.setVisibility(View.VISIBLE);
                mTvMask.setText("微信文件扫描中");

                TaskUtil.getImpl().runTask(() -> {
                    File dir = new File(Environment.getExternalStorageDirectory() + "/数据恢复助手/微信文档恢复/");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    rlMask.setTag(false);
                    for (MediaInfo mediaBean : this.mMediaList) {
                        if (mediaBean.isSelect()) {
                            File source = new File(mediaBean.getPath());
                            File dest = new File(dir.getAbsolutePath() + "/" + mediaBean.getFileName());
                            try {
                                FileUtils.copyFile(source, dest);
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtil.msg("微信文件恢复错误->" + e.getMessage());
                            }
                            mediaBean.setSelect(false);
                            rlMask.setTag(true);
                        }
                    }

                    VUiKit.post(() -> {
                        mIsOperate = false;
                        if (Boolean.parseBoolean(rlMask.getTag() + "")) {
                            mAdapter.notifyDataSetChanged();
                            start2RecoverActivity();
                        } else {
                            ToastUtil.toast2(getActivity(), "请选择要恢复的微信文件");
                        }
                        rlMask.setVisibility(View.GONE);

                    });

                });
            }
        });

        RxView.clicks(tvRecovered).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {

            start2RecoverActivity();
        });
    }

    private void start2RecoverActivity() {
        if (!UserInfoHelper.gotoVip(getActivity())) {
            Intent intent = new Intent(getActivity(), RecoverFileActivity.class);

            startActivity(intent);
        }
    }

    private void initData() {
        this.mIsOperate = true;
        this.mMediaList.clear();
        this.mAdapter.notifyDataSetChanged();
        rlMask.setVisibility(View.VISIBLE);
        this.mTvMask.setText("微信文件扫描中");
        TaskUtil.getImpl().runTask(() -> {
            scanDisk();
            VUiKit.post(() -> notifyDataSetChanged(null));

        });

    }

    @Override
    public boolean filterExt(String path) {
        String exts = "doc,xls,txt,ppt,xml,pdf";
        boolean flag = false;
        for (String ext : exts.split(",")) {
            if (path.contains("." + ext)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public MediaInfo getMediaInfo(File file) {
        long length = file.length();
        String absolutePath = file.getAbsolutePath();
        MediaInfo mediaBean = new MediaInfo();
        mediaBean.setLastModifyTime((int) (file.lastModified() / 1000));
        mediaBean.setPath(absolutePath);
        mediaBean.setSize(length);
        mediaBean.setStrSize(Func.getSizeString(length));
        mediaBean.setFileName(file.getName());
        return mediaBean;
    }

    @Override
    public void notifyDataSetChanged(MediaInfo mediaInfo) {
        if (mediaInfo != null) {
            this.mMediaList.add(mediaInfo);

            this.mAdapter.setNewData(this.mMediaList);
            this.mAdapter.notifyDataSetChanged();

        } else {
            this.mIsOperate = false;
            this.rlMask.setVisibility(View.GONE);
        }
    }
}
