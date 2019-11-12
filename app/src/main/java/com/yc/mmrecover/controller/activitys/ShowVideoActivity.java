package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.view.View;

import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.mmrecover.R;
import com.yc.mmrecover.eventbus.VideoEventBusMessage;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.thread.ScanVideoService;
import com.yc.mmrecover.utils.GridSpacingItemDecoration;
import com.yc.mmrecover.utils.BackgroundShape;
import com.yc.mmrecover.view.adapters.GridVideoAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;


public class ShowVideoActivity extends BaseShowActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_show;
    }

    @Override
    protected void initViews() {
        this.initTitle("视频");

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
                Intent intent = new Intent(ShowVideoActivity.this, DetailVideoActivity.class);
                intent.putExtra("info", (Serializable) (MediaInfo) adapter.getData().get(position));
                ShowVideoActivity.this.startActivity(intent);
                return false;
            }
        });

    }


    @Override
    protected void initData() {
        this.mAdapter = new GridVideoAdapter(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(VideoEventBusMessage event) {
        if (event.getMediaInfo() != null) {
            this.mMediaList.add(event.getMediaInfo());
            this.setTitle("全部视频(" + this.mMediaList.size() + ")");
            this.mAdapter.setNewData(this.mMediaList);
            this.mAdapter.notifyDataSetChanged();
            if (this.mMediaList.size() < this.mMaxProgress) {
                this.mProgressBar.setProgress(this.mMediaList.size());
            }
        } else {
            stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScanVideoService.stopService();
    }
}
