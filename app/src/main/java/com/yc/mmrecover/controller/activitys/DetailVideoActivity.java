package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;

import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;

import java.io.File;

import butterknife.BindView;

public class DetailVideoActivity extends BaseActivity {

    @BindView(R.id.video_view)
    VideoView mVideoView;

    private MediaInfo mediaInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initViews() {
        initTitle("视频播放");

        Intent intent = getIntent();
        mediaInfo = (MediaInfo) intent.getSerializableExtra("info");
        MediaController mediaController = new MediaController(this) {
            @Override
            public void show() {
                super.show(0);
            }
        };
        this.mVideoView.setVideoURI(Uri.fromFile(new File(mediaInfo.getPath())));
        this.mVideoView.setMediaController(mediaController);
        this.mVideoView.setOnPreparedListener(mediaPlayer -> {
            DetailVideoActivity.this.mVideoView.start();
            mediaController.show();
        });

        this.mVideoView.setOnCompletionListener(mp -> mediaController.show());
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mVideoView.stopPlayback();
    }
}
