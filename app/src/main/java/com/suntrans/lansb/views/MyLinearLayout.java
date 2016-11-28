package com.suntrans.lansb.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.suntrans.lansb.R;
import com.suntrans.lansb.utils.UiUtils;


/**
 * Created by Looney on 2016/11/28.
 */

public class MyLinearLayout extends LinearLayout {
    public MyLinearLayout(Context context) {
        super(context);
        init();
    }



    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private int primaryColor;
    private void init() {
        primaryColor = UiUtils.getColor(getContext(), R.attr.colorPrimary);
        setBackgroundColor(primaryColor);
    }
}
