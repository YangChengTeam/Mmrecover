package com.yc.mmrecover.model.bean;

import java.util.List;

/**
 * Created by suns  on 2019/12/14 09:13.
 */
public class IndexInfo {

    private UserInfo userInfo;//用户信息
    private ContactInfo kf; //联系客服
//    private int userCard; //用户拥有的会员卡id
    private VipItemInfo userLevel;//


    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public ContactInfo getKf() {
        return kf;
    }

    public void setKf(ContactInfo kf) {
        this.kf = kf;
    }

    public VipItemInfo getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(VipItemInfo userLevel) {
        this.userLevel = userLevel;
    }
}
