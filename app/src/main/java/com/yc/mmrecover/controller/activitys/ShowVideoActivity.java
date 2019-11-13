package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding3.view.RxView;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.utils.GridSpacingItemDecoration;
import com.yc.mmrecover.view.adapters.GridVideoAdapter;

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;


public class ShowVideoActivity extends BaseShowActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_show;
    }

    @Override
    protected String initTitle() {
        return "视频";
    }

    @Override
    protected String initPath() {
        return "/数据恢复助手/微信视频恢复/";
    }

    @Override
    public BaseQuickAdapter initAdapter() {
        return new GridVideoAdapter(null);
    }

    @Override
    public boolean filterExt(String path) {
        String exts = "3gp,mp4,wmv,asf,rm,rmvb,mov,avi,dat,mpg,mpeg";
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
        MediaInfo mediaBean = new MediaInfo();
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            String absolutePath = file.getAbsolutePath();
            long length = file.length();
            mediaMetadataRetriever.setDataSource(absolutePath);
            Object extractMetadata = mediaMetadataRetriever.extractMetadata(19);
            Object extractMetadata2 = mediaMetadataRetriever.extractMetadata(18);
            Object extractMetadata3 = mediaMetadataRetriever.extractMetadata(9);
            int parseInt = TextUtils.isEmpty(extractMetadata + "") ? 0 : Integer.parseInt(extractMetadata + "");
            int parseInt2 = TextUtils.isEmpty(extractMetadata2 + "") ? 0 : Integer.parseInt(extractMetadata2 + "");
            if ((TextUtils.isEmpty(extractMetadata3 + "") ? 0 : Integer.parseInt(extractMetadata3 + "")) > 1000 && parseInt2 > 0 && parseInt > 0) {
                mediaBean.setLastModifyTime((int) (file.lastModified() / 1000));
                mediaBean.setPath(absolutePath);
                mediaBean.setSize(length);
                mediaBean.setStrSize(Func.getSizeString(length));
                mediaBean.setWidth(parseInt2);
                mediaBean.setHeight(parseInt);
                mediaBean.setFileName(file.getName());
            }
            mediaMetadataRetriever.release();
        } catch (Exception unused) {
            mediaMetadataRetriever.release();
        }
        return mediaBean;
    }

    @Override
    protected void initViews() {
        super.initViews();
        GridLayoutManager layoutManage = new GridLayoutManager(this, 4);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
        recyclerView.setLayoutManager(layoutManage);
        recyclerView.setAdapter(this.mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mIsScan) {
                    return;
                }
                MediaInfo mediaInfo = (MediaInfo) adapter.getData().get(position);
                if (mediaInfo.isSelect()) {
                    view.findViewById(R.id.im_select).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.im_select).setVisibility(View.VISIBLE);
                }
                mediaInfo.setSelect(!mediaInfo.isSelect());
            }
        });

        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (mIsScan) {
                    return false;
                }
                Intent intent = new Intent(ShowVideoActivity.this, DetailVideoActivity.class);
                intent.putExtra("info", (Serializable) (MediaInfo) adapter.getData().get(position));
                ShowVideoActivity.this.startActivity(intent);
                return false;
            }
        });

        RxView.clicks(mTvRecovered).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            startActivity(new Intent(ShowVideoActivity.this, RecoverVideoActivity.class));
        });
    }
}
