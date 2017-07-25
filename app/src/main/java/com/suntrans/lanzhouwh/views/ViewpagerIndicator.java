package com.suntrans.lanzhouwh.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.utils.UiUtils;

import static com.suntrans.lanzhouwh.R.drawable.add;

/**
 * Created by Looney on 2017/3/21.
 */

public class ViewpagerIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;

    int mPointWidth;// 圆点间的距离
    private int indicator_select = UiUtils.dip2px(5);
    private int indicator_unselect = UiUtils.dip2px(3);
    private LinearLayout.LayoutParams params_select;
    private LinearLayout.LayoutParams params_unselected;
    protected int pointSpace = UiUtils.dip2px(10);


    public ViewpagerIndicator(Context context) {
        super(context, null);
    }

    public ViewpagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public ViewpagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        params_select = new LinearLayout.LayoutParams(
                indicator_select, indicator_select);
        params_unselected = new LinearLayout.LayoutParams(
                indicator_unselect, indicator_unselect);

        params_select.leftMargin = pointSpace;// 设置圆点间隔
        params_unselected.leftMargin = pointSpace;// 设置圆点间隔

    }





    public void setUpViewPager(ViewPager pager) {
        this.viewPager = pager;
        viewPager.addOnPageChangeListener(this);
        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            View point = new View(getContext());
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_point_red);// 设置引导页默认圆点
                point.setLayoutParams(params_select);// 设置圆点的大小
                this.addView(point);// 将圆点添加给线性布局
            } else {
                point.setBackgroundResource(R.drawable.shape_point_gray);// 设置引导页默认圆点
                point.setLayoutParams(params_unselected);// 设置圆点的大小
                this.addView(point);// 将圆点添加给线性布局
            }
        }
       getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
           @Override
           public void onGlobalLayout() {
               getViewTreeObserver()
                       .removeGlobalOnLayoutListener(this);
               mPointWidth = getChildAt(1).getLeft()
                       - getChildAt(0).getLeft();
           }
       });
        invalidate();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < this.getChildCount(); i++) {
            if (i == position) {
                this.getChildAt(i).setBackgroundResource(R.drawable.shape_point_red);
                this.getChildAt(i).setLayoutParams(params_select);
            } else {
                this.getChildAt(i).setBackgroundResource(R.drawable.shape_point_gray);
                this.getChildAt(i).setLayoutParams(params_unselected);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
