package com.yc.mmrecover.controller.activitys;

import androidx.recyclerview.widget.GridLayoutManager;

import com.yc.mmrecover.R;
import com.yc.mmrecover.eventbus.VideoEventBusMessage;
import com.yc.mmrecover.view.adapters.GridVideoAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ShowVoiceActivity extends BaseShowActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_voice_show;
    }

    @Override
    protected void initViews() {
        this.initTitle("音频");
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

    }
}
