package com.suntrans.lansb.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.suntrans.lansb.R;
import com.suntrans.lansb.utils.StatusBarCompat;
import com.suntrans.lansb.views.EditView;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login ;
    private EditView ed_account;
    private EditView ed_password;
    private String account;
    private String password;
    private AlertDialog dialog;
    Button forget;
    Button activate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarCompat.compat(this, Color.TRANSPARENT);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        login = (Button) findViewById(R.id.login);
        ed_account = (EditView) findViewById(R.id.account);
        ed_password = (EditView) findViewById(R.id.password);
        forget = (Button) findViewById(R.id.forget);
        activate = (Button) findViewById(R.id.activate);

        String zhanghao = getSharedPreferences("config", Context.MODE_PRIVATE).getString("account","");
        String mima = getSharedPreferences("config", Context.MODE_PRIVATE).getString("password","");
        ed_account.setText(zhanghao);
        ed_password.setText(mima);

        forget.setOnClickListener(this);
        login.setOnClickListener(this);
        activate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        switch (v.getId()){
            case R.id.login:{
                showDialog();
            }
            break;
            case R.id.bt_yezhu:
                intent.putExtra("type",0);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.bt_yuangong:
                intent.putExtra("type",1);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.bt_tuichu:
                dialog.dismiss();
                break;
            case R.id.forget:
                startActivity(new Intent(LoginActivity.this,FindpasswordActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.activate:
                startActivity(new Intent(LoginActivity.this,ActivateActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }

    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.dialog_login,null);
        builder.setView(view);
        dialog = builder.create();
        Button yezhu = (Button) view.findViewById(R.id.bt_yezhu);
        Button yuangong = (Button) view.findViewById(R.id.bt_yuangong);
        Button tuichu = (Button) view.findViewById(R.id.bt_tuichu);
        yezhu.setOnClickListener(this);
        yuangong.setOnClickListener(this);
        tuichu.setOnClickListener(this);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        if (dialog!=null){
            dialog.dismiss();
        }
        super.onDestroy();
    }
}
