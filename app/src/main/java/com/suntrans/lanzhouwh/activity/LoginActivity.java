package com.suntrans.lanzhouwh.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.general.Result;
import com.suntrans.lanzhouwh.bean.general.UserInfo;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.StatusBarCompat;
import com.suntrans.lanzhouwh.utils.UiUtils;
import com.suntrans.lanzhouwh.views.EditView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.suntrans.lanzhouwh.R.id.account;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="LoginActivity" ;
    private Button login;
    private EditView ed_account;
    private EditView ed_password;
    Button forget;
    Button activate;
    private ProgressDialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarCompat.compat(this, Color.TRANSPARENT);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        login = (Button) findViewById(R.id.login);
        ed_account = (EditView) findViewById(account);
        ed_password = (EditView) findViewById(R.id.password);
        forget = (Button) findViewById(R.id.forget);
        activate = (Button) findViewById(R.id.activate);

        String zhanghao = getSharedPreferences("config", Context.MODE_PRIVATE).getString("account", "");
        String mima = getSharedPreferences("config", Context.MODE_PRIVATE).getString("password", "");
        ed_account.setText(zhanghao);
        ed_password.setText(mima);

        forget.setOnClickListener(this);
        login.setOnClickListener(this);
        activate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login: {
                checkLogin();
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
            }
            break;

            case R.id.forget:
                startActivity(new Intent(LoginActivity.this, FindpasswordActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.activate:
                startActivity(new Intent(LoginActivity.this, ActivateStep1_Activity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }

    }

    private void checkLogin() {


        String account = ed_account.getText().toString();
        String password = ed_password.getText().toString();
        if (account==null||account.equals("")){
            UiUtils.showToast("账号为空");
            return;
        }
        if (password==null||password.equals("")){
            UiUtils.showToast("密码为空");
            return;
        }
        dialog1 = new ProgressDialog(this);
        dialog1.setMessage("正在登录..");
        dialog1.show();

        RetrofitHelper.getApi().login(account, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        UiUtils.showToast("登录失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Result result) {
                        if (result == null) {
                            dialog1.dismiss();
                            UiUtils.showToast("登录失败");
                            return;
                        }
                        if (result.result.equals("ok")) {
                            SharedPreferences.Editor editor = App.getSharedPreferences().edit();
                            editor.putString("rusername",result.data.ds.get(0).rusername);
                            editor.commit();
                            handler.sendEmptyMessageDelayed(LOGIN_SUCCESS,800);
                            LogUtil.i("登录成功,用户名为==>"+result.data.ds.get(0).rusername);
                        } else {
                            dialog1.dismiss();
                            UiUtils.showToast(result.reason);
                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }



    private   final int LOGIN_SUCCESS=0;
    private   final int GETUSERINFO_SUCCESS=1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    getUserInfo();
                    break;
                case GETUSERINFO_SUCCESS:
                    dialog1.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra("rusergid",(String) msg.obj);
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    break;
            }
        }
    };




    private void getUserInfo() {
        String account = App.getSharedPreferences().getString("rusername", "-1");
        if (account.equals("-1")) {
            LogUtil.w(TAG, "账号不存在");
            return;
        }
        RetrofitHelper.getApi().getUserInfo(account)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog1.dismiss();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Result result) {
                        if (result != null) {
                            UserInfo info = result.data.ds.get(0);
                            LogUtil.i(TAG,info.toString());
                            SharedPreferences.Editor editor = App.getSharedPreferences().edit();
                            editor.putString("deptidlist",info.deptidlist);
                            editor.putString("ruid",info.ruid);
                            editor.putString("rusername",info.rusername);
                            editor.putString("rusergid",info.rusergid);
                            editor.commit();
                            Message msg = new Message();
                            msg.what=GETUSERINFO_SUCCESS;
                            msg.obj = info.rusergid;
                            handler.sendMessageDelayed(msg,300);
                        }else {
                            dialog1.dismiss();
                        }
                    }
                });
    }
}
