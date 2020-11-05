package com.cici.cicimobileassistant.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;

import com.cici.cicimobileassistant.R;

/**
 * 下载按钮
 * 状态：未下载，下载中（继续下载），暂停，
 */
public class DownloadButton extends AppCompatButton {

    public static final int DOWNLOAD = 1;
    public static final int DOWNLOADING = 2;
    public static final int PAUSE = 3;
    public static final int FINISH = 4;
    private Bitmap downloadImg, downloadingImg, pauseImg;

    private int status = DOWNLOAD;

    private RectF rectF;//圆形外框区域

    private float progress = 0f;
    private int width, height;

    public void setStatus(int status) {
        this.status = status;
        invalidate();

    }
    public void setProgress(float progress) {
        this.progress = progress;
        if (progress<=0){

           return;
        }
        invalidate();

    }


    public int getStatus() {
        return status;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DownloadButton(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DownloadButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DownloadButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        downloadImg = BitmapFactory.decodeResource(getResources(), R.drawable.icn_download_cloud);
        downloadingImg = BitmapFactory.decodeResource(getResources(), R.drawable.icn_paused_64);
        pauseImg = BitmapFactory.decodeResource(getResources(), R.drawable.icn_sque_64);

        width = downloadImg.getWidth();
        height = downloadImg.getHeight();


        mRadius = (width - progressWidth) / 2;
        centerX = width / 2;
        centerY = height / 2;

        initPaint();


    }

    /**
     * 计算控件宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(width, height);

    }

    private Paint circlePaint, textPaint, rectPaint, arcPaint;
    private int mRadius;
    private int centerX, centerY;

    public void initPaint() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(getResources().getColor(R.color.blue));
        circlePaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(dp2px(12));
        textPaint.setColor(getResources().getColor(R.color.blue));
        textPaint.setStyle(Paint.Style.STROKE);

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setTextSize(dp2px(2));
        rectPaint.setColor(getResources().getColor(R.color.blue));
        rectPaint.setStyle(Paint.Style.STROKE);

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStrokeWidth(progressWidth);
        arcPaint.setColor(getResources().getColor(R.color.blue));
        arcPaint.setStyle(Paint.Style.STROKE);
    }

    private String finishText = "运行";
    private int progressWidth = dp2px(3);

    /**
     * 绘制：都需要加个外框圆
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rectF = new RectF();//缩放图片区域
        rectF.set(width / 4, width / 4, width / 2 + width / 4, width / 2 + width / 4);

        switch (status) {
            case DOWNLOAD:

                canvas.drawBitmap(downloadImg, null, rectF, circlePaint);
                canvas.drawCircle(centerX, centerY, mRadius, circlePaint);
                break;

            case DOWNLOADING://需要绘制进度条
                canvas.drawBitmap(downloadingImg, null, rectF, circlePaint);
                canvas.drawCircle(centerX, centerY, mRadius, circlePaint);
                //绘制进度,圆弧，从-90到270

                RectF arcF = new RectF(width / 2 - mRadius, width / 2 - mRadius, width / 2 + mRadius, width / 2 + mRadius);

                canvas.drawArc(arcF, -90, 360 * progress, false, arcPaint);

                break;

            case PAUSE:
                canvas.drawBitmap(pauseImg, null, rectF, circlePaint);
                //外圆
                canvas.drawCircle(centerX, centerY, mRadius, circlePaint);

                break;


            case FINISH://绘制运行文字加方形外框

                int rectWidth = width;
                int rectHeight = (int) (height / 1.5);

                Rect bound = new Rect();
                textPaint.getTextBounds(finishText, 0, finishText.length(), bound);

                Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();

                int x = (width - bound.width()) / 2;
                float dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;

                canvas.drawText(finishText, x, rectHeight / 2 + dy, textPaint);

                Rect rect = new Rect(0, 0, rectWidth, rectHeight);

                canvas.drawRect(rect, rectPaint);
                break;

        }


    }


    private int dp2px(int dp) {
        return (int) ((getResources().getDisplayMetrics().density * dp) + 0.5);
    }
}
