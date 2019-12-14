package com.yc.mmrecover.controller.activitys;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.fulongbin.decoder.Silk;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;

import java.io.File;

import butterknife.BindView;

public class DetailVoiceActivity extends BaseActivity {

    @BindView(R.id.video_view)
    VideoView mVideoView;

    private MediaInfo mediaInfo;
    private MediaController mediaController;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initViews() {
        initTitle("音频播放");

        Intent intent = getIntent();
        mediaInfo = (MediaInfo) intent.getSerializableExtra("info");

        MediaController mediaController = new MediaController(this) {
            @Override
            public void show() {
                super.show(0);
            }
        };
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/数据恢复助手/微信音频解码恢复/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        path = dir.getAbsolutePath() + "/" + mediaInfo.getFileName() + ".mp3";
//        Log.e("TAG", "initViews: " + mediaInfo.getFileName() + "--path--" + path);
        if (new File(path).exists() || Silk.convertSilkToMp3(mediaInfo.getPath(), path)) {
            this.mVideoView.setVideoURI(Uri.fromFile(new File(path)));
            this.mVideoView.setMediaController(mediaController);
            this.mVideoView.setOnPreparedListener(mediaPlayer -> {
                DetailVoiceActivity.this.mVideoView.start();
                mediaController.show();
            });

            this.mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaController.show();
                }
            });
        } else {
            ToastUtil.toast2(this, "音频转换失败");
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (this.mVideoView != null) {
            this.mVideoView.pause();
        }
    }


}
