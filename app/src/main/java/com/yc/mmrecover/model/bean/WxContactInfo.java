package com.yc.mmrecover.model.bean;

import android.text.TextUtils;

import com.yc.mmrecover.utils.Func;

public class WxContactInfo {
    private int contactType;
    private String content;
    private String headPath;
    private int lastTime;
    private String markName;
    private String name;
    private String phone;
    private String quanPin;
    private String tmpContent;
    private String uid;
    private String wxId;

    public int getLastTime() {
        return this.lastTime;
    }

    public void setLastTime(int i) {
        this.lastTime = i;
    }

    public String getContent() {
        if (GlobalData.vipType == 3) {
            return this.content;
        }
        if (TextUtils.isEmpty(this.tmpContent) && !TextUtils.isEmpty(this.content)) {
            this.tmpContent = Func.getMixString(this.content);
        }
        return this.tmpContent;
    }

    public void setContent(String str) {
        this.content = str;
        this.tmpContent = null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getHeadPath() {
        return this.headPath;
    }

    public void setHeadPath(String str) {
        this.headPath = str;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String str) {
        this.uid = str;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public String getMarkName() {
        return this.markName;
    }

    public void setMarkName(String str) {
        this.markName = str;
    }

    public String getWxId() {
        return this.wxId;
    }

    public void setWxId(String str) {
        this.wxId = str;
    }

    public int getContactType() {
        return this.contactType;
    }

    public void setContactType(int i) {
        this.contactType = i;
    }

    public String getQuanPin() {
        return this.quanPin;
    }

    public void setQuanPin(String str) {
        this.quanPin = str;
    }

}
