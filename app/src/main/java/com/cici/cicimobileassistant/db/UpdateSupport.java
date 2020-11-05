package com.cici.cicimobileassistant.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class UpdateSupport<T> {
    private Class<T> mClass;
    private SQLiteDatabase mSQLiteDatabase;


    public UpdateSupport(Class<T> mClass, SQLiteDatabase mSQLiteDatabase) {
        this.mClass = mClass;
        this.mSQLiteDatabase = mSQLiteDatabase;
    }

    public long update(T object, String whereClause, String[] whereArgs) {

        ContentValues contentValue = new ContentValues();


        return mSQLiteDatabase.update(mClass.getSimpleName(), contentValue, whereClause, whereArgs);
    }

}
