package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.view.adapters.VerticalFileAdapter;

import java.io.File;

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
    }

    @Override
    protected String initTitle() {
        return "文件";
    }

    @Override
    protected String initPath() {
        return "/数据恢复助手/微信文件恢复/";
    }

    @Override
    public BaseQuickAdapter initAdapter() {
        return new VerticalFileAdapter(null);
    }

    @Override
    public boolean filterExt(String path) {
        String exts = "doc,docx,xls,txt,xlsx,ppt,pptx,xml";
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
    public MediaInfo getMediaInfo(File file2) {
        long length = file2.length();
        String absolutePath = file2.getAbsolutePath();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ScanDocService absolutePath = ");
        stringBuilder.append(absolutePath);
        stringBuilder.append(", ");
        stringBuilder.append(file2.getName());
        Log.d("ssssss", "getMediaInfo: " + stringBuilder.toString());
        MediaInfo mediaBean = new MediaInfo();
        mediaBean.setLastModifyTime((int) (file2.lastModified() / 1000));
        mediaBean.setPath(absolutePath);
        mediaBean.setSize(length);
        mediaBean.setStrSize(Func.getSizeString(length));
        mediaBean.setFileName(file2.getName());
        return mediaBean;
    }

    @Override
    protected void start2RecoverActivity() {
        startActivity(new Intent(this, RecoverFileActivity.class));
    }

}
