package com.yc.mmrecover.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;

import com.kk.utils.LogUtil;
import com.kk.utils.security.Md5;
import com.yc.mmrecover.constant.Config;
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
            } else if (GlobalData.brand.equals("huawei") || GlobalData.brand.equals("honor")) {
                unPackHuaweiBackup();

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
            Cursor cursor = sqLiteDatabase.rawQuery("select msgSvrId,type,talker,createTime,content,imgPath,isSend,msgSeq from message where talker = ? order by createTime asc", new String[]{uid});
            int startTime = 0;
            while (cursor.moveToNext()) {
                WxChatMsgInfo wxChatMsgInfo = new WxChatMsgInfo();
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String content = getChangeContent(type, cursor.getString(cursor.getColumnIndex("content")));
                String imgPath = cursor.getString(cursor.getColumnIndex("imgPath"));
                int createTime = cursor.getInt(cursor.getColumnIndex("createTime"));
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
                wxChatMsgInfo.setSend(cursor.getInt(cursor.getColumnIndex("isSend")) == 1 ? true : false);
                wxChatMsgInfo.setType(cursor.getInt(cursor.getColumnIndex("isSend")) == 1 ? 1 : 0);
                wxChatMsgInfo.setContent(content);
                wxChatMsgInfo.setContentType(type);
                wxChatMsgInfo.setHeadPath(getUserHeadPath(cursor.getString(cursor.getColumnIndex("talker"))));
                wxChatMsgInfo.setTime(createTime);

                if (Math.abs(createTime - startTime) > 360000) {
                    WxChatMsgInfo wxChatMsgTimeInfo = new WxChatMsgInfo();
                    wxChatMsgTimeInfo.setType(2);
                    wxChatMsgTimeInfo.setContent(Func.formatData("yyyy/MM/dd HH:mm", createTime / 1000));
                    wxContactInfos.add(wxChatMsgTimeInfo);
                }
                startTime = createTime;

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
            try {
                backupIntent.setComponent(new android.content.ComponentName("com.miui.backup", "com.miui.backup.BackupActivity"));
                context.startActivity(backupIntent);
            } catch (Exception e) {

            }
        } else if (GlobalData.brand.equals("huawei") || GlobalData.brand.equals("honor")) {
            try {
                backupIntent.setComponent(new android.content.ComponentName("com.huawei.KoBackup", "com.huawei.KoBackup.InitializeActivity"));
                context.startActivity(backupIntent);
            } catch (Exception ae) {

            }
        } else if (GlobalData.brand.equals("meizu"))
        {
            if (Build.MODEL.toLowerCase().contains("m1 metal")) {
                context.startActivity(new Intent("android.settings.INTERNAL_STORAGE_SETTINGS"));
                return;
            }
            try {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setComponent(new android.content.ComponentName("com.meizu.backup", "com.meizu.backup.ui.MainBackupAct"));
                context.startActivity(intent);
            } catch (ActivityNotFoundException unused) {

            }
        } else {
            try {
                context.startActivity(new Intent(Settings.ACTION_PRIVACY_SETTINGS));
            } catch (Exception e) {

            }
        }
    }

    private static File huaWeiBackupFile = null;

    public static void unPackHuaweiBackup() throws IOException {
        String source = getExternalStorageDir() + "/Huawei/Backup";
        unPackHuaweiBackup(source);
        if (huaWeiBackupFile != null) {
            unPackHuaweiBackup2(huaWeiBackupFile.getAbsolutePath());
        }
    }

    public static void unPackHuaweiBackup(String source) throws IOException {
        File sourceFile = new File(source);
        if (!sourceFile.exists()) {
            return;
        }
        File[] files = sourceFile.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    unPackHuaweiBackup(files[i].getAbsolutePath());
                } else {
                    if (files[i].getAbsolutePath().endsWith("com.tencent.mm.db")) {
                        huaWeiBackupFile = files[i];
                        break;
                    }
                }
            }
        }
    }

    private static void unPackHuaweiBackup2(String str) throws IOException {
        File file = new File(str);
        if (file.exists()) {
            SQLiteDatabase openDatabase = SQLiteDatabase.openDatabase(str, "", null, 16);
            openDatabase.execSQL("drop table if exists need");
            openDatabase.execSQL("create table if not exists need as select file_index,file_path from apk_file_info where file_path like '%user_%' or file_path like '%EnMicroMsg%' or file_path like '%systemInfo.cfg' or file_path like '%CompatibleInfo.cfg' or file_path like '%.tem' or file_path like '%IndexMicroMsg.db' or file_path like '%EnMicroMsg%' or file_path like '%FTS5IndexMicroMsg%' or file_path like '%app_brand_global_sp.xml'");
            Cursor rawQuery = openDatabase.rawQuery("select n.file_path,d.* from apk_file_data d join need n on n.file_index=d.file_index order by d.file_index desc,d.data_index asc", null);
            while (rawQuery.moveToNext()) {
                String path = getAppStorageDir() + rawQuery.getString(rawQuery.getColumnIndex("file_path"));
                byte[] blob = rawQuery.getBlob(rawQuery.getColumnIndex("file_data"));
                File dataFile = new File(path);
                if (!dataFile.getParentFile().exists()) {
                    dataFile.getParentFile().mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(dataFile, true);
                fileOutputStream.write(blob);
                fileOutputStream.close();
            }
            rawQuery.close();
            openDatabase.close();
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
                    if (tmpbak.getAbsolutePath().contains("com.tencent.mm") && tmpbak.getAbsolutePath().endsWith(".bak")) {
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

            destFile.delete();
            new File(zip).delete();
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

}
