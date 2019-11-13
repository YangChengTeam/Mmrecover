package com.yc.mmrecover.controller.activitys;

import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.view.adapters.GridVideoAdapter;

import java.io.File;

public class ShowFileActivity extends BaseShowActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_file_show;
    }

    @Override
    protected void initViews() {
        this.initTitle("文件");
        super.initViews();
        GridLayoutManager layoutManage = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManage);
        recyclerView.setAdapter(this.mAdapter);
    }

    @Override
    protected String initTitle() {
        return "文件";
    }

    @Override
    protected String initPath() {
        return null;
    }

    @Override
    public BaseQuickAdapter initAdapter() {
        return new GridVideoAdapter(null);
    }

    @Override
    public boolean filterExt(String path) {
        return false;
    }

    @Override
    public MediaInfo getMediaInfo(File file) {
        return null;
    }

    @Override
    protected void start2RecoverActivity() {

    }

}
