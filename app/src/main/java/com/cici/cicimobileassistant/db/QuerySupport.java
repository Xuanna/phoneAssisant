package com.cici.cicimobileassistant.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询类
 *
 * @param <T>
 */
public class QuerySupport<T> {

    private Class<T> mClass;
    private SQLiteDatabase mSQLiteDatabase;


    public QuerySupport(Class<T> mClass, SQLiteDatabase mSQLiteDatabase) {
        this.mClass = mClass;
        this.mSQLiteDatabase = mSQLiteDatabase;
    }

    public List<T> queryAll() {
        Cursor cursor = mSQLiteDatabase.query(mClass.getSimpleName(), null, null, null
                , null, null, null);

        return cursor2List(cursor);
    }

    public T query(String[] columns, String selection,
                   String[] selectionArgs, String groupBy, String having,
                   String orderBy) {
        Cursor cursor = mSQLiteDatabase.query(mClass.getSimpleName(), columns, selection,
                selectionArgs, groupBy, having, orderBy);
        T tInstance = null;
        try {
            tInstance = mClass.newInstance();

            Field[] fields = mClass.getDeclaredFields();
            for (Field field : fields) {

                field.setAccessible(true);
                String name = field.getName();
                int index = cursor.getColumnIndex(name);
                if (index == -1) {
                    continue;
                }
                Method method = cursorMethod(field.getType());// 通过反射获取Method方法，并且获得method方法的返回追
                if (method != null) {
                    Object value = method.invoke(cursor, index);
                    if (value == null) {
                        continue;
                    }
                    field.set(tInstance, value);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
        return tInstance;

    }

    private List<T> cursor2List(Cursor cursor) {

        List<T> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {

            try {

                do {
                    T instance = mClass.newInstance();
                    Field[] fields = mClass.getDeclaredFields();
                    for (Field field : fields) {

                        field.setAccessible(true);
                        String name = field.getName();

                        int index = cursor.getColumnIndex(name);

                        if (index == -1) {
                            continue;
                        }
                        Method method = cursorMethod(field.getType());// 通过反射获取Method方法，并且获得method方法的返回追
                        if (method != null) {
                            Object value = method.invoke(cursor, index);
                            if (value == null) {
                                continue;
                            }
                            field.set(instance, value);
                        }
                    }
                    list.add(instance);
                } while (cursor.moveToNext());

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        cursor.close();
        return list;
    }


    private Method cursorMethod(Class<?> type) throws Exception {
        String methodName = getColumnMethodName(type);
        Method method = Cursor.class.getMethod(methodName, int.class);
        return method;
    }

    private String getColumnMethodName(Class<?> fieldType) {
        String typeName = fieldType.getSimpleName();//int long String
        String methodName = "get" + typeName;
        if ("getBoolean".equals(methodName)) {
            methodName = "getBlob";
        } else if ("getChar".equals(methodName) || "getCharacter".equals(methodName)) {
            methodName = "getString";
        } else if ("getDate".equals(methodName)||"getlong".equals(methodName)) {
            methodName = "getLong";
        } else if ("getInteger".equals(methodName)||"getint".equals(methodName)) {
            methodName = "getInt";
        }
        return methodName;
    }
}
