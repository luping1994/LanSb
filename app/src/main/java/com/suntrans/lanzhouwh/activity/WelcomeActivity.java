package com.suntrans.lanzhouwh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.utils.LogUtil;

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
                    intent.putExtra("rusergid", "1");
                    intent.setClass(WelcomeActivity.this, MainActivity.class);
                    break;
            }
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    };

    private void initView() {
        String access_token = App.getSharedPreferences().getString("access_token", "-1");
        if (access_token.equals("-1")) {
            handler.sendEmptyMessageDelayed(START_LOGIN, 1800);
        } else {
            try {
                String expires_in = App.getSharedPreferences().getString("expires_in", "-1");
                long firsttime = App.getSharedPreferences().getLong("firsttime", -1l);
                long currenttime = System.currentTimeMillis();
                long d = (currenttime - firsttime) / 1000;
                LogUtil.i("时间差" + d);
                LogUtil.i("过期时间" + expires_in);
                LogUtil.i("过期时间-6天:" + (Long.valueOf(expires_in) - 6 * 24 * 3600));
                String a = d > (Long.valueOf(expires_in) - 6 * 24 * 3600)?"过期了":"没过期";
                LogUtil.i("是否过期:" + a);
                if (d > (Long.valueOf(expires_in) - 6 * 24 * 3600)) {
                    handler.sendEmptyMessageDelayed(START_LOGIN, 1800);
                } else {
                    handler.sendEmptyMessageDelayed(START_MAIN, 1800);
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.sendEmptyMessageDelayed(START_LOGIN, 1800);

            }

        }
    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
