package com.suntrans.lansb.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntrans.lansb.R;
import com.suntrans.lansb.utils.UiUtils;

/**
 * Created by Looney on 2016/12/7.
 */

public class PersonalActivity extends AppCompatActivity {
    TextView tvRight;
    TextView titleName;
    private int theme = 0;
    LinearLayout leftIcon;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        if(savedInstanceState==null){
            theme= UiUtils.getAppTheme(this);
        }else{
            theme=savedInstanceState.getInt("theme");
        }
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        tvRight = (TextView) findViewById(R.id.tv_right);
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("个人中心");
        leftIcon = (LinearLayout) findViewById(R.id.left_icon);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
