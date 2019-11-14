package com.yc.mmrecover.controller.activitys;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

/**
 * 用于API23 Android6.0以上权限校验的Activity基类
 * 校验时只校验必须权限，请求时请求全部权限
 */
public abstract class BasePermissionActivity extends BaseActivity {

    private String TAG = "GameSdkLog";

    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @TargetApi(23)
    public void checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            onRequestPermissionSuccess();
            return;
        }
        if (checkMustPermissions(this)) {  //权限都有了
            onRequestPermissionSuccess();
            return;
        }
        List<String> mustPermissions = getMustPermissions();
        // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
        String[] requestPermissions = new String[mustPermissions.size()];
        mustPermissions.toArray(requestPermissions);
        requestPermissions(requestPermissions, 1024);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && checkMustPermissions(this)) {
            onRequestPermissionSuccess();
        } else {
            onRequestPermissionError();
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

    /**
     * 校验必须权限是否授权
     */
    @TargetApi(23)
    private boolean checkMustPermissions(Context context) {
        List<String> mustPermissions = getMustPermissions();
        if (mustPermissions == null || mustPermissions.size() == 0) {
            return true;
        }
        List<String> lackedPermission = new ArrayList<>();
        for (String permissions : mustPermissions) {
            if (!(context.checkSelfPermission(permissions) == PackageManager.PERMISSION_GRANTED)) {
                lackedPermission.add(permissions);
            }
        }
        if (0 == lackedPermission.size()) {
            return true;
        }
        return false;
    }

    /**
     * 需要请求的权限
     */
    protected abstract List<String> getMustPermissions();

    /**
     * 权限请求失败的回调函数
     */
    protected void onRequestPermissionError() {
    }

    /**
     * 权限请求成功的回调函数
     */
    protected abstract void onRequestPermissionSuccess();
}

