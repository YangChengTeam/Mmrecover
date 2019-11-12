package com.yc.mmrecover.controller.activitys;

import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.yc.mmrecover.R;
import com.yc.mmrecover.eventbus.VideoEventBusMessage;
import com.yc.mmrecover.thread.ScanVideoService;
import com.yc.mmrecover.utils.BackgroundShape;
import com.yc.mmrecover.view.adapters.GridVideoAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class ShowVideoActivity extends BaseShowActivity {


    @BindView(R.id.tv_del)
    TextView mDelBtn;
//    @BindView(R.id.tv_mask)
//    TextView mTvMask;

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

        this.mDelBtn.setBackgroundDrawable(new BackgroundShape(this, 22, R.color.red_word));
    }

    @Override
    protected void scan() {
        super.scan();
//        this.mTvMask.setText(title + "扫描中");
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
