package com.yc.mmrecover.model.bean;

public class WxAccountInfo {
    private String headPath;
    private String nickName;
    private String parent;
    private String phone;
    private String wxAccount;
    private String wxId;

    public String getHeadPath() {
        return this.headPath;
    }

    public void setHeadPath(String str) {
        this.headPath = str;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String str) {
        this.nickName = str;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public String getWxAccount() {
        return this.wxAccount;
    }

    public void setWxAccount(String str) {
        this.wxAccount = str;
    }

    public String getParent() {
        return this.parent;
    }

    public void setParent(String str) {
        this.parent = str;
    }

    public String getWxId() {
        return this.wxId;
    }

    public void setWxId(String str) {
        this.wxId = str;
    }

}
