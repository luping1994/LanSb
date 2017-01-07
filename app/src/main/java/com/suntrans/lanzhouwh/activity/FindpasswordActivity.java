package com.suntrans.lanzhouwh.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BaseActivity;
import com.suntrans.lanzhouwh.utils.StatusBarCompat;

/**
 * Created by Looney on 2016/12/8.
 */

public class FindpasswordActivity extends BaseActivity {
    ImageView leftIcon;
    TextView titleName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);
        StatusBarCompat.compat(this, Color.TRANSPARENT);
        initView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    private void initView() {
        leftIcon = (ImageView) findViewById(R.id.left_image);
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("忘记密码");
        leftIcon.setImageResource(android.support.design.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
