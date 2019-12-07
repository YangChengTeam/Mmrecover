package com.yc.mmrecover.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.kk.utils.LogUtil;
import com.kk.utils.security.Md5;
import com.yc.mmrecover.constant.Config;
import com.yc.mmrecover.controller.activitys.MessageGuide2Activity;
import com.yc.mmrecover.model.bean.GlobalData;
import com.yc.mmrecover.model.bean.WxAccountInfo;
import com.yc.mmrecover.model.bean.WxChatMsgInfo;
import com.yc.mmrecover.model.bean.WxContactInfo;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import org.kamranzafar.jtar.TarEntry;
import org.kamranzafar.jtar.TarInputStream;
import org.nick.abe.AndroidBackup;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageUtils {


    static {
        GlobalData.brand = Build.BRAND.toLowerCase();
        GlobalData.microMsgPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tencent/MicroMsg";
    }

    private static String getMmDBKey(String systemInfo, String compatibleInfo) {
        ObjectInputStream systemInfoIn = null;
        ObjectInputStream compatibleInfoIn = null;
        try {
            systemInfoIn = new ObjectInputStream(new FileInputStream(
                    systemInfo));
            compatibleInfoIn = new ObjectInputStream(new FileInputStream(
                    compatibleInfo));

            HashMap systemInfoMp = (HashMap) systemInfoIn.readObject();
            HashMap compatibleInfoInMp = (HashMap) compatibleInfoIn.readObject();

            String mime = String.valueOf(compatibleInfoInMp.get(Integer
                    .valueOf(258)));
            String uin = String.valueOf(systemInfoMp.get(Integer.valueOf(1)));
            systemInfoIn.close();
            compatibleInfoIn.close();
            return Md5.md5(mime + uin).substring(0, 7);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getMmDBKey() {
        if (TextUtils.isEmpty(passphrase)) {
            String root = getMicroMsgDir();
            passphrase = getMmDBKey(root + "/systemInfo.cfg", root + "/CompatibleInfo.cfg");
        }
        return passphrase;
    }

    private static File dbFile = null;
    private static List<File> dbFiles = new ArrayList<>();

    private static void getEnMicroMsgDb(String path) {
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isDirectory()) {
                    getEnMicroMsgDb(file.getAbsolutePath());
                } else {
                    if (file.getName().equals("EnMicroMsg.db")) {
                        dbFiles.add(file);
                    }
                }
            }
        }
    }

    private static List<File> getEnMicroMsgDb() {
        if (dbFiles.size() == 0) {
            getEnMicroMsgDb(getMicroMsgDir());
        }
        return dbFiles;
    }

    private static String passphrase = "";

    static class MySQLiteDatabaseHook implements SQLiteDatabaseHook {
        public void preKey(SQLiteDatabase sQLiteDatabase) {
        }

        MySQLiteDatabaseHook() {
        }

        public void postKey(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.rawExecSQL("PRAGMA cipher_migrate;");
        }
    }

    private static String mmPath = "";

    private static void getMMDir(String dir) {
        File[] files = new File(dir).listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isDirectory()) {
                    if (file.getName().equals("com.tencent.mm")) {
                        mmPath = file.getAbsolutePath();
                        break;
                    }
                    getMMDir(file.getAbsolutePath());
                }
            }
        }
    }

    private static String getMMDir() {
        if (mmPath.isEmpty()) {
            getMMDir(getAppStorageDir());
        }
        return mmPath;
    }

    private static String getExternalStorageDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    private static String getAppStorageDir() {
        return getExternalStorageDir() + "/数据恢复助手";
    }

    private static String microMsgPath = "";

    private static void getMicroMsgDir(String dir) {
        File[] files = new File(dir).listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isDirectory()) {
                    if (file.getName().equals("MicroMsg")) {
                        microMsgPath = file.getAbsolutePath();
                        break;
                    }
                    getMicroMsgDir(file.getAbsolutePath());
                }
            }
        }
    }

    private static String getMicroMsgDir() {
        if (TextUtils.isEmpty(microMsgPath)) {
            getMicroMsgDir(getMMDir());
        }
        return microMsgPath;
    }

    private static SQLiteDatabase getSQLiteDatabase() throws IOException {
        File microMsgDir = new File(getMicroMsgDir());
        if (!microMsgDir.exists()) {
            if (GlobalData.brand.equals("xiaomi")) {
                changeMiuiBak2AndroidBak();
            } else if (GlobalData.brand.equals("oppo")) {
                String source = getExternalStorageDir() + "/backup/App/com.tencent.mm.tar";
                unpackZip(source, getAppStorageDir());
            }
        }
        SQLiteDatabase sqLiteDatabase = null;
        passphrase = getMmDBKey();
        for (File file : getEnMicroMsgDb()) {
            try {
                sqLiteDatabase = SQLiteDatabase.openDatabase(file.getAbsolutePath(), passphrase, null, 16, new MySQLiteDatabaseHook());
                if (sqLiteDatabase != null) {
                    dbFile = file;
                    break;
                }
            } catch (Exception e) {
            }
        }
        return sqLiteDatabase;
    }

    public static WxAccountInfo getWxAccountInfo() {
        WxAccountInfo wxAccountInfo = new WxAccountInfo();
        try {
            SQLiteDatabase sqLiteDatabase = getSQLiteDatabase();
            if (sqLiteDatabase == null) {
                LogUtil.msg("SQLiteDatabase is null");
                return null;
            }
            Cursor cursor = sqLiteDatabase.rawQuery("select id,value from userinfo where id in(2,4,6)", null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                if (id == 2) {
                    wxAccountInfo.setWxAccount(cursor.getString(cursor.getColumnIndex("value")));
                    SpUtils.getInstance().putString(Config.USER_ID, cursor.getString(cursor.getColumnIndex("value")));
                } else if (id == 4) {
                    wxAccountInfo.setNickName(cursor.getString(cursor.getColumnIndex("value")));
                } else if (id == 6) {
                    wxAccountInfo.setPhone(cursor.getString(cursor.getColumnIndex("value")));
                }
            }
            wxAccountInfo.setHeadPath(getUserHeadPath(wxAccountInfo.getWxAccount()));
            wxAccountInfo.setParent(dbFile.getParentFile().getPath());
            SpUtils.getInstance().putString(Config.HEAD_PATH, getUserHeadPath(wxAccountInfo.getWxAccount()));
            sqLiteDatabase.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wxAccountInfo;
    }

    private static List<WxContactInfo> mWxContactInfos;

    public static List<WxContactInfo> getWxContactInfos() {
        List<WxContactInfo> wxContactInfos = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = getSQLiteDatabase();
            if (sqLiteDatabase == null) {
                LogUtil.msg("SQLiteDatabase is null");
                return null;
            }
            Cursor cursor = sqLiteDatabase.rawQuery("select r.username,r.conRemark,r.nickname,r.quanPin,i.reserved1,i.reserved2,r.deleteFlag,max(m.createTime) as msgTime,m.content as msgContent,m.type as msgType,r.alias as alias,r.type  from rcontact r left join message m on r.username=m.talker left join img_flag i on r.username=i.username where r.username not like 'gh_%'and r.username not like 'gh_%'and r.type not in (33) and r.username !='filehelper' group by r.username order by r.quanPin asc", null);
            while (cursor.moveToNext()) {
                WxContactInfo wxContactInfo = new WxContactInfo();
                String uid = cursor.getString(cursor.getColumnIndex("username"));
                String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
                long msgTime = cursor.getLong(cursor.getColumnIndex("msgTime"));
                int msgType = cursor.getInt(cursor.getColumnIndex("msgType"));
                String msgContent = cursor.getString(cursor.getColumnIndex("msgContent"));
                String alias = cursor.getString(cursor.getColumnIndex("alias"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String quanPin = cursor.getString(cursor.getColumnIndex("quanPin"));
                String headPath = cursor.getString(cursor.getColumnIndex("reserved2"));

                wxContactInfo.setUid(uid);
                wxContactInfo.setLastTime((int) (msgTime / 1000));
                wxContactInfo.setContent(getChangeContent(msgType, msgContent));

                String userHeadPath = getUserHeadPath(uid);
                if (new File(userHeadPath).exists()) {
                    headPath = userHeadPath;
                }

                wxContactInfo.setHeadPath(headPath);

                String name = TextUtils.isEmpty(nickname) ? uid.contains("@chatroom") ? "历史群聊" : uid : nickname;
                wxContactInfo.setName(name);

                if (TextUtils.isEmpty(alias)) {
                    wxContactInfo.setWxId(uid);
                } else {
                    wxContactInfo.setWxId(alias);
                }
                wxContactInfo.setContactType(type);
                wxContactInfo.setQuanPin(quanPin);

                if (!TextUtils.isEmpty(name) && !name.startsWith("fake_")) {
                    wxContactInfos.add(wxContactInfo);
                }
            }
            sqLiteDatabase.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mWxContactInfos = wxContactInfos;
        return wxContactInfos;
    }

    public static List<WxChatMsgInfo> getWxMsgInfos(String uid) {
        List<WxChatMsgInfo> wxContactInfos = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = getSQLiteDatabase();
            if (sqLiteDatabase == null) {
                LogUtil.msg("SQLiteDatabase is null");
                return null;
            }
            Cursor cursor = sqLiteDatabase.rawQuery("select msgSvrId,type,talker,createTime,content,imgPath,isSend,msgSeq from message where talker = ?", new String[]{uid});
            String userId = SpUtils.getInstance().getString(Config.USER_ID);
            while (cursor.moveToNext()) {
                WxChatMsgInfo wxChatMsgInfo = new WxChatMsgInfo();
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String content = getChangeContent(type, cursor.getString(cursor.getColumnIndex("content")));
                String imgPath = cursor.getString(cursor.getColumnIndex("imgPath"));
                switch (type) {
                    case 3:
                        wxChatMsgInfo.setImgPath(getChatImagePath(dbFile.getParentFile().getName(), imgPath));
                        break;
                    case 34:
                        wxChatMsgInfo.setVoicePath(getChatVoicePath(dbFile.getParentFile().getName(), imgPath));
                        break;
                    case 43:
                        wxChatMsgInfo.setVideoPath(getChatVideoPath(dbFile.getParentFile().getName(), imgPath));
                        break;
                    case 47:
                        wxChatMsgInfo.setImgPath(getChatImagePath(dbFile.getParentFile().getName(), imgPath));
                        break;
                    default:
                        break;
                }
                int isSend = cursor.getInt(cursor.getColumnIndex("isSend"));
                wxChatMsgInfo.setSend(isSend == 1);
                wxChatMsgInfo.setContent(content);
                wxChatMsgInfo.setContentType(type);
                wxChatMsgInfo.setTime(cursor.getInt(cursor.getColumnIndex("createTime")));
                if (isSend == 1) {
                    wxChatMsgInfo.setUid(userId);
                    wxChatMsgInfo.setHeadPath(SpUtils.getInstance().getString(Config.HEAD_PATH));
                } else {
                    wxChatMsgInfo.setUid(cursor.getString(cursor.getColumnIndex("talker")));
                    wxChatMsgInfo.setHeadPath(getUserHeadPath(cursor.getString(cursor.getColumnIndex("talker"))));
                }

                wxContactInfos.add(wxChatMsgInfo);
            }
            sqLiteDatabase.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return wxContactInfos;
    }

    private static String getUserHeadPath(String account) {
        String suff = Func.md5(account);
        return dbFile.getParentFile().getPath() + "/avatar/" + suff.substring(0, 2) + "/" + suff.substring(2, 4) + "/user_" + suff + ".png";
    }

    private static String getChatVideoPath(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        String md5 = Func.md5(str2);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GlobalData.microMsgPath);
        stringBuilder.append("/");
        stringBuilder.append(str);
        stringBuilder.append("/video/");
        stringBuilder.append(md5.substring(0, 2));
        stringBuilder.append("/");
        stringBuilder.append(md5.substring(2, 4));
        stringBuilder.append("/msg_");
        str = stringBuilder.toString();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(str2);
        stringBuilder2.append(".amr");
        str = stringBuilder2.toString();
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("getChatVoicePath = ");
        stringBuilder3.append(str);
        return str;
    }

    private static String getChatVoicePath(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        String md5 = Func.md5(str2);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GlobalData.microMsgPath);
        stringBuilder.append("/");
        stringBuilder.append(str);
        stringBuilder.append("/voice2/");
        stringBuilder.append(md5.substring(0, 2));
        stringBuilder.append("/");
        stringBuilder.append(md5.substring(2, 4));
        stringBuilder.append("/msg_");
        str = stringBuilder.toString();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(str2);
        stringBuilder2.append(".amr");
        str = stringBuilder2.toString();
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("getChatVoicePath = ");
        stringBuilder3.append(str);
        return str;
    }

    private static String getChatImagePath(String str, String str2) {
        if (str2 == null || !str2.startsWith("THUMBNAIL_DIRPATH") || str2.indexOf("th_") <= 0 || str2.length() <= str2.indexOf("th_") + 7) {
            return str2;
        }
        int indexOf = str2.indexOf("th_");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GlobalData.microMsgPath);
        stringBuilder.append("/");
        stringBuilder.append(str);
        stringBuilder.append("/image2/");
        int i = indexOf + 3;
        int i2 = indexOf + 5;
        stringBuilder.append(str2.substring(i, i2));
        stringBuilder.append("/");
        stringBuilder.append(str2.substring(i2, indexOf + 7));
        stringBuilder.append("/");
        String stringBuilder2 = stringBuilder.toString();
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(stringBuilder2);
        stringBuilder3.append(str2.substring(i));
        stringBuilder3.append(".jpg");
        str = stringBuilder3.toString();
        stringBuilder3 = new StringBuilder();
        stringBuilder3.append(stringBuilder2);
        stringBuilder3.append(str2.substring(indexOf));
        stringBuilder3.append("hd");
        String stringBuilder4 = stringBuilder3.toString();
        StringBuilder stringBuilder5 = new StringBuilder();
        stringBuilder5.append(stringBuilder2);
        stringBuilder5.append(str2.substring(indexOf));
        str2 = stringBuilder5.toString();
        if (new File(str).exists()) {
            return str;
        }
        return new File(stringBuilder4).exists() ? stringBuilder4 : str2;
    }


    private static String getSpecContent(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<");
        stringBuilder.append(str);
        stringBuilder.append(">");
        String stringBuilder2 = stringBuilder.toString();
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("</");
        stringBuilder3.append(str);
        stringBuilder3.append(">");

        str = (TextUtils.isEmpty(str2) || !str2.contains(stringBuilder2)) ? str2 : str2.substring(str2.indexOf(stringBuilder2) + stringBuilder2.length(), str2.indexOf(stringBuilder3.toString()));
        if (TextUtils.isEmpty(str2)) {
            str = "";
        }
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("getSpecContent = ");
        stringBuilder4.append(str);
        return str;
    }

    private static String getContentTitle(String str) {
        String substring = (!TextUtils.isEmpty(str) && str.contains("</title>") && str.contains("<title>")) ? str.substring(str.indexOf("<title>") + 7, str.indexOf("</title>")) : str;
        if (TextUtils.isEmpty(str)) {
            substring = "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getContentTitle = ");
        stringBuilder.append(substring);
        return substring;
    }

    private static String getChangeContent(int i, String content) {
        StringBuilder stringBuilder;
        String substring;
        StringBuilder stringBuilder2;
        switch (i) {
            case 1:
                break;
            case 3:
                content = "[图片]";
                break;
            case 34:
                content = "[语音]";
                break;
            case 42:
                substring = content.substring(content.indexOf("nickname=\"") + 10);
                substring = substring.substring(0, substring.indexOf("\""));
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("[名片]");
                stringBuilder2.append(substring);
                content = stringBuilder2.toString();
                break;
            case 43:
                content = "[视频]";
                break;
            case 47:
                content = "[动画表情]";
                break;
            case 48:
                substring = content.substring(content.indexOf("label=\"") + 7);
                substring = substring.substring(0, substring.indexOf("\""));
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("[位置]");
                stringBuilder2.append(substring);
                content = stringBuilder2.toString();
                break;
            case 49:
                stringBuilder = new StringBuilder();
                stringBuilder.append("[分享]");
                stringBuilder.append(getContentTitle(content));
                content = stringBuilder.toString();
                break;
            case 285212721:
                content = "[新闻内容]";
                break;
            case 419430449:
                substring = "[微信转账]";
                String clearData = clearData(getSpecContent("pay_memo", content));
                String clearData2 = clearData(getSpecContent("feedesc", content));
                if ("3".equals(getSpecContent("paysubtype", content))) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(substring);
                    stringBuilder2.append("(已领取)");
                    substring = stringBuilder2.toString();
                }
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(substring);
                stringBuilder2.append(clearData);
                stringBuilder2.append(clearData2);
                content = stringBuilder2.toString();
                break;
            case 436207665:
                substring = clearData(getSpecContent("receivertitle", content));
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("[微信红包]");
                stringBuilder2.append(substring);
                content = stringBuilder2.toString();
                break;
            case 452984881:
                stringBuilder = new StringBuilder();
                stringBuilder.append("[微信卡包]");
                stringBuilder.append(getContentTitle(content));
                content = clearData(stringBuilder.toString());
                break;
            case 570425393:
                content = "群系统消息";
                break;
            default:
                content = getContentTitle(content);
                break;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("getChangeContent = ");
        stringBuilder.append(content);
        return content;
    }

    private static String clearData(String str) {
        return str.replace("<![CDATA[", "").replace("]]>", "");
    }

    public static void openBackup(Context context) {
        Intent backupIntent = new Intent("android.intent.action.VIEW");
        if (GlobalData.brand.equals("oppo")) {
            try {
                backupIntent.setComponent(new android.content.ComponentName("com.coloros.backuprestore", "com.coloros.backuprestore.activity.BootActivity"));
                context.startActivity(backupIntent);
            } catch (Exception ae) {
                try {
                    backupIntent.setComponent(new android.content.ComponentName("com.coloros.backuprestore", "com.coloros.backuprestore.BootActivity"));
                    context.startActivity(backupIntent);
                } catch (Exception e) {

                }
            }
        } else if (GlobalData.brand.equals("xiaomi")) {
            backupIntent.setComponent(new android.content.ComponentName("com.miui.backup", "com.miui.backup.BackupActivity"));
            context.startActivity(backupIntent);
        } else if (GlobalData.brand.equals("huawei") || GlobalData.brand.equals("honor")) {
            backupIntent.setComponent(new android.content.ComponentName("com.huawei.KoBackup", "com.huawei.KoBackup.InitializeActivity"));
            context.startActivity(backupIntent);
        } else if (GlobalData.brand.equals("meizu")) {
            if (Build.MODEL.toLowerCase().contains("m1 metal")) {
                context.startActivity(new Intent("android.settings.INTERNAL_STORAGE_SETTINGS"));
                return;
            }
            try {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setComponent(new android.content.ComponentName("com.meizu.backup", "com.meizu.backup.ui.MainBackupAct"));
                context.startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                context.startActivity(new Intent("android.settings.INTERNAL_STORAGE_SETTINGS"));
            }
        } else if (GlobalData.brand.equals("vivo")) {
            MessageGuide2Activity.startActivityForPackage(context, "com.bbk.cloud", "com.bbk.cloud.activities.BBKCloudHomeScreen");
        } else {
            try {
                context.startActivity(new Intent(Settings.ACTION_PRIVACY_SETTINGS));
            } catch (Exception e) {

            }
        }
    }

    private static void changeMiuiBak2AndroidBak() throws IOException {
        String source = getExternalStorageDir() + "/MIUI/backup/AllBackup";
        String dir = getAppStorageDir();
        String dest = dir + "/app.bak";
        String zip = dir + "/app.zip";
        File sourceFile = new File(source);
        if (!sourceFile.exists()) {
            return;
        }

        File destFile = new File(dest);
        if (destFile.exists()) {
            destFile.delete();
        }

        FileOutputStream fs = new FileOutputStream(destFile);
        File[] files = sourceFile.listFiles();
        if (files != null) {
            File bak = null;
            for (int i = 0; i < files.length; i++) {
                File[] baks = files[i].listFiles();
                for (File tmpbak : baks) {
                    if (tmpbak.getAbsolutePath().endsWith(".bak")) {
                        if (bak != null && bak.length() < tmpbak.length()) {
                            bak = tmpbak;
                        }
                    }
                }
            }
            InputStream is = new FileInputStream(bak);
            is.skip(41);
            byte[] bytes = new byte[16384];
            int len = is.read(bytes, 0, 16384);
            while (len > 0) {
                fs.write(bytes, 0, len);
                len = is.read(bytes);
            }
            String password = System.getenv("ABE_PASSWD");
            AndroidBackup.extractAsTar(dest, zip, password, false);
            unpackZip(zip, dir);
        }
    }

    private static void unpackZip(String tarFile, String destFolder) throws IOException {
        TarInputStream tis = new TarInputStream(new BufferedInputStream(new FileInputStream(tarFile)));
        TarEntry entry;

        while ((entry = tis.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                continue;
            }
            int count;
            byte data[] = new byte[16384];
            File file = new File(destFolder + "/" + entry.getName());
            File parent = file.getParentFile();

            if (!parent.exists()) {
                parent.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream dest = new BufferedOutputStream(fos);

            while ((count = tis.read(data)) != -1) {
                dest.write(data, 0, count);
            }

            dest.flush();
            dest.close();
        }

        tis.close();
    }

    public static WxContactInfo getUserInfoById(String str) {

        if (mWxContactInfos == null) {
            mWxContactInfos = getWxContactInfos();
        }
        if (mWxContactInfos != null) {
            for (WxContactInfo wxContactInfo : mWxContactInfos) {
                if (TextUtils.equals(str, wxContactInfo.getUid())) {
                    return wxContactInfo;
                }
            }
        }
        return null;
    }

    private static String mLastUid;
    private static int mMsgIndex;

    public static List<WxChatMsgInfo> fetchMessageInfo(String account_parent, String uid, String self_uid, boolean load_more) {
        String str4 = account_parent;
        String str5 = uid;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FetchMessageInfo parent = ");
        stringBuilder.append(str4);
        stringBuilder.append(",UID = ");
        stringBuilder.append(str5);

        List<WxChatMsgInfo> arrayList = new ArrayList<>();
        if (!(TextUtils.equals(mLastUid, str5) && load_more)) {
            mLastUid = str5;
            mMsgIndex = 0;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
//        stringBuilder2.append(mCurrentBakId);
        stringBuilder2.append(str4);
        stringBuilder2.append(str5);
        String stringBuilder3 = stringBuilder2.toString();
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("msgKey = ");
        stringBuilder4.append(stringBuilder3);
        Log.d("TAG", stringBuilder4.toString());
        int i = 30;
        StringBuilder stringBuilder5;
        if (load_more) {
            SQLiteDatabase sQLiteDatabase;
            String str6;
            SQLiteDatabase GetDatabase = null;
            try {
                GetDatabase = getSQLiteDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String userHeadPath = getUserHeadPath(uid);
            String userHeadPath2 = getUserHeadPath(self_uid);
            StringBuilder stringBuilder6 = new StringBuilder();
            stringBuilder6.append("select msgSvrId,type,talker,createTime,content,imgPath,isSend,msgSeq from message where talker = '");
            stringBuilder6.append(str5);
            stringBuilder6.append("'order by createTime asc");
            Cursor rawQuery = GetDatabase.rawQuery(stringBuilder6.toString(), null);
            StringBuilder stringBuilder7 = new StringBuilder();
            stringBuilder7.append("...msg count = ");
            stringBuilder7.append(rawQuery.getCount());
            Log.d("TAG", stringBuilder7.toString());
            stringBuilder7 = new StringBuilder();
            stringBuilder7.append("...mMsgIndex = ");
            stringBuilder7.append(mMsgIndex);
            Log.d("TAG", stringBuilder7.toString());
            if (rawQuery.getCount() > mMsgIndex + 30) {
                int count = (rawQuery.getCount() - mMsgIndex) - 30;
                StringBuilder stringBuilder8 = new StringBuilder();
                stringBuilder8.append("...moveIndex = ");
                stringBuilder8.append(count);
                Log.d("TAG", stringBuilder8.toString());
                rawQuery.move(count);
            }
            int i2 = mMsgIndex;
            String str7 = userHeadPath;
            int i3 = 0;
            long j = 0;

            while (rawQuery.moveToNext()) {

                i3++;
                if (i3 > i || i3 + i2 > rawQuery.getCount()) {
                    break;
                }
                String str8;

                mMsgIndex++;
                WxChatMsgInfo chatMsgBean = new WxChatMsgInfo();


                String string = rawQuery.getString(rawQuery.getColumnIndex("talker"));
                int type = rawQuery.getInt(rawQuery.getColumnIndex("type"));
                int isSend = rawQuery.getInt(rawQuery.getColumnIndex("isSend"));
                int i6 = i2;
                String imgPath = rawQuery.getString(rawQuery.getColumnIndex("imgPath"));
                int i7 = i3;
                sQLiteDatabase = GetDatabase;
                long createTime = rawQuery.getLong(rawQuery.getColumnIndex("createTime"));
                String str9 = str7;
                str7 = rawQuery.getString(rawQuery.getColumnIndex("msgSeq"));
                str6 = stringBuilder3;
                stringBuilder3 = rawQuery.getString(rawQuery.getColumnIndex("content"));

                stringBuilder5 = new StringBuilder();
                Cursor cursor = rawQuery;
                stringBuilder5.append("====talker=== ");
                stringBuilder5.append(string);
                Log.d("TAG", stringBuilder5.toString());
                stringBuilder5 = new StringBuilder();
                stringBuilder5.append("type=== ");
                stringBuilder5.append(type);
                Log.d("TAG", stringBuilder5.toString());
                stringBuilder5 = new StringBuilder();
                stringBuilder5.append("isSend=== ");
                stringBuilder5.append(isSend);
                Log.d("TAG", stringBuilder5.toString());
                stringBuilder5 = new StringBuilder();
                stringBuilder5.append("imgPath=== ");
                stringBuilder5.append(imgPath);
                Log.d("TAG", stringBuilder5.toString());
                stringBuilder5 = new StringBuilder();
                stringBuilder5.append("createTime=== ");
                stringBuilder5.append(createTime);
                Log.d("TAG", stringBuilder5.toString());
                stringBuilder5 = new StringBuilder();
                stringBuilder5.append("msgSeq=== ");
                stringBuilder5.append(str7);
                Log.d("TAG", stringBuilder5.toString());
                stringBuilder5 = new StringBuilder();
                stringBuilder5.append("content=== ");
                stringBuilder5.append(stringBuilder3);
                Log.d("TAG", stringBuilder5.toString());
                Log.d("TAG", "=============");


                chatMsgBean.setSend(isSend == 1);
                chatMsgBean.setType(isSend == 1 ? WxChatMsgInfo.TYPE_ME : WxChatMsgInfo.TYPE_FRIEND);
                chatMsgBean.setContent(getChangeContent(type, stringBuilder3));
                chatMsgBean.setContentType(type);
                chatMsgBean.setHeadPath(getUserHeadPath(string));
                chatMsgBean.setTime((int) createTime);

                if (Math.abs(createTime - j) > 360000) {
                    chatMsgBean.setType(WxChatMsgInfo.TYPE_TITLE);
                    str8 = string;
                    chatMsgBean.setContent(Func.formatData("yyyy/MM/dd HH:mm", createTime / 1000));

                } else {
                    str8 = string;
                }


                String str10 = str8;
                if (!str10.contains("@chatroom")) {
                    chatMsgBean.setName(str10);
                } else if (!TextUtils.isEmpty(stringBuilder3) && stringBuilder3.indexOf(":") > 0) {
                    chatMsgBean.setName(stringBuilder3.substring(0, stringBuilder3.indexOf(":")));
                    String substring = stringBuilder3.substring(0, stringBuilder3.indexOf(":"));
                    stringBuilder3 = stringBuilder3.substring(stringBuilder3.indexOf(":") + 1).replace("\n", "");
                    stringBuilder6 = new StringBuilder();
                    stringBuilder6.append("tmpUid = ");
                    stringBuilder6.append(substring);
                    Log.d("TAG", stringBuilder6.toString());
                    str10 = getUserHeadPath(str4);
                    StringBuilder stringBuilder9 = new StringBuilder();
                    stringBuilder9.append("room avatar = ");
                    stringBuilder9.append(str10);
                    Log.d("TAG", stringBuilder9.toString());
                    if (!new File(str10).exists()) {
//                        str10 = getAvatarById(substring);
                        stringBuilder5 = new StringBuilder();
                        stringBuilder5.append("by id avatar = ");
                        stringBuilder5.append(str10);
//                        log.e(stringBuilder5.toString());
                    }
                    str7 = str10;
                    chatMsgBean.setContentType(type);
                    chatMsgBean.setContent(getChangeContent(type, stringBuilder3));
                    if (type != 10000) {
                        chatMsgBean.setType(WxChatMsgInfo.TYPE_TITLE);
//                        arrayList.add(chatMsgBean);
                    } else if (type == 570425393) {
                        chatMsgBean.setType(WxChatMsgInfo.TYPE_TITLE);
//                        arrayList.add(chatMsgBean);
                    } else {
                        chatMsgBean.setTime((int) (createTime / 1000));
                        if (isSend == 1) {
                            chatMsgBean.setType(WxChatMsgInfo.TYPE_ME);
                            chatMsgBean.setHeadPath(userHeadPath2);
                        } else {
                            chatMsgBean.setType(WxChatMsgInfo.TYPE_FRIEND);
                            chatMsgBean.setHeadPath(str7);
                        }
                        if (type == 47 && stringBuilder3.contains("cdnurl")) {
                            substring = stringBuilder3.substring(stringBuilder3.indexOf("cdnurl") + 8);
                            if (substring.indexOf("\"") <= 2) {
                                substring = substring.substring(substring.indexOf("\"") + 1);
                            }
                            substring = substring.substring(0, substring.indexOf("\"")).replace("*#*", ":");
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("strUrl = ");
                            stringBuilder2.append(substring);
                            Log.d("TAG", stringBuilder2.toString());
                            chatMsgBean.setImgPath(substring);
                        } else {
                            chatMsgBean.setImgPath(getChatImagePath(str4, imgPath));
                            stringBuilder5 = new StringBuilder();
                            stringBuilder5.append("getImgPath = ");
                            stringBuilder5.append(chatMsgBean.getImgPath());
                            Log.d("TAG", stringBuilder5.toString());
                        }
                        if (chatMsgBean.getContentType() == 43) {
                            stringBuilder5 = new StringBuilder();
                            stringBuilder5.append(GlobalData.microMsgPath);
                            stringBuilder5.append(File.separator);
                            stringBuilder5.append(str4);
                            stringBuilder5.append("/video/");
                            stringBuilder5.append(imgPath);
                            stringBuilder5.append(".mp4");
                            chatMsgBean.setVideoPath(stringBuilder5.toString());
                        }
                        if (chatMsgBean.getContentType() == 34) {
                            chatMsgBean.setVoicePath(getChatVoicePath(str4, imgPath));

                            chatMsgBean.setVoiceSec(new PlayVoiceTask(null).getVoiceMiSecond(chatMsgBean.getVoicePath()) / 1000);
                        } else {

                        }
                        chatMsgBean.setUid(str5);


                    }

                    stringBuilder3 = str6;

                }
                str7 = str9;
                chatMsgBean.setContentType(type);
                chatMsgBean.setContent(getChangeContent(type, stringBuilder3));
                if (type != 10000) {
                }

                j = createTime;
                i2 = i6;
                i3 = i7;
                GetDatabase = sQLiteDatabase;
                stringBuilder3 = str6;
                rawQuery = cursor;
                i = 30;
                arrayList.add(chatMsgBean);
            }


            sQLiteDatabase = GetDatabase;
            rawQuery.close();
            sQLiteDatabase.close();
        }


        return arrayList;
    }


}
