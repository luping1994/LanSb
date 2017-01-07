package com.suntrans.lanzhouwh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;

/**
 * Created by Looney on 2017/1/6.
 */

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    final int START_LOGIN = 0;
    final int START_MAIN = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            switch (msg.what) {
                case START_LOGIN:
                    intent.setClass(WelcomeActivity.this, LoginActivity.class);
                    break;
                case START_MAIN:
                    String rusergid = App.getSharedPreferences().getString("rusergid", "-1");
                    if (!rusergid.equals("-1"))
                        intent.putExtra("rusergid",rusergid);
                    intent.setClass(WelcomeActivity.this, MainActivity.class);
                    break;
            }
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    };

    private void initView() {
        String username = App.getSharedPreferences().getString("rusername", "-1");
        if (username.equals("-1")) {
            handler.sendEmptyMessageDelayed(START_LOGIN, 1800);
        } else {
            handler.sendEmptyMessageDelayed(START_MAIN, 1800);
        }
    }

}
