package com.cici.cicimobileassistant.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.IInterface;
import android.util.ArrayMap;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class DaoSupport<T> implements IDaoSupport<T> {

    private SQLiteDatabase mSqLiteDatabase;
    private SQLiteDatabase sqLiteDatabase;
    private Class<T> tClass;


    /**
     * 先创建表(根据传进来的实体创建表，类名为表名，字段为列)
     */
    public void init(SQLiteDatabase sqLiteDatabase, Class<T> tClass) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.tClass = tClass;

        StringBuffer sb = new StringBuffer();
        sb.append("create table if not exists ")
                .append(tClass.getSimpleName())
                .append(" (id integer primary key autoincrement, ");

        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {

            field.setAccessible(true);
            String type = field.getType().getSimpleName();// int String boolean
            String name = field.getName();
            sb.append(name).append(DbCommonUtils.getColumnType(type)).append(", ");
        }
        sb.replace(sb.length() - 2, sb.length(), ")");
        String sql = sb.toString();

        sqLiteDatabase.execSQL(sql);

    }


    private static final Map<String, Method> mPutMethods = new ArrayMap<>();

    private static final Object[] mPutMethodArgs = new Object[2];

    /**
     * 插入数据，可以是任意对象
     *
     * @param obj
     * @return
     */
    @Override
    public long insert(T obj) {

        ContentValues contentValues = toContentValues(obj);
        return sqLiteDatabase.insert(tClass.getSimpleName(), null, contentValues);
    }


    // obj 转成 ContentValues
    private ContentValues toContentValues(T obj) {
        // 第三方的 使用比对一下 了解一下源码
        ContentValues contentValues = new ContentValues();

        // 封装values
        Field[] fields = tClass.getDeclaredFields();

        for (Field field : fields) {
            try {
                // 设置权限，私有和共有都可以访问
                field.setAccessible(true);
                String key = field.getName();
                // 获取value
                Object value = field.get(obj);
                // put 第二个参数是类型  把它转换

                mPutMethodArgs[0] = key;
                mPutMethodArgs[1] = value;

                // 方法使用反射 ， 反射在一定程度上会影响性能
                // 源码里面  activity实例 反射  View创建反射
                // 第三方以及是源码给我们提供的是最好的学习教材   插件换肤
                // 感谢google提供的源码，我们明天再见

                String filedTypeName = field.getType().getName();
                // 还是使用反射  获取方法  put  缓存方法
                Method putMethod = mPutMethods.get(filedTypeName);
                if (putMethod == null) {

                        putMethod = ContentValues.class.getDeclaredMethod("put",
                                String.class, value.getClass());
                        mPutMethods.put(filedTypeName, putMethod);//获取方法

                }

                if (value!=null){
                    // 通过反射执行
                    putMethod.invoke(contentValues, mPutMethodArgs[0],mPutMethodArgs[1]);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }
        }
        return contentValues;
    }


    @Override
    public QuerySupport<T> query() {
        return new QuerySupport<>(tClass, sqLiteDatabase);
    }

    @Override
    public long update(T obj, String whereClause, String[] whereArgs) {
        ContentValues contentValues = toContentValues(obj);

        if (contentValues != null) {
            return sqLiteDatabase.update(tClass.getSimpleName(), contentValues, whereClause, whereArgs);

        }
        return 0;
    }
}
