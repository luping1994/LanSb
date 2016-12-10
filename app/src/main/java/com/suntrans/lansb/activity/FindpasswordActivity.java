package com.suntrans.lansb.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntrans.lansb.R;
import com.suntrans.lansb.utils.StatusBarCompat;

/**
 * Created by Looney on 2016/12/8.
 */

public class FindpasswordActivity extends AppCompatActivity {
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
        leftIcon.setImageResource(R.drawable.ic_back);
    }
}
