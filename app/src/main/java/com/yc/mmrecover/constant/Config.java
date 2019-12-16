package com.yc.mmrecover.constant;

public class Config {

    public static boolean DEBUG = false;

    public static final String INIT_URL = getBaseUrl() + "index/init";
    public static final String HEAD_PATH = "head_path";
    public static final String USER_ID = "user_id";
    public static final String VIP_ITEMS = "vip_items";
    public static final String USER_INFO = "user_info";

    public static String getBaseUrl() {
//        String baseUrl = "http://wxgj.wuhanup.com/api/";
        String baseUrl = "http://uu.zhanyu22.com/api/";
        String debugBaseUrl = "http://wxgj.wuhanup.com/api/";
        return (DEBUG ? debugBaseUrl : baseUrl);
    }

    public static final String USER_URL = getBaseUrl().concat("v1.user/info");

    public static final String GUIDE_URL = getBaseUrl().concat("v1.info/backUpImg");

    public static final String SUGGEST_URL = getBaseUrl().concat("v1.suggest/addSuggest");

    public static final String UPLOAD_URL = getBaseUrl().concat("v1.suggest/upload");

    public static final String VIP_ITEM_URL = getBaseUrl().concat("v1.card/index");
}
