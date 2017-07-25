package com.suntrans.lanzhouwh.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import android.content.Context;
/**
 * Created by Looney on 2017/3/10.
 */

public class ReCharge_activity extends BasedActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.bt1)
    RadioButton bt1;
    @BindView(R.id.bt2)
    RadioButton bt2;
    @BindView(R.id.bt3)
    RadioButton bt3;
    @BindView(R.id.zfb_rightimg)
    ImageView zfbRightimg;
    @BindView(R.id.rl_zhb)
    RelativeLayout rlZhb;
    @BindView(R.id.wx_rightimg)
    ImageView wxRightimg;
    @BindView(R.id.rl_wx)
    RelativeLayout rlWx;
    @BindView(R.id.qita)
    EditText qita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        setUpToolBar();
        initView();
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }

    private int payway = 1;

    private void initView() {
        qita.setFocusable(false);
        zfbRightimg.setImageResource(R.drawable.ic_select);
        wxRightimg.setImageResource(R.drawable.ic_unselect);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                bt1.setBackgroundResource(R.drawable.layout_border);
                bt2.setBackgroundResource(R.drawable.layout_border);
                bt3.setBackgroundResource(R.drawable.layout_border);

                bt3.setTextColor(Color.BLACK);
                bt2.setTextColor(Color.BLACK);
                bt1.setTextColor(Color.BLACK);

                group.findViewById(checkedId).setBackgroundResource(R.drawable.bg_select);
                ((RadioButton) group.findViewById(checkedId)).setTextColor(Color.WHITE);
            }
        });
        bt1.setChecked(true);
        rlWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payway = 1;
                wxRightimg.setImageResource(R.drawable.ic_select);
                zfbRightimg.setImageResource(R.drawable.ic_unselect);
            }
        });
        rlZhb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payway = 2;
                wxRightimg.setImageResource(R.drawable.ic_unselect);
                zfbRightimg.setImageResource(R.drawable.ic_select);
            }
        });
        qita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        qita.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etNameGetFocus((EditText) v);
                } else {
                    etNameLostFocus((EditText)v);
                }
            }
        });
    }

    public void setUpToolBar() {

        toolbar.setTitle("充值中心");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // 获取焦点
    private void etNameGetFocus(final EditText etName) {
        etName.requestFocus();
        etName.setGravity(Gravity.START);
        etName.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) etName.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(etName, 0);
            }
        });
        // 光标置于文字最后
        etName.setSelection(etName.getText().length());
    }
    // 重置edittext, 居中并失去焦点
    private void etNameLostFocus(EditText etName) {
        etName.clearFocus();
        InputMethodManager manager = (InputMethodManager) etName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(etName.getWindowToken(), 0);
    }
}
