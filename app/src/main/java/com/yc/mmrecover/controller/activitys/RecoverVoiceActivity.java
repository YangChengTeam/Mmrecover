package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.helper.GridSpacingItemDecoration;
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

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(RecoverVoiceActivity.this, DetailVoiceActivity.class);
                intent.putExtra("info", (MediaInfo)adapter.getData().get(position));
                startActivity(intent);
            }
        });
    }
}
