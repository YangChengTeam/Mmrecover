package com.yc.mmrecover.controller.activitys;

import androidx.recyclerview.widget.GridLayoutManager;

import com.yc.mmrecover.R;
import com.yc.mmrecover.utils.GridSpacingItemDecoration;
import com.yc.mmrecover.view.adapters.GridVoiceAdapter;


public class RecoverVoiceActivity extends BaseRecoverActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_voice_recover;
    }

    @Override
    public String initTitle() {
        return "音频";
    }

    @Override
    public String initPath() {
        return "数据恢复助手/微信音频恢复/";
    }

    @Override
    protected void initViews() {
        super.initViews();
        this.mAdapter = new GridVoiceAdapter(this.mMediaList);
        GridLayoutManager layoutManage = new GridLayoutManager(this, 4);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
        recyclerView.setLayoutManager(layoutManage);
        recyclerView.setAdapter(this.mAdapter);
    }
}
