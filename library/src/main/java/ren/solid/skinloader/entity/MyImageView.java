package ren.solid.skinloader.entity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

import ren.solid.library.R;


/**
 * Created by Looney on 2017/1/3.
 */

public class MyImageView extends android.support.v7.widget.AppCompatImageView {


    private int filterColor;
    private static final int DEFAULT_FILTER_COLOR = Color.parseColor("#eb4f38");

    public MyImageView(Context context) {
        super(context);
        init();
    }


    public MyImageView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorFilterImage, defStyleAttr, 0);
        filterColor = a.getColor(R.styleable.ColorFilterImage_ColorFilter1,DEFAULT_FILTER_COLOR);
        a.recycle();
        init();
    }



    private void init() {
        this.clearColorFilter();
        this.setColorFilter(filterColor);
    }

    public void setFilterColor(int filterColor) {
        this.filterColor = filterColor;
        init();
        invalidate();
    }

}
