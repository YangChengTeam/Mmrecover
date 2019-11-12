package com.yc.mmrecover.eventbus;

import com.yc.mmrecover.model.bean.MediaInfo;

public class VideoEventBusMessage {
    public VideoEventBusMessage() {

    }

    public VideoEventBusMessage(MediaInfo mediaInfo) {
        this.mediaInfo = mediaInfo;
    }

    private MediaInfo mediaInfo;

    public MediaInfo getMediaInfo() {
        return mediaInfo;
    }

    public void setMediaInfo(MediaInfo mediaInfo) {
        this.mediaInfo = mediaInfo;
    }
}
