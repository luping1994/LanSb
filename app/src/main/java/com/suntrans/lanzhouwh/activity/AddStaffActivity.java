package com.suntrans.lanzhouwh.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BaseActivity;

/**
 * Created by Looney on 2016/12/8.
 */

public class AddStaffActivity extends BaseActivity {
    ImageView leftIcon;
    TextView titleName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstaff);
        initView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    private void initView() {
        leftIcon = (ImageView) findViewById(R.id.left_image);
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("添加员工");
        leftIcon.setImageResource(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    }
}
