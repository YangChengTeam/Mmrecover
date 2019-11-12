package com.yc.mmrecover.model.bean;

import java.io.Serializable;

public class MediaInfo implements Serializable {
    private String fileName;
    private int height;
    private boolean isSelect;
    private int lastModifyTime;
    private String path;
    private long size;
    private String strSize;
    private int width;

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long j) {
        this.size = j;
    }

    public int getLastModifyTime() {
        return this.lastModifyTime;
    }

    public void setLastModifyTime(int i) {
        this.lastModifyTime = i;
    }

    public String getStrSize() {
        return this.strSize;
    }

    public void setStrSize(String str) {
        this.strSize = str;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean z) {
        this.isSelect = z;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

}
