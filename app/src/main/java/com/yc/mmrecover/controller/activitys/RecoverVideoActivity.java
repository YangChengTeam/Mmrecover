package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.helper.GridSpacingItemDecoration;
import com.yc.mmrecover.view.adapters.GridVideoAdapter;

public class RecoverVideoActivity extends BaseRecoverActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_recover;
    }

    @Override
    public String initTitle() {
        return "视频";
    }

    @Override
    public String initPath() {
        return "数据恢复助手/微信视频恢复/";
    }

    @Override
    protected void initViews() {
        super.initViews();
        this.mAdapter = new GridVideoAdapter(this.mMediaList);
        GridLayoutManager layoutManage = new GridLayoutManager(this, 4);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
        recyclerView.setLayoutManager(layoutManage);
        recyclerView.setAdapter(this.mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            MediaInfo mediaInfo = (MediaInfo) adapter.getData().get(position);
            Intent intent = new Intent(RecoverVideoActivity.this, DetailVideoActivity.class);
            intent.putExtra("info",  mediaInfo);
            RecoverVideoActivity.this.startActivity(intent);

        });
    }
}
