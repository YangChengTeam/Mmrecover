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

        MediaController mediaController = new MediaController(this) {
            @Override
            public void show() {
                super.show(0);
            }
        };
        this.mVideoView.setMediaController(mediaController);
        this.mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                DetailVideoActivity.this.mVideoView.start();
                mediaController.show();
            }
        });

        this.mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaController.show();
            }
        });

    }
}
