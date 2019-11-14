package com.yc.mmrecover.controller.activitys;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.MediaController;
import android.widget.VideoView;

import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;

import butterknife.BindView;

public class DetailVideoActivity extends BaseActivity {

    @BindView(R.id.video_view)
    VideoView mVideoView;

    public static void startDetailVideoActivity(Context context, String path, String title) {
        Intent intent = new Intent(context, DetailVideoActivity.class);
        intent.putExtra("path", path);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        String title = intent.getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            title = "播放";
        }
        initTitle(title);

        MediaController mediaController = new MediaController(this) {
            @Override
            public void show() {
                super.show(0);
            }
        };


        this.mVideoView.setVideoURI(Uri.parse(path));
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
