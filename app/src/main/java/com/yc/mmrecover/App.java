package com.yc.mmrecover;

import android.content.pm.ApplicationInfo;
import android.os.Build;

import androidx.multidex.MultiDexApplication;

import com.alibaba.fastjson.JSON;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.FileUtil;
import com.kk.utils.LogUtil;
import com.kk.utils.TaskUtil;
import com.umeng.commonsdk.UMConfigure;
import com.yc.mmrecover.model.bean.ChannelInfo;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class App extends MultiDexApplication {

    private static App app;

    public static App getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        inits();
    }

    private void inits() {
        TaskUtil.getImpl().runTask(() -> {
            initHttpInfo();
            SQLiteDatabase.loadLibs(getApplicationContext());
        });
    }

    private void initHttpInfo() {
        ApplicationInfo appinfo = getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zf = null;
        ChannelInfo channelInfo = null;
        try {
            zf = new ZipFile(sourceDir);
            ZipEntry ze = zf.getEntry("META-INF/channelconfig.json");
            InputStream in = zf.getInputStream(ze);
            String result = FileUtil.readString(in);
            channelInfo = JSON.parseObject(result, ChannelInfo.class);
            LogUtil.msg("渠道来源->" + result);
        } catch (Exception e) {
            LogUtil.msg("apk中channelconfig.json文件不存在", LogUtil.W);
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
            }
        }
        GoagalInfo.get().init(getApplicationContext());
        HttpConfig.setPublickey("-----BEGIN PUBLIC KEY-----\n" +
                "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA5KaI8l7xplShIEB0Pwgm\n" +
                "MRX/3uGG9BDLPN6wbMmkkO7H1mIOXWB/Jdcl4/IMEuUDvUQyv3P+erJwZ1rvNsto\n" +
                "hXdhp2G7IqOzH6d3bj3Z6vBvsXP1ee1SgqUNrjX2dn02hMJ2Swt4ry3n3wEWusaW\n" +
                "mev4CSteSKGHhBn5j2Z5B+CBOqPzKPp2Hh23jnIH8LSbXmW0q85a851BPwmgGEan\n" +
                "5HBPq04QUjo6SQsW/7dLaaAXfUTYETe0HnpLaimcHl741ftGyrQvpkmqF93WiZZX\n" +
                "wlcDHSprf8yW0L0KA5jIwq7qBeu/H/H5vm6yVD5zvUIsD7htX0tIcXeMVAmMXFLX\n" +
                "35duvYDpTYgO+DsMgk2Q666j6OcEDVWNBDqGHc+uPvYzVF6wb3w3qbsqTnD0qb/p\n" +
                "WxpEdgK2BMVz+IPwdP6hDsDRc67LVftYqHJLKAfQt5T6uRImDizGzhhfIfJwGQxI\n" +
                "7TeJq0xWIwB+KDUbFPfTcq0RkaJ2C5cKIx08c7lYhrsPXbW+J/W4M5ZErbwcdj12\n" +
                "hrfV8TPx/RgpJcq82otrNthI3f4QdG4POUhdgSx4TvoGMTk6CnrJwALqkGl8OTfP\n" +
                "KojOucENSxcA4ERtBw4It8/X39Mk0aqa8/YBDSDDjb+gCu/Em4yYvrattNebBC1z\n" +
                "ulK9uJIXxVPi5tNd7KlwLRMCAwEAAQ==\n" +
                "-----END PUBLIC KEY-----");

        // 设置http默认参数
        String agent_id = "1";
        Map<String, String> params = new HashMap<>();
        if (GoagalInfo.get().channelInfo != null && GoagalInfo.get().channelInfo.agent_id != null) {
            params.put("from_id", GoagalInfo.get().channelInfo.from_id + "");
            params.put("author", GoagalInfo.get().channelInfo.author + "");
            agent_id = GoagalInfo.get().channelInfo.agent_id;
        }
        params.put("agent_id", agent_id);
        params.put("ts", System.currentTimeMillis() + "");
        params.put("device_type", "android");
        params.put("app_type", "app");
        params.put("app_id", "16");
        params.put("imeil", GoagalInfo.get().uuid);
        String sv = Build.MODEL.contains(Build.BRAND) ? Build.MODEL + " " + Build.VERSION.RELEASE : Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.RELEASE;
        params.put("sv", sv);
        if (channelInfo != null) {
            params.put("site_id", channelInfo.getSite_id() + "");
            params.put("soft_id", channelInfo.getSoft_id() + "");
            params.put("referer_url", channelInfo.getNode_url() + "");
        }
        if (GoagalInfo.get().packageInfo != null) {
            params.put("app_version", GoagalInfo.get().packageInfo.versionCode + "");
        }
        HttpConfig.setDefaultParams(params);

        // 友盟统计
        UMConfigure.init(this, "5dc8f6cc3fc1958070000d85", "WebStore" + agent_id, UMConfigure.DEVICE_TYPE_PHONE, "");
    }

}
