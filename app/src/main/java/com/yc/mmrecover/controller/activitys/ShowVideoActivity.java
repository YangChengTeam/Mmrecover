package com.yc.mmrecover.controller.activitys;

import androidx.recyclerview.widget.GridLayoutManager;

import com.yc.mmrecover.R;
import com.yc.mmrecover.eventbus.VideoEventBusMessage;
import com.yc.mmrecover.thread.ScanVideoService;
import com.yc.mmrecover.view.adapters.GridVideoAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        recyclerView.setLayoutManager(layoutManage);
        recyclerView.setAdapter(this.mAdapter);
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
            this.mAdapter.notifyDataSetChanged();
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
