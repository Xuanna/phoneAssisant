package com.cici.cicimobileassistant.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class MyTabLayout extends HorizontalScrollView {

    public MyTabLayout(Context context) {
        this(context,null);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
