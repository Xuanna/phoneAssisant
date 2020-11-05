package com.cici.cicimobileassistant.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;

import com.cici.cicimobileassistant.R;
import com.cici.cicimobileassistant.utils.CommonUtils;

public class ScaleTextView extends AppCompatTextView {

    private int normalTextColor = Color.BLACK;
    private int selectedTextColor = Color.RED;
    private int normalTextSize;
    private int selectedTextSize;
    private float progress;
    private ScaleType mScaleType = ScaleType.NNORMAL;

    public enum ScaleType {
        LARGE, SHIRNK, NNORMAL
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public void setScaleType(ScaleType type) {
        this.mScaleType = type;
        invalidate();
    }


    public ScaleTextView(Context context) {
        this(context, null);
    }

    public ScaleTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        normalTextSize = CommonUtils.dp2px(context, 13);
        selectedTextSize = CommonUtils.dp2px(context, 20);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ScaleTextView);
        normalTextColor = array.getColor(R.styleable.ScaleTextView_normalTextColor, normalTextColor);
        selectedTextColor = array.getColor(R.styleable.ScaleTextView_selectedTextColor, selectedTextColor);
        normalTextSize = array.getColor(R.styleable.ScaleTextView_normalTextSize, normalTextSize);
        selectedTextSize = array.getColor(R.styleable.ScaleTextView_selectedTextSize, selectedTextSize);
        array.recycle();
        setNormalTextSize(normalTextSize);
        setNormalTextColor(normalTextColor);

        initPaint();

    }


    public void setNormalTextSize(int normalTextSize) {
        this.normalTextSize = normalTextSize;
        setTextSize(normalTextSize);

    }

    public void setSelectedTextSize(int selectedTextSize) {
        this.selectedTextSize = selectedTextSize;
        setTextSize(selectedTextSize);

    }

    public void setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
        setTextColor(normalTextColor);

    }

    public void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        setTextColor(selectedTextColor);

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        drawText(canvas);


    }

    private Paint mPaint,linePaint;

    public void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        linePaint=new Paint();
        linePaint.setStrokeWidth(30);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(selectedTextColor);

    }

    private boolean isSelected;

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;

    }

    int width, height;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    /**
     * 绘制文字
     * 根据进度条来绘制文字从小到大，不断修改画笔的大小
     */
    public void drawText(Canvas canvas) {

        String text = getText().toString();
        Rect round = new Rect();

        if (isSelected) {
            mPaint.setColor(selectedTextColor);
        } else {
            mPaint.setColor(normalTextColor);
        }
        float textsize = normalTextSize + (selectedTextSize - normalTextSize) * progress;
        mPaint.setTextSize(textsize);
        mPaint.getTextBounds(text, 0, text.length(), round);

        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        float textHalfWidth = round.width() / 2;
        float widthHalf = width / 2;
        float baseLine = height / 2 + (metrics.bottom - metrics.top) / 2 - metrics.bottom;

        canvas.drawText(text, widthHalf - textHalfWidth, baseLine, mPaint);

        if (isSelected){
            canvas.drawLine(widthHalf - textHalfWidth,height,widthHalf+textHalfWidth ,height,linePaint);
        }

    }
}
