package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.helper.GridSpacingItemDecoration;
import com.yc.mmrecover.view.adapters.GridVideoAdapter;

import java.io.File;


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
        boolean z = false;
        if (TextUtils.isEmpty(path) || path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".gif") || path.endsWith(".txt") || path.endsWith(".db") || path.endsWith(".apk") || path.endsWith(".ppm") || path.endsWith(".zip") || path.endsWith(".slk") || path.endsWith(".json") || path.endsWith(".so") || path.endsWith(".js") || path.endsWith(".log") || path.endsWith(".html") || path.endsWith(".amr") || path.endsWith(".xlog") || path.endsWith(".xml")) {
            return false;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        if (options.outWidth <= 0 || options.outHeight <= 0 || options.outMimeType == null || !options.outMimeType.toLowerCase().contains("image")) {
            z = true;
        }
        return z;

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

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mIsOperate) {
                return;
            }
            MediaInfo mediaInfo = (MediaInfo) adapter.getData().get(position);
            if (mediaInfo.isSelect()) {
                view.findViewById(R.id.im_select).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.im_select).setVisibility(View.VISIBLE);
            }
            mediaInfo.setSelect(!mediaInfo.isSelect());
        });

        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            if (mIsOperate) {
                return false;
            }
            MediaInfo mediaInfo = (MediaInfo) adapter.getData().get(position);
            Intent intent = new Intent(ShowVideoActivity.this, DetailVideoActivity.class);
            intent.putExtra("info", mediaInfo);
            ShowVideoActivity.this.startActivity(intent);
            return false;
        });
    }

    @Override
    protected void start2RecoverActivity() {
        startActivity(new Intent(this, RecoverVideoActivity.class));
    }
}
