package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.utils.GridSpacingItemDecoration;
import com.yc.mmrecover.view.adapters.GridVideoAdapter;

import java.io.Serializable;


public class RecoverImageActivity extends BaseRecoverActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_recover;
    }

    @Override
    public String initTitle() {
        return "图片";
    }

    @Override
    public String initPath() {
        return "数据恢复助手/微信图片恢复/";
    }

    @Override
    protected void initViews() {
        super.initViews();
        this.mAdapter = new GridVideoAdapter(this.mMediaList);
        GridLayoutManager layoutManage = new GridLayoutManager(this, 4);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
        recyclerView.setLayoutManager(layoutManage);
        recyclerView.setAdapter(this.mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(RecoverImageActivity.this, DetailImageActivty.class);
                intent.putExtra("info", (Serializable) (MediaInfo) adapter.getData().get(position));
                RecoverImageActivity.this.startActivity(intent);
            }
        });
    }
}
