package com.suntrans.lanzhouwh.views;

/**
 * Created by Looney on 2017/3/11.
 */

import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.content.Context;
import android.widget.TextView;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.ThreadManager;
import com.suntrans.lanzhouwh.utils.UiUtils;

/***
 * 创建了自定义帧布局 把baseFragment 一部分代码 抽取到这个类中
 *
 * @author itcast
 *
 */
public abstract class LoadingPage extends FrameLayout {

    public static final int STATE_UNKOWN = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_SUCCESS = 4;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        showPage();
    }

    public int state = STATE_UNKOWN;

    private View loadingView;// 加载中的界面
    private View errorView;// 错误界面
    private View emptyView;// 空界面
    private View successView;// 加载成功的界面

    public LoadingPage(Context context) {
        super(context);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        loadingView = createLoadingView(); // 创建了加载中的界面
        if (loadingView != null) {
            this.addView(loadingView, new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        errorView = createErrorView(); // 加载错误界面
        if (errorView != null) {
            this.addView(errorView, new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        emptyView = createEmptyView(); // 加载空的界面
        if (emptyView != null) {
            this.addView(emptyView, new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        successView = createSuccessView();
        if (successView != null) {
            this.addView(successView, new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        showPage();// 根据不同的状态显示不同的界面
    }

    // 根据不同的状态显示不同的界面
    public void showPage() {
        LogUtil.i("Loadpage","state="+state);
        if (loadingView != null) {
            loadingView.setVisibility(state == STATE_UNKOWN
                    || state == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
        }
        if (errorView != null) {
            errorView.setVisibility(state == STATE_ERROR ? View.VISIBLE
                    : View.INVISIBLE);
        }
        if (emptyView != null) {
            emptyView.setVisibility(state == STATE_EMPTY ? View.VISIBLE
                    : View.INVISIBLE);
        }
        if (state == STATE_SUCCESS) {
            if (successView == null) {
                successView = createSuccessView();
                this.addView(successView, new FrameLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
            successView.setVisibility(View.VISIBLE);
        } else {
            if (successView != null) {
                successView.setVisibility(View.INVISIBLE);
            }
        }
    }

    /* 创建了空的界面 */
    private View createEmptyView() {
        View view = LayoutInflater.from(getContext()).inflate( R.layout.loadpage_empty,
                null);
        return view;
    }

    /* 创建了错误界面 */
    private View createErrorView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.loadpage_error,
                null);
        Button page_bt = (Button) view.findViewById(R.id.bt_error);
        page_bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setState(STATE_LOADING);
                load();
            }
        });
        return view;
    }

    /* 创建加载中的界面 */
    private View createLoadingView() {
        View view =LayoutInflater.from(getContext()).inflate(
                R.layout.loadpage_loading, null);
        return view;
    }

    public enum LoadResult {
        error(2), empty(3), success(4);
        int value;

        LoadResult(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    // 根据服务器的数据 切换状态
//    public void show() {
//        if (state == STATE_ERROR || state == STATE_EMPTY) {
//            state = STATE_LOADING;
//        }
//        showPage();
//    }
    /***
     * 创建成功的界面
     *
     * @return
     */
    public abstract View createSuccessView();

    /**
     * 请求服务器
     *
     * @return
     */
    protected abstract void load();
}