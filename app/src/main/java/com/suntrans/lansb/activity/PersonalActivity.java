package com.suntrans.lansb.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
    }
}
