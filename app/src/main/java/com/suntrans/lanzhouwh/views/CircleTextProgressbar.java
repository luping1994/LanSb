package com.suntrans.lanzhouwh.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.UiUtils;

/**
 * Created on 2016/7/12.
 *
 * @author Yan Zhenjie.
 */
public class CircleTextProgressbar extends android.support.v7.widget.AppCompatTextView {

    private static final String TAG = "CircleTextProgressbar";
    /**
     * 外部轮廓的颜色。
     */
    private int outLineColor;

    /**
     * 外部轮廓的宽度。
     */
    private int outLineWidth = UiUtils.dip2px(8);

    /**
     * 内部圆的颜色。
     */
    private ColorStateList inCircleColors = ColorStateList.valueOf(Color.TRANSPARENT);
    /**
     * 中心圆的颜色。
     */
    private int circleColor;

    /**
     * 进度条的颜色。
     */
    private int progressLineColor = Color.BLUE;

    /**
     * 进度条的宽度。
     */
    private int progressLineWidth = 8;

    /**
     * 画笔。
     */
    private Paint mPaint = new Paint();

    /**
     * 进度条的矩形区域。
     */
    private RectF mArcRect = new RectF();

    /**
     * 进度。
     */
    private int progress = 0;
    /**
     * 进度条类型。
     */
    private ProgressType mProgressType = ProgressType.COUNT;
    /**
     * 进度倒计时时间。
     */
    private long timeMillis = 2000;

    /**
     * View的显示区域。
     */
    final Rect bounds = new Rect();
    /**
     * 进度条通知。
     */
    private OnCountdownProgressListener mCountdownProgressListener;
    /**
     * Listener what。
     */
    private int listenerWhat = 0;

    private int outStrokeWidth = UiUtils.dip2px(20);
    private int progress_copy;


    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        invalidate();
    }

    private float maxValue = 100;

    public CircleTextProgressbar(Context context) {
        this(context, null);
    }

    public CircleTextProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleTextProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }


    /**
     * 初始化。
     *
     * @param context      上下文。
     * @param attributeSet 属性。
     */
    private void initialize(Context context, AttributeSet attributeSet) {
        mPaint.setAntiAlias(true);
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CircleTextProgressbar);
        if (typedArray.hasValue(R.styleable.CircleTextProgressbar_in_circle_color))
            inCircleColors = typedArray.getColorStateList(R.styleable.CircleTextProgressbar_in_circle_color);
        else
            inCircleColors = ColorStateList.valueOf(Color.TRANSPARENT);
        circleColor = inCircleColors.getColorForState(getDrawableState(), Color.TRANSPARENT);
        maxValue = typedArray.getInt(R.styleable.CircleTextProgressbar_maxValue, 100);
        outLineColor = typedArray.getColor(R.styleable.CircleTextProgressbar_outLineColor, Color.parseColor("#8e8f92"));
