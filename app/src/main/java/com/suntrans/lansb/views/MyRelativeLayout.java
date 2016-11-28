package com.suntrans.lansb.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.suntrans.lansb.R;
import com.suntrans.lansb.utils.UiUtils;

/**
 * Created by Looney on 2016/11/28.
 */

public class MyRelativeLayout extends RelativeLayout {
    public MyRelativeLayout(Context context) {
        super(context);
        init();
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private int primaryColor;

    private void init() {
        primaryColor = UiUtils.getColor(getContext(), R.attr.colorPrimary);
        setBackgroundColor(primaryColor);
    }
}
