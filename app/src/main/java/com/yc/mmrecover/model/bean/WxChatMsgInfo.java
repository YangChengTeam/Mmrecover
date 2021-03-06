package com.yc.mmrecover.model.bean;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.utils.UserInfoHelper;

public class WxChatMsgInfo implements MultiItemEntity {
    private static final int LOAD_NUM = 10;
    public static final int TYPE_FRIEND = 1;
    public static final int TYPE_ME = 0;
    public static final int TYPE_TITLE = 2;


    private String content;
    private int contentType;
    private String headPath;
    private String imgPath;
    private String name;
    private long time;
    private String tmpContent;
    private int type;
    private String uid;
    private String videoPath;
    private String voicePath;
    private int voiceSec;
    private boolean isSend;

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public String getHeadPath() {
        return this.headPath;
    }

    public void setHeadPath(String str) {
        this.headPath = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }


    public String getContent() {
        if (UserInfoHelper.getVipType() == 2) {
            return this.content;
        }
        if (TextUtils.isEmpty(this.tmpContent)) {
            this.tmpContent = Func.getMixString(this.content);
        }
        if (this.type == 2) {
            this.tmpContent = this.content;
        }
        return this.tmpContent;
    }

    public void setContent(String str) {
        this.content = str;
        this.tmpContent = null;
    }

    public int getContentType() {
        return this.contentType;
    }

    public void setContentType(int i) {
        this.contentType = i;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long i) {
        this.time = i;
    }

    public String getImgPath() {
        return this.imgPath;
    }

    public void setImgPath(String str) {
        this.imgPath = str;
    }

    public String getVideoPath() {
        return this.videoPath;
    }

    public void setVideoPath(String str) {
        this.videoPath = str;
    }

    public int getVoiceSec() {
        return this.voiceSec;
    }

    public void setVoiceSec(int i) {
        this.voiceSec = i;
    }

    public String getVoicePath() {
        return this.voicePath;
    }

    public void setVoicePath(String str) {
        this.voicePath = str;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String str) {
        this.uid = str;
    }

    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public String toString() {
        return "WxChatMsgInfo{" +
                "content='" + content + '\'' +
                ", contentType=" + contentType +
                ", headPath='" + headPath + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", tmpContent='" + tmpContent + '\'' +
                ", type=" + type +
                ", uid='" + uid + '\'' +
                ", videoPath='" + videoPath + '\'' +
                ", voicePath='" + voicePath + '\'' +
                ", voiceSec=" + voiceSec +
                ", isSend=" + isSend +
                '}';
    }
}
