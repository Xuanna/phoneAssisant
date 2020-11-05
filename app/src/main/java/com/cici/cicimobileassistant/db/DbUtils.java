package com.cici.cicimobileassistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.cici.cicimobileassistant.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * 创建数据库
 */
public class DbUtils {
    private static SQLiteDatabase mSqLiteDatabase = null;
    public static DbUtils dbUtils;
    private Context context;

    public static DbUtils getDbUtils() {
        if (dbUtils == null) {
            synchronized (DbUtils.class) {
                if (dbUtils == null) {
                    dbUtils = new DbUtils();
                }
            }
        }
        return dbUtils;
    }

    public <T> IDaoSupport<T> getDao(Class<T> tClass) {
        IDaoSupport<T> daoSoupport = new DaoSupport();
        daoSoupport.init(mSqLiteDatabase, tClass);
        return daoSoupport;
    }

    /**
     * 创建数据库并将数据库放在sd卡上
     */
    public void init(Context context) {
        this.context = context;

        if (mSqLiteDatabase==null){
            File dbRoot = null;
            File fileDbRoot=null;
            try {
                dbRoot = FileUtils.init(context).createSDPackageFile("database", "download.db");
                fileDbRoot = FileUtils.init(context).createSDPackageFile("database", "fileInfo.db");

                mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbRoot, null);
                mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(fileDbRoot, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
