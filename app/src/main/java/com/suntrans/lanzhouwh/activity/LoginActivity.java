package com.suntrans.lanzhouwh.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.LoginResult.LoginResult;
import com.suntrans.lanzhouwh.bean.userinfo.UserInfoResult;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.StatusBarCompat;
import com.suntrans.lanzhouwh.utils.UiUtils;
import com.suntrans.lanzhouwh.views.EditView;
import com.trello.rxlifecycle.android.ActivityEvent;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.suntrans.lanzhouwh.R.id.account;


public class LoginActivity extends BasedActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
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
//                startActivity(new Intent(LoginActivity.this, FindpasswordActivity.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.activate:
//                startActivity(new Intent(LoginActivity.this, ActivateStep1_Activity.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }

    }

    private void checkLogin() {
        String account = ed_account.getText().toString();
        String password = ed_password.getText().toString();
        if (account == null || account.equals("")) {
            UiUtils.showToast("账号为空");
            return;
        }
        if (password == null || password.equals("")) {
            UiUtils.showToast("密码为空");
            return;
        }
        dialog1 = new ProgressDialog(this);
        dialog1.setMessage("正在登录..");
        dialog1.setCancelable(false);
        dialog1.show();
        RetrofitHelper.getLoginApi().login("password", "test", "test", account, password)
                .compose(this.<LoginResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LoginResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(LOGIN_FILED);
                    }

                    @Override
                    public void onNext(LoginResult info) {
                        if (info != null) {
                            if (info.access_token != null) {
                                if (!info.access_token.equals("")) {
                                    LogUtil.i(info.toString());
                                    SharedPreferences.Editor editor = App.getSharedPreferences().edit();
                                    editor.putString("expires_in", info.expires_in);
                                    editor.putString("access_token", info.access_token);
                                    editor.putLong("firsttime", System.currentTimeMillis());
                                    editor.commit();
                                    handler.sendEmptyMessageDelayed(GETINFO, 300);
                                }
                            } else {
                                handler.sendEmptyMessage(LOGIN_FILED);
                            }
                        } else {
                            handler.sendEmptyMessage(LOGIN_FILED);
                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private final int LOGIN_FILED = 0;
    private final int LOGIN_SUCCESS = 1;
    private final int GETINFO = 2;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGIN_FILED:
                    dialog1.dismiss();
                    UiUtils.showToast("登录失败");
                    break;
                case LOGIN_SUCCESS:
                    dialog1.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra("rusergid", "1");
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    break;
                case GETINFO:
                    getDeviceList();
                    break;
            }
        }
    };

    private void getDeviceList() {
        RetrofitHelper.getApi().getUserInfo().compose(this.<UserInfoResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UserInfoResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(LOGIN_FILED);
                    }

                    @Override
                    public void onNext(UserInfoResult info) {
                        if (info == null) {
                            handler.sendEmptyMessage(LOGIN_FILED);
                            return;
                        }
                        if (info.user == null) {
                            handler.sendEmptyMessage(LOGIN_FILED);
                            return;

                        }
                        if (info.status_code.equals("0")) {
                            UiUtils.showToast("请求失败!请检查你的网络");
                            handler.sendEmptyMessage(LOGIN_FILED);
                            return;
                        }
                        App.getSharedPreferences().edit().putString("id", info.user.id).commit();
                        handler.sendMessage(Message.obtain(handler, LOGIN_SUCCESS));
                    }
                });
    }
}
