package com.cici.cicimobileassistant.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.TypedValue;

import java.math.BigDecimal;
import java.security.MessageDigest;

public class CommonUtils {


    public static String md5Url(String url) {

        if (TextUtils.isEmpty(url)) {

            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] cipher = messageDigest.digest(url.getBytes());

            for (int i = 0; i < cipher.length; i++) {
                //转成16进制
                String hexStr = Integer.toHexString(cipher[i]);
                stringBuilder.append(hexStr.length() == 1 ? "0" + hexStr : hexStr);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();


    }

    public static float add(float a, float b) {

        BigDecimal bDouble1 = BigDecimal.valueOf(a);
        BigDecimal bDouble2 = new BigDecimal(b);
        BigDecimal result = bDouble1.add(bDouble2);
        return result.floatValue();

    }


    public static int sub(float a, float b) {

        BigDecimal bDouble1 = BigDecimal.valueOf(a);
        BigDecimal bDouble2 = new BigDecimal(b);
        BigDecimal result = bDouble1.subtract(bDouble2);
        return result.intValue();

    }

    public static int getVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    public static int dp2px(Context context, int pxV) {


        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pxV, context.getResources().getDisplayMetrics()) + 0.5f);
    }
}
