package com.yc.mmrecover.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by suns  on 2019/12/14 10:08.
 */
public class UploadInfo implements MultiItemEntity {
    public static final int ITEM_CONTENT= 1;
    public static final int ITEM_CASE=2;

    private String path;
    private String url;
    private int type =ITEM_CASE;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UploadInfo() {
    }

    public UploadInfo(int type) {
        this.type = type;
    }

    public UploadInfo(String url, int type) {
        this.url = url;
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
