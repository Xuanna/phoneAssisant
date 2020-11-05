package com.cici.cicimobileassistant.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public interface IDaoSupport<T> {

    long insert(T t) throws IllegalAccessException;

    long update(T obj, String whereClause, String[] whereArgs);

    QuerySupport<T> query();

    void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz);




}