//        outLineColor = typedArray.getColor(R.styleable.CircleTextProgressbar_outLineColor,Color.parseColor("#88888888"));
        typedArray.recycle();
    }

    /**
     * 设置外部轮廓的颜色。
     *
     * @param outLineColor 颜色值。
     */
    public void setOutLineColor(@ColorInt int outLineColor) {
        this.outLineColor = outLineColor;
        invalidate();
    }

    /**
     * 设置外部轮廓的颜色。
     *
     * @param outLineWidth 颜色值。
     */
    public void setOutLineWidth(@ColorInt int outLineWidth) {
        this.outLineWidth = outLineWidth;
        invalidate();
    }

    /**
     * 设置圆形的填充颜色。
     *
     * @param inCircleColor 颜色值。
     */
    public void setInCircleColor(@ColorInt int inCircleColor) {
        this.inCircleColors = ColorStateList.valueOf(inCircleColor);
        invalidate();
    }

    /**
     * 是否需要更新圆的颜色。
     */
    private void validateCircleColor() {
        int circleColorTemp = inCircleColors.getColorForState(getDrawableState(), Color.TRANSPARENT);
        if (circleColor != circleColorTemp) {
            circleColor = circleColorTemp;
            invalidate();
        }
    }

    /**
     * 设置进度条颜色。
     *
     * @param progressLineColor 颜色值。
     */
    public void setProgressColor(@ColorInt int progressLineColor) {
        this.progressLineColor = progressLineColor;
        invalidate();
    }

    /**
     * 设置进度条线的宽度。
     *
     * @param progressLineWidth 宽度值。
     */
    public void setProgressLineWidth(int progressLineWidth) {
        this.progressLineWidth = progressLineWidth;
        invalidate();
    }

    /**
     * 设置进度。
     *
     * @param progress 进度。
     */
    public void setProgress(int progress) {
//        this.progress = validateProgress(progress);
        this.progress_copy = validateProgress(progress);
        invalidate();
    }

    /**
     * 验证进度。
     *
     * @param progress 你要验证的进度值。
     * @return 返回真正的进度值。
     */
    private int validateProgress(int progress) {
        if (progress < 0)
            progress = 0;
        return progress;
    }

    /**
     * 拿到此时的进度。
     *
     * @return 进度值，最大100，最小0。
     */
    public int getProgress() {
        return progress;
    }

    /**
     * 设置倒计时总时间。
     *
     * @param timeMillis 毫秒。
     */
    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
        invalidate();
    }

    /**
     * 拿到进度条计时时间。
     *
     * @return 毫秒。
     */
    public long getTimeMillis() {
        return this.timeMillis;
    }

    /**
     * 设置进度条类型。
     *
     * @param progressType {@link ProgressType}.
     */
    public void setProgressType(ProgressType progressType) {
        this.mProgressType = progressType;
        resetProgress();
        invalidate();
    }

    /**
     * 重置进度。
     */
    private void resetProgress() {
        progress = 0;
    }

    /**
     * 拿到进度条类型。
     *
     * @return
     */
    public ProgressType getProgressType() {
        return mProgressType;
    }

    /**
     * 设置进度监听。
     *
     * @param mCountdownProgressListener 监听器。
     */
    public void setCountdownProgressListener(int what, OnCountdownProgressListener mCountdownProgressListener) {
        this.listenerWhat = what;
        this.mCountdownProgressListener = mCountdownProgressListener;
    }

    /**
     * 开始。
     */
    public void start() {
        stop();
        post(progressChangeTask);
    }


    boolean isBreak = false;


    class AnimationThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < progress_copy; i++) {
                if (isBreak)
                    break;
                progress = i;
                post(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 重新开始。
     */
    public void reStart() {
        resetProgress();
        start();
    }

    /**
     * 停止。
     */
    public void stop() {
        isBreak = true;
        removeCallbacks(progressChangeTask);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取view的边界
        getDrawingRect(bounds);
        RectF f = new RectF(bounds.left + outStrokeWidth, bounds.top + outStrokeWidth, bounds.right - outStrokeWidth, bounds.bottom - outStrokeWidth);

        int size = bounds.height() > bounds.width() ? bounds.width() : bounds.height();
        float outerRadius = size / 2;

        //画内部背景
//        int circleColor = inCircleColors.getColorForState(getDrawableState(), 0);
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setColor(circleColor);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setStrokeWidth(UiUtils.dip2px(8));
//        canvas.drawCircle(bounds.centerX(), bounds.centerY(), outerRadius - outLineWidth, mPaint);

        //画边框圆
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(outLineWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(outLineColor);
        canvas.drawArc(f, 120, 300, false, mPaint);
//        canvas.drawCircle(bounds.centerX(), bounds.centerY(), outerRadius - outLineWidth / 2, mPaint);

        //画字
        Paint paint = getPaint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        float textY = bounds.centerY() - (paint.descent() + paint.ascent()) / 2;
        canvas.drawText(getText().toString(), bounds.centerX(), textY, paint);

        //画起始和终止值
        float x = calculateX(f.centerX() * 1.05f, -30);
        float y = calculateY(f.centerX() * 1.05f, -30);
        canvas.drawText("0", x, y, paint);

        float x1 = calculateX(f.centerX() * 1.05f, 30);
        float y1 = calculateY(f.centerX() * 1.05f, 30);
        canvas.drawText(maxValue + "", x1, y1, paint);

        //画进度条
        mPaint.setColor(progressLineColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(outLineWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

//      int deleteWidth = progressLineWidth + outLineWidth;
//      mArcRect.set(bounds.left + deleteWidth / 2, bounds.top + deleteWidth / 2, bounds.right - deleteWidth / 2, bounds.bottom - deleteWidth / 2);
        if (progress != 0)
            canvas.drawArc(f, 120, ((float) progress / (maxValue / 300.00f)), false, mPaint);
    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int lineWidth = 4 * (outLineWidth + progressLineWidth);
//        int width = getMeasuredWidth();
//        int height = getMeasuredHeight();
//        int size = (width > height ? width : height) + lineWidth;
//        setMeasuredDimension(size, size);
//    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        validateCircleColor();
    }

    /**
     * 进度更新task。
     */
    private Runnable progressChangeTask = new Runnable() {
        @Override
        public void run() {
            removeCallbacks(this);
            progress += 1;
            if (progress > progress_copy)
                return;
            if (progress >= 0 && progress <= 100) {
                if (mCountdownProgressListener != null)
                    mCountdownProgressListener.onProgress(listenerWhat, progress);
                invalidate();
                postDelayed(progressChangeTask, timeMillis / 100);
            } else
                progress = validateProgress(progress);
        }
    };

//

    /**
     * 进度条类型。
     */
    public enum ProgressType {
        /**
         * 顺数进度条，从0-100；
         */
        COUNT,

        /**
         * 倒数进度条，从100-0；
         */
        COUNT_BACK;
    }

    /**
     * 进度监听。
     */
    public interface OnCountdownProgressListener {

        /**
         * 进度通知。
         *
         * @param progress 进度值。
         */
        void onProgress(int what, int progress);
    }

    /**
     * @param r
     * @param angle
     * @return
     */
    private float calculateX(float r, double angle) {
        angle = angle * ((2 * Math.PI) / 360);
        double x = r * Math.sin(angle);
        double xFinal = (double) bounds.centerX() + x;
//        Log.i(TAG, "angle = " + angle + ",Math.sin(angle) = " + Math.sin(angle) + "xfinal=" + xFinal);
        return (float) xFinal;
    }

    /**
     * 根据半径和角度计算y坐标
     */
    private float calculateY(float r, double angle) {
        angle = angle * ((2 * Math.PI) / 360);
//        Log.i(TAG, "angle = " + angle + ",Math.cos(angle) = " + Math.cos(angle));
        double y = r * Math.cos(angle);
        double yFinal = (double) bounds.centerY() + y;
        return (float) yFinal;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    private Handler handler = new Handler();
}
