package com.cici.cicimobileassistant.views;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.cici.cicimobileassistant.R;

public class DefaultToolbar extends AbsToolbarView<DefaultToolbar.Builder.DefaultToolbarParams> {

    public DefaultToolbar(DefaultToolbar.Builder.DefaultToolbarParams params) {
        super(params);
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_toolbar;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void applyView() {

        RelativeLayout relativeLayout = findViewById(R.id.rl);
        if (getParams().mDirection != TitleDirection.LEFT) {

            relativeLayout.setVisibility(View.VISIBLE);
            setText(R.id.tvTitle, getParams().mTitle);

        } else {

            relativeLayout.setVisibility(View.GONE);
            setText(getParams().mDirection, getParams().mTitle);
            setText(R.id.tvRight, getParams().mRightText);
            setOnClickListener(R.id.tvRight, getParams().mOnRightClickListener);
        }

        setOnClickListener(R.id.ivBack, getParams().mOnBackClickListener);
        setOnMenuClickListener(getParams().mOnMenuItemClickListener);
        if (getParams().mMenuLayout != 0) {

            setMenu(getParams().mMenuLayout);
        }


    }




    public static class Builder extends AbsToolbarView.Builder {

        public DefaultToolbarParams params;

        public Builder(Context mContext, ViewGroup mParent) {
            super(mContext, mParent);
            params = new DefaultToolbarParams(mContext, mParent);
        }

        @Override
        public DefaultToolbar build() {
            return new DefaultToolbar(params);
        }

        public Builder setTitle(TitleDirection direction, String title) {

            params.mTitle = title;
            params.mDirection = direction;

            return this;
        }

        public Builder setRightText(String text) {
            params.mRightText = text;
            return this;
        }

        public Builder setMenuLayout(int menuLayout) {
            params.mMenuLayout = menuLayout;
            return this;

        }

        public Builder setTitle(String title) {

            params.mTitle = title;

            return this;
        }

        public Builder setOnMenuClickListener(Toolbar.OnMenuItemClickListener onMenuClickListener){
            params.mOnMenuItemClickListener=onMenuClickListener;
            return this;
        }

        public Builder setOnBackClickListener(View.OnClickListener onClickListener) {

            params.mOnBackClickListener = onClickListener;
            return this;

        }

        public Builder setOnRightClickListener(View.OnClickListener onClickListener) {

            params.mOnRightClickListener = onClickListener;

            return this;

        }


        public static class DefaultToolbarParams extends AbsToolbarView.Builder.AbsToolbarParams {

            public String mTitle;
            public String mRightText;
            public TitleDirection mDirection = TitleDirection.LEFT;
            public int mMenuLayout;
            public Toolbar.OnMenuItemClickListener mOnMenuItemClickListener;
            public View.OnClickListener mOnBackClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) mContext).finish();
                }
            };
            public View.OnClickListener mOnRightClickListener;

            public DefaultToolbarParams(Context mContext, ViewGroup mParent) {
                super(mContext, mParent);
            }
        }
    }
}
