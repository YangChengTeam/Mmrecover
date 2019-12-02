package com.yc.mmrecover.controller.activitys;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.view.adapters.VerticalFileAdapter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.Serializable;

public class ShowFileActivity extends BaseShowActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_file_show;
    }

    @Override
    protected void initViews() {
        super.initViews();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(this.mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
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
            }
        });
    }

    @Override
    protected String initTitle() {
        return "文档";
    }

    @Override
    protected String initPath() {
        return "/数据恢复助手/微信文档恢复/";
    }

    @Override
    public BaseQuickAdapter initAdapter() {
        return new VerticalFileAdapter(null);
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
    protected void start2RecoverActivity() {
        startActivity(new Intent(this, RecoverFileActivity.class));
    }

}
