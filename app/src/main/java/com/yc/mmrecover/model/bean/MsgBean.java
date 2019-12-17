package com.yc.mmrecover.model.bean;

/**
 * Created by suns  on 2019/12/16 15:14.
 */
public class MsgBean {
    public static final int TYPE_ACCOUNT = 103;
    public static final int TYPE_BAK_DIR = 102;
    public static final int TYPE_CHAT = 105;
    public static final int TYPE_CONTACT = 104;
    public static final int TYPE_COPY_TAR = 301;
    public static final int TYPE_DOC = 108;
    public static final int TYPE_END_ACCOUNT = 203;
    public static final int TYPE_END_BAK_DIR = 202;
    public static final int TYPE_END_CHAT = 205;
    public static final int TYPE_END_CONTACT = 204;
    public static final int TYPE_END_DOC = 208;
    public static final int TYPE_END_IMAGE = 201;
    public static final int TYPE_END_RELEASE = 200;
    public static final int TYPE_END_VIDEO = 206;
    public static final int TYPE_END_VOICE = 207;
    public static final int TYPE_FORMAT_FILE = 303;
    public static final int TYPE_IMAGE = 101;
    public static final int TYPE_RECOVERS_FILE = 304;
    public static final int TYPE_RELEASE_TAR = 302;
    public static final int TYPE_VIDEO = 106;
    public static final int TYPE_VOICE = 107;
    public String key;
    public Object msgInfo;
    public int type;

    public MsgBean(int i) {
        this.type = i;
    }

    public MsgBean(int i, Object obj) {
        this.type = i;
        this.msgInfo = obj;
    }

    public MsgBean(int i, String str, Object obj) {
        this.type = i;
        this.msgInfo = obj;
        this.key = str;
    }

}
