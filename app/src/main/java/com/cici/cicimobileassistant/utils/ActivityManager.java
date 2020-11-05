package com.cici.cicimobileassistant.utils;

import android.app.Activity;
import android.graphics.ImageDecoder;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {


    public static List<Activity> activities = new ArrayList<>();


    public static void addActivity(Activity activity) {

        if (activity != null) {
            activities.add(activity);
        }

    }

    public static void removeActivity(Activity activity) {

        if (activity != null) {
            activities.remove(activity);
        }
    }

    public static void removeAll() {


        for (Activity activity : activities) {

            activities.remove(activity);
        }

    }

}
