package com.yc.mmrecover.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

import com.kk.utils.LogUtil;
import com.yc.mmrecover.BuildConfig;
import com.yc.mmrecover.controller.activitys.MessageGuide2Activity;
import com.yc.mmrecover.model.bean.GlobalData;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.PointerIconCompat;


/**
 * Created by suns  on 2019/12/6 08:22.
 */
public class Func1 {
    private static String testCode;

    public static long getAvailableMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        if (Build.VERSION.SDK_INT < 18) {
            return 0;
        }
        long blockSizeLong = statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("availableBlocksLong = ");
        stringBuilder.append(blockSizeLong);

        return blockSizeLong;
    }

    public static boolean isLetterDigitOrChinese(String str) {
        return str.matches("^[a-z0-9A-Z一-龥]+$");
    }

    public static void checkStorePermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, PointerIconCompat.TYPE_HAND);
            return;
        }

    }

    public static String makeStringHeadMix(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = str.length();
        int i = 0;
        while (i < length) {
            if (i == 0 || i == 1) {
                stringBuilder.append(str.charAt(i));
            } else {
                stringBuilder.append("*");
            }
            i++;
        }
        return stringBuilder.toString();
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void gotoBackUp(Context context) {
        Object obj;
        String str = GlobalData.brand;
//        Log.e("TAG", "gotoBackUp: " + str.hashCode() + "--str--" + str);
        switch (str.hashCode()) {
            case -1245779295:
                if (str.equals("gionee")) {
                    obj = 5;
                    break;
                }
            case -1206476313:
                if (str.equals("huawei")) {
                    obj = 1;
                    break;
                }
            case -759499589:
                if (str.equals("xiaomi")) {
                    obj = null;
                    break;
                }
            case 3418016:
                if (str.equals("oppo")) {
                    obj = 3;
                    break;
                }
            case 99462250:
                if (str.equals("honor")) {
                    obj = 2;
                    break;
                }
            case 103777484:
                if (str.equals("meizu")) {
                    obj = 4;
                    break;
                }
            case 3620012:
                if (str.equals("vivo")) {
                    obj = 6;
                    break;
                }
            default:
                obj = -1;
                break;
        }
        if (obj == null) {
            MessageGuide2Activity.startActivityForPackage(context, "com.miui.backup", "com.miui.backup.BackupActivity");
//            startOppoForPackage(context);
        } else if (obj.equals(1) || obj.equals(2)) {
            MessageGuide2Activity.startActivityForPackage(context, "com.huawei.KoBackup", "com.huawei.KoBackup.InitializeActivity");
        } else if (obj.equals(3)) {
            startOppoForPackage(context);
        } else if (obj.equals(4)) {
            startMXForPackage(context);
        } else if (obj.equals(5)) {
            MessageGuide2Activity.startActivityForPackage(context, "gn.com.android.synchronizer", "gn.com.android.synchronizer.SettingNaviActivity");
        } else if (obj.equals(6)) {
            MessageGuide2Activity.startActivityForPackage(context, "com.bbk.cloud", "com.bbk.cloud.activities.BBKCloudHomeScreen");
        }
//        switch (obj) {
//            case null:
//                MessageGuide2Activity.startActivityForPackage(context, "com.miui.backup", "com.miui.backup.BackupActivity");
//                return;
//            case 1:
//            case 2:
//                MessageGuide2Activity.startActivityForPackage(context, "com.huawei.KoBackup", "com.huawei.KoBackup.InitializeActivity");
//                return;
//            case 3:
//                startOppoForPackage(context);
//                return;
//            case 4:
//                startMXForPackage(context);
//                return;
//            case 5:
//                MessageGuide2Activity.startActivityForPackage(context, "gn.com.android.synchronizer", "gn.com.android.synchronizer.SettingNaviActivity");
//                return;
//            default:
//                return;
//        }
    }

    private static void startMXForPackage(Context context) {
        if (Build.MODEL.toLowerCase().contains("m1 metal")) {
            context.startActivity(new Intent("android.settings.INTERNAL_STORAGE_SETTINGS"));
            return;
        }
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setComponent(new ComponentName("com.meizu.backup", "com.meizu.backup.ui.MainBackupAct"));
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            context.startActivity(new Intent("android.settings.INTERNAL_STORAGE_SETTINGS"));
        }
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:5:?, code:
            r0 = new android.content.Intent("android.intent.action.VIEW");
            r0.setComponent(new android.content.ComponentName("com.coloros.backuprestore", "com.coloros.backuprestore.activity.BootActivity"));
            r4.startActivity(r0);
     */
    /* JADX WARNING: Missing block: B:8:?, code:
            return;
     */
    /* JADX WARNING: Missing block: B:10:?, code:
            return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void startOppoForPackage(Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setComponent(new ComponentName("com.coloros.backuprestore", "com.coloros.backuprestore.BootActivity"));
        context.startActivity(intent);
        try {
        } catch (ActivityNotFoundException unused) {
            intent = new Intent("android.intent.action.VIEW");
            intent.setComponent(new ComponentName("com.oppo.backuprestore", "com.oppo.backuprestore.BootActivity"));
            context.startActivity(intent);
        }
        try {
        } catch (ActivityNotFoundException unused2) {
            Toast.makeText(context, "未找到备份软件，请您自行使用系统的备份软件，完成后回到本软件解析.", Toast.LENGTH_SHORT).show();
        }
    }


    private static String getSignValidString(byte[] bArr) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance("MD5");
        instance.update(bArr);
        return toHexString(instance.digest());
    }

    private static String toHexString(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            String num = Integer.toString(b & 255, 16);
            if (num.length() == 1) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("0");
                stringBuilder2.append(num);
                num = stringBuilder2.toString();
            }
            stringBuilder.append(num);
        }
        return stringBuilder.toString();
    }


}
