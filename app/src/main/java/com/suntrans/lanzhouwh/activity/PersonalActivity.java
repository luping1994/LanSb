package com.suntrans.lanzhouwh.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;

/**
 * Created by Looney on 2016/12/7.
 */

public class PersonalActivity extends BasedActivity {
    TextView tvRight;
    TextView titleName;
    private int theme = 0;
    LinearLayout leftIcon;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){

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

    public void signOut(View view){
        new AlertDialog.Builder(this)
                .setMessage("是否要退出登录?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        killAll();
                        SharedPreferences.Editor editor = App.getSharedPreferences().edit();
                        editor.putString("access_token","-1");
                        editor.putString("expires_in","-1");
                        editor.putLong("firsttime",-1l);
                        editor.commit();
                        Intent intent = new Intent(PersonalActivity.this,LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_slide_in_bottom, android.support.v7.appcompat.R.anim.abc_slide_out_bottom);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }
}
