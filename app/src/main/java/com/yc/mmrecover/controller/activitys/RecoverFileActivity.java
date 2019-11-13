package com.yc.mmrecover.controller.activitys;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.yc.mmrecover.R;
import com.yc.mmrecover.view.adapters.VerticalFileAdapter;


public class RecoverFileActivity extends BaseRecoverActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_file_recover;
    }


    @Override
    public String initTitle() {
        return "文件";
    }

    @Override
    public String initPath() {
        return "数据恢复助手/微信文件恢复/";
    }

    @Override
    protected void initViews() {
        super.initViews();
        this.mAdapter = new VerticalFileAdapter(this.mMediaList);
        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManage);
        recyclerView.setAdapter(this.mAdapter);
    }
}
