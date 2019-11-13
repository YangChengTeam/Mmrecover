package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.graphics.BitmapFactory;
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

public class ShowImageActivity extends BaseShowActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_show;
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
                Intent intent = new Intent(ShowImageActivity.this, DetailImageActivty.class);
                intent.putExtra("info", (Serializable) (MediaInfo) adapter.getData().get(position));
                intent.putExtra("path", initPath());
                ShowImageActivity.this.startActivity(intent);
                return false;
            }
        });

    }

    @Override
    protected void start2RecoverActivity() {
        startActivity(new Intent(this, RecoverImageActivity.class));
    }

    @Override
    protected String initTitle() {
        return "图片";
    }

    @Override
    protected String initPath() {
        return "/数据恢复助手/微信图片恢复/";
    }

    @Override
    public BaseQuickAdapter initAdapter() {
        return new GridVideoAdapter(null);
    }

    @Override
    public boolean filterExt(String path) {
        return true;
    }

    @Override
    public MediaInfo getMediaInfo(File file) {
        MediaInfo mediaBean = new MediaInfo();
        String absolutePath = file.getAbsolutePath();
        long length = file.length();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(absolutePath, options);
        if (options.outWidth > 0 && options.outHeight > 0 && options.outMimeType != null && options.outMimeType.toLowerCase().contains("image") && !options.outMimeType.toLowerCase().contains("webp")) {
            mediaBean.setLastModifyTime((int) (file.lastModified() / 1000));
            mediaBean.setPath(absolutePath);
            mediaBean.setSize(length);
            mediaBean.setStrSize(Func.getSizeString(length));
            mediaBean.setWidth(options.outWidth);
            mediaBean.setHeight(options.outHeight);
            mediaBean.setFileName(file.getName());
        }
        return mediaBean;
    }
}
