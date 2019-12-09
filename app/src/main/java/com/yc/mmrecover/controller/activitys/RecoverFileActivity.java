package com.yc.mmrecover.controller.activitys;

import com.yc.mmrecover.R;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.view.adapters.VerticalFileAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;


public class RecoverFileActivity extends BaseRecoverActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_file_recover;
    }


    @Override
    public String initTitle() {
        return "文档";
    }

    @Override
    public String initPath() {
        return "数据恢复助手/微信文档恢复/";
    }

    @Override
    protected void initViews() {
        super.initViews();


        this.mAdapter = new VerticalFileAdapter(this.mMediaList);
        ((VerticalFileAdapter) this.mAdapter).setShowSelect(false);
        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManage);
        recyclerView.setAdapter(this.mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> Func.openFile(RecoverFileActivity.this, mMediaList.get(position).getPath()));

    }


}
