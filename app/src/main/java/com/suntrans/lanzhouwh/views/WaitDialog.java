package com.suntrans.lanzhouwh.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.suntrans.lanzhouwh.R;

/**
 * Created by Looney on 2017/1/13.
 */

public class WaitDialog extends Dialog {
    private Context context;
    private TextView mWaitTv = null;
    private String mWaitTxt = null;

    public WaitDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        mWaitTv = (TextView) findViewById(R.id.wait_tv);
        if (mWaitTxt != null && !mWaitTxt.isEmpty()) {
            mWaitTv.setVisibility(View.VISIBLE);
            mWaitTv.setText(mWaitTxt);
        } else {
            mWaitTv.setVisibility(View.GONE);
        }
    }


    public void setWaitText(String str) {
        mWaitTxt = str;
        if (mWaitTv == null)
            return;
        if (mWaitTxt != null && !mWaitTxt.isEmpty()) {
            mWaitTv.setVisibility(View.VISIBLE);
            mWaitTv.setText(mWaitTxt);
        } else {
            mWaitTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void show() {
        if (context != null && !((Activity) context).isFinishing()) {
            try {
                super.show();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void dismiss() {
        if (context != null && !((Activity) context).isFinishing()) {
            try {
                super.dismiss();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void cancel() {
        if (context != null && !((Activity) context).isFinishing()) {
            try {
                super.cancel();

            } catch (Exception e) {

            }
        }
    }
}
