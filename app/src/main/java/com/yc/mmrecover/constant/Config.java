package com.yc.mmrecover.constant;

public class Config {
    public static boolean DEBUG = false;

    public static final String INIT_URL = getBaseUrl() + "index/init";

    public static String getBaseUrl() {
        String baseUrl = "http://wxgj.wuhanup.com/api/";
        String debugBaseUrl = "http://wxgj.wuhanup.com/api/";
        return (DEBUG ? debugBaseUrl : baseUrl);
    }

}
