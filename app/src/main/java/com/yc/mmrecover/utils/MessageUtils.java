package com.yc.mmrecover.utils;

import android.os.Build;
import android.os.Environment;

import com.kk.utils.security.Md5;
import com.yc.mmrecover.model.bean.GlobalData;
import com.yc.mmrecover.model.bean.WxAccountInfo;

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
    }

    public static String getMmDBKey(String systemInfo, String compatibleInfo) {
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

    public static String getMmDBKey() {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/数据恢复助手";
        return getMmDBKey(dir + "/apps/com.tencent.mm/r/MicroMsg/systemInfo.cfg", dir + "/apps/com.tencent.mm/r/MicroMsg/CompatibleInfo.cfg");
    }

    public static File dbFile = null;

    public static void getEnMicroMsgDb(String path) {
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isDirectory()) {
                    getEnMicroMsgDb(file.getAbsolutePath());
                } else {
                    if (file.getName().equals("EnMicroMsg.db")) {
                        if (dbFile == null || dbFile.length() < file.length()) {
                            dbFile = file;
                        }
                    }
                }
            }
        }
    }

    //select id,value from userinfo where id in(2,4,6)   查看帐号
    //select msgSvrId,type,talker,createTime,content,imgPath,isSend,msgSeq from message where talker = '
    //select r.username,r.conRemark,r.nickname,r.quanPin,i.reserved1,i.reserved2,r.deleteFlag,max(m.createTime) as msgTime,m.content as msgContent,m.type as msgType,r.alias as alias,r.type  from rcontact r left join message m on r.username=m.talker left join img_flag i on r.username=i.username where r.username not like 'gh_%'and r.username not like 'gh_%'and r.type not in (33) and r.username !='filehelper' group by r.username order by r.quanPin asc 查看联系人

    public static SQLiteDatabase getSQLiteDatabase(String path) {
        String passphrase = getMmDBKey();
        return SQLiteDatabase.openDatabase(path, passphrase, null, 16, new MySQLiteDatabaseHook());
    }

    static class MySQLiteDatabaseHook implements SQLiteDatabaseHook {
        public void preKey(SQLiteDatabase sQLiteDatabase) {
        }

        MySQLiteDatabaseHook() {
        }

        public void postKey(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.rawExecSQL("PRAGMA cipher_migrate;");
        }
    }

    public static SQLiteDatabase getSQLiteDatabase() throws IOException {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/数据恢复助手" + "/apps/com.tencent.mm/r/MicroMsg";
        File microMsgDir = new File(path);
        if (!microMsgDir.exists()) {
            if (GlobalData.brand.equals("xiaomi")) {
                changeMiuiBak2AndroidBak();
            }
        }
        getEnMicroMsgDb(path);
        if (dbFile != null) {
            return getSQLiteDatabase(dbFile.getAbsolutePath());
        }
        return null;
    }

    public static List<WxAccountInfo> getWxAccountInfo() {
        List<WxAccountInfo> wxAccountInfos = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = getSQLiteDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select id,value from userinfo where id in(2,4,6)", null);
            WxAccountInfo wxAccountInfo = new WxAccountInfo();
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
            wxAccountInfos.add(wxAccountInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wxAccountInfos;
    }


    public static void changeMiuiBak2AndroidBak() throws IOException {
        String source = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MIUI/backup/AllBackup";
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/数据恢复助手";
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


}
