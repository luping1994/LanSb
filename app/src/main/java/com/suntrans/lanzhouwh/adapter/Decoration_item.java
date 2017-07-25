package com.suntrans.lanzhouwh.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.utils.UiUtils;


/**
 * Created by Looney on 2017/3/2.
 */

public class Decoration_item extends RecyclerView.ItemDecoration {
    private int tagWidth;
    private Paint paint;
    private Paint textpaint;

    public Decoration_item(Context context) {
        Paint.FontMetrics fontMetrics = new Paint.FontMetrics();
        paint = new Paint();
        paint.setColor(Color.GRAY);
    }


    private int dividerHeight = UiUtils.dip2px(40);
    private int oneDp = UiUtils.dip2px(1);
    private int headerHeight = UiUtils.dip2px(25);

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = oneDp;
    }


    int padding = UiUtils.dip2px(16);
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        int childCount = parent.getChildCount();
//        int left = padding;
//        int right = parent.getWidth() - padding;
//
//        for (int i = 0; i < childCount ; i++) {
//            View view = parent.getChildAt(i);
//            float top = view.getBottom();
//            float bottom = view.getBottom() + oneDp;
//            c.drawRect(left, top, right, bottom, paint);
//        }
    }
}
