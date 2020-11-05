package com.cici.cicimobileassistant.views;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.cici.cicimobileassistant.R;

/**
 * 使用builder设计模式,在布局中添加toolbar
 */
public abstract class AbsToolbarView<P extends AbsToolbarView.Builder.AbsToolbarParams> implements IToolbar {

    private P params;

    public AbsToolbarView(P params) {

        this.params = params;
        createAndBindView();

    }

    public void setText(int id, String text) {
        TextView textView = findViewById(id);

        if (textView != null) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }

    }

    public void setOnMenuClickListener(Toolbar.OnMenuItemClickListener onMenuClickListener) {
        toolbar.setOnMenuItemClickListener(onMenuClickListener);
    }

    public void setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        View view = findViewById(viewId);
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }

    }

    public <T extends View> T findViewById(int id) {


        return (T) toolbarView.findViewById(id);

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setText(TitleDirection direction, String text) {

        if (direction == TitleDirection.LEFT) {

            if (toolbar != null) {

                toolbar.setTitle(text);
            }

        } else if (direction == TitleDirection.CENTER) {


        } else if (direction == TitleDirection.RIGHT) {

        }

    }


    public P getParams() {
        return params;
    }

    private View toolbarView;

    private Toolbar toolbar;

    public void createAndBindView() {

        //获取decorview直接在里面添加toolbar内容
        if (params.mParent == null) {
            ViewGroup decorView = (ViewGroup) ((Activity) params.mContext)
                    .getWindow().getDecorView();
            params.mParent = (ViewGroup) decorView.getChildAt(0);

        }
        toolbarView = LayoutInflater.from(params.mContext)
                .inflate(bindLayout(), params.mParent, false);
        params.mParent.addView(toolbarView, 0);


        toolbar = toolbarView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_normal);

        applyView();



    }


    public void setMenu(int menuLayout) {

        if (toolbar != null) {

            toolbar.inflateMenu(menuLayout);
        }
    }

    public abstract static class Builder {
        AbsToolbarParams params;


        public Builder(Context mContext, ViewGroup mParent) {
            params = new AbsToolbarParams(mContext, mParent);
        }


        public abstract AbsToolbarView build();

        public static class AbsToolbarParams {

            public Context mContext;
            public ViewGroup mParent;

            public AbsToolbarParams(Context mContext, ViewGroup mParent) {
                this.mContext = mContext;
                this.mParent = mParent;
            }
        }


    }
}
