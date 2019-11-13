package com.yc.mmrecover.controller.activitys;

import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;

import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;

import butterknife.BindView;

public class DetailVideoActivity extends BaseActivity {
    private MediaInfo mediaInfo;
    
    @BindView(R.id.video_view)
    VideoView mVideoView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initViews() {
        initTitle("视频播放");
        this.mediaInfo = (MediaInfo) getIntent().getSerializableExtra("info");
        this.mVideoView.setVideoURI(Uri.parse(this.mediaInfo.getPath()));
        this.mVideoView.setMediaController(new MediaController(this));
        this.mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                DetailVideoActivity.this.mVideoView.start();
            }
        });
    }
}
