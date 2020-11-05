package com.cici.cicimobileassistant.utils;


import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * 为状态栏设置颜色
 * 5.0以上状态栏默认有颜色，可以改变颜色值
 * 5.0以下默认是黑色,4.4以下无法改变
 * 所以针对的是4。4以上的手机
 */
public class StatusBarUtils {



    public static void setStatusBarColor(Activity activity, int color){

        //5.0以上直接设置颜色
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

            activity.getWindow().setStatusBarColor(color);


        }else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){

            //4.0-5.0可以设置状态栏透明, 但是状态栏上面的电量等等需要在

            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            View view=new View(activity);
            view.setLayoutParams(new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,getStatusBarHeight(activity)));
            view.setBackgroundColor(color);

            ViewGroup decorView= (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(view);

            //在状态栏上面添加一个和状态栏一样高度的view
            ViewGroup contentView=decorView.findViewById(android.R.id.content);
            View activityView=contentView.getChildAt(0);
            activityView.setFitsSystemWindows(true);



        }

    }

    private static int getStatusBarHeight(Activity activity) {
        Resources resources=activity.getResources();

        int statusHeightId=resources.getIdentifier("status_bar_height" ,"dimen","android");
        return resources.getDimensionPixelOffset(statusHeightId);

    }


}
