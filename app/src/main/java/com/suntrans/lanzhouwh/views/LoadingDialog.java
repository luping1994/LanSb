package com.suntrans.lanzhouwh.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.utils.UiUtils;


public class LoadingDialog extends Dialog {

    private Context mContext;

    private TextView mWaitTv = null;

    private String mWaitingTxt = null;

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        mWaitTv = (TextView) findViewById(R.id.msg);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        getWindow().setGravity(Gravity.TOP);
//        params.x = UiUtils.dip2px(16);
        params.y = UiUtils.dip2px(60);
        Display display = ((Activity)mContext).getWindowManager().getDefaultDisplay();
        params.width = display.getWidth()*5/6;
        params.height = UiUtils.dip2px(40);
        getWindow().setAttributes(params);
        if (mWaitingTxt != null && !mWaitingTxt.isEmpty()) {
            mWaitTv.setVisibility(View.VISIBLE);
            mWaitTv.setText(mWaitingTxt);
        } else {
            mWaitTv.setVisibility(View.GONE);
        }
    }

    public void setWaitText(String text) {
        mWaitingTxt = text;
        if (mWaitTv == null) {
            return;
        }
        if (mWaitingTxt != null && !mWaitingTxt.isEmpty()) {
            mWaitTv.setVisibility(View.VISIBLE);
            mWaitTv.setText(text);
        } else {
            mWaitTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void show() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.show();
            } catch (Exception e) {
            }
        }

    }

    @Override
    public void dismiss() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.dismiss();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void cancel() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.cancel();
            } catch (Exception e) {
            }
        }
    }
}
