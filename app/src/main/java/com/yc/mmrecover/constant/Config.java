package com.yc.mmrecover.constant;

public class Config {
    public static boolean DEBUG = false;

    public static final String INIT_URL = getBaseUrl() + "index/init";

    public static String getBaseUrl() {
        String baseUrl = "http://tic.upkao.com/api/";
        String debugBaseUrl = "http://120.76.202.236:1980/api/";
        return (DEBUG ? debugBaseUrl : baseUrl);
    }

}
