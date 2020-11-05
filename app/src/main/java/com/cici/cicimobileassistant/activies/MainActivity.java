package com.cici.cicimobileassistant.activies;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cici.cicimobileassistant.R;
import com.cici.cicimobileassistant.db.DbUtils;
import com.cici.cicimobileassistant.download.DownloadCallback;
import com.cici.cicimobileassistant.download.DownloadManager;
import com.cici.cicimobileassistant.entity.DownloadFileInfo;
import com.cici.cicimobileassistant.fragment.RankFragment;
import com.cici.cicimobileassistant.fragment.RecommendFragment;
import com.cici.cicimobileassistant.views.DefaultToolbar;
import com.cici.cicimobileassistant.views.ScaleTextView;
import com.tbruyelle.rxpermissions3.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ViewPager viewpager;
    private LinearLayout llTitle;

    private List<Fragment> fragmentList = new ArrayList<>();
    private String[] title = new String[]{"推荐", "排行", "游戏", "分类"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity or Fragment instance



        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {


                    } else {

                    }
                });

        new DefaultToolbar.Builder(this, null)
                .setTitle("手机助手")
                .setRightText("设置")
                .setMenuLayout(R.menu.toolbar_menu)
                .setOnMenuClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.action_search:

                                Log.e("Onclick", "action_search");
                                break;
                        }


                        return true;
                    }
                })
                .setOnRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Log.e("Onclick", "hhhhh");
                    }
                })
                .build();


        llTitle = findViewById(R.id.llTitle);
        viewpager = findViewById(R.id.viewpager);
        for (int i = 0; i < title.length; i++) {
            ScaleTextView scaleTextView = new ScaleTextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            scaleTextView.setGravity(Gravity.CENTER);
            params.weight = 1;
            scaleTextView.setLayoutParams(params);
            scaleTextView.setText(title[i]);
            llTitle.addView(scaleTextView);
            if (i == 0) {
                scaleTextView.setProgress(1);
                scaleTextView.setSelected(true);
            }
        }

        initViewpager();


    }


    public void initViewpager() {
        fragmentList.add(new RecommendFragment());
        fragmentList.add(new RankFragment());
        fragmentList.add(new RecommendFragment());
        fragmentList.add(new RankFragment());

        viewpager.addOnPageChangeListener(onPageChangeListener);
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                super.destroyItem(container, position, object);
            }
        });
        viewpager.setCurrentItem(0);
    }


    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        ScaleTextView selectTV, right;
        private int scrollPos;

        /**
         *
         * @param position
         * @param positionOffset：代表滚动的百分比0-1
         * @param positionOffsetPixels
         * 左边--0-1
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            try {
                selectTV = (ScaleTextView) llTitle.getChildAt(position);
                selectTV.setProgress(1 - positionOffset);


                right = (ScaleTextView) llTitle.getChildAt(position + 1);
                right.setProgress(positionOffset);
            } catch (Exception e) {

            }
        }

        @Override
        public void onPageSelected(int position) {

            try {
                selectTV = (ScaleTextView) llTitle.getChildAt(position);
                selectTV.setSelected(true);

                if (position < title.length - 1) {
                    right = (ScaleTextView) llTitle.getChildAt(position + 1);
                    right.setSelected(false);
                }

                if (position > 0) {
                    ScaleTextView center = (ScaleTextView) llTitle.getChildAt(position - 1);
                    center.setSelected(false);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}