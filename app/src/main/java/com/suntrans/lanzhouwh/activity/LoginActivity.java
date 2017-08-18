package com.suntrans.lanzhouwh.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.LoginResult.LoginResult;
import com.suntrans.lanzhouwh.bean.userinfo.UserInfos;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.ParcelableUtil;
import com.suntrans.lanzhouwh.utils.StatusBarCompat;
import com.suntrans.lanzhouwh.utils.UiUtils;
import com.suntrans.lanzhouwh.views.EditView;
import com.suntrans.lanzhouwh.views.LoadingDialog;
import com.trello.rxlifecycle.android.ActivityEvent;

import retrofit2.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.util.Base64.encodeToString;
import static com.suntrans.lanzhouwh.R.id.account;


public class LoginActivity extends BasedActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private Button login;
    private EditView ed_account;
    private EditView ed_password;
    TextView forget;
    Button activate;
    private LoadingDialog dialog1;
    public UserInfos userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        setContentView(R.layout.activity_login1);
//        StatusBarCompat.compat(this, Color.TRANSPARENT);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        login = (Button) findViewById(R.id.login);
        ed_account = (EditView) findViewById(account);
        ed_password = (EditView) findViewById(R.id.password);
//        forget = (TextView) findViewById(R.id.forget);
//        activate = (Button) findViewById(R.id.activate);

        String zhanghao = getSharedPreferences("config", Context.MODE_PRIVATE).getString("account", "");
        String mima = getSharedPreferences("config", Context.MODE_PRIVATE).getString("password", "");
        ed_account.setText(zhanghao);
//        ed_password.setText(mima);

//        forget.setOnClickListener(this);
        login.setOnClickListener(this);
//        activate.setOnClickListener(this);
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
            UiUtils.showToast("请输入账号");
            return;
        }
        if (password == null || password.equals("")) {
            UiUtils.showToast("请输入密码");
            return;
        }
        dialog1 = new LoadingDialog(this, R.style.loading_dialog);
        dialog1.setWaitText("正在登录..");
        dialog1.setCancelable(false);
        dialog1.show();
        RetrofitHelper.getLoginApi2().login2("password", "suntrans", "suntrans", account, password)
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
                        if (e instanceof HttpException) {
                            UiUtils.showToast("网络连接失败,请检查你的网络");
                        }
                        if (e.getMessage() != null) {
                            if (e.getMessage().equals("HTTP 401 Unauthorized") || e.getMessage().equals("HTTP 400 Bad Request")) {
                                UiUtils.showToast("账号或密码错误");
                            }
                        }
                        if (dialog1 != null)
                            dialog1.dismiss();

                    }

                    @Override
                    public void onNext(LoginResult info) {
                        if (info != null) {
                            if (info.access_token != null) {
                                if (!info.access_token.equals("")) {
                                    LogUtil.i(info.toString());
                                    SharedPreferences.Editor editor = App.getSharedPreferences().edit();
                                    editor.putString("expires_in1", info.expires_in);
                                    editor.putString("access_token1", info.access_token);
                                    editor.putLong("firsttime1", System.currentTimeMillis());
                                    editor.commit();
                                    handler.sendEmptyMessageDelayed(GETINFO, 300);
                                }
                            } else {
                                handler.sendEmptyMessage(LOGIN_FILED);
                            }
                        } else {
                            LogUtil.i(info.error_description);
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
                    intent.putExtra("info", userInfo);
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    break;
                case GETINFO:
                    getUserInfo();
                    break;
            }
        }
    };

    private void getUserInfo() {
        RetrofitHelper.getApi2().getUserInfo().compose(this.<UserInfos>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UserInfos>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(LOGIN_FILED);
                    }

                    @Override
                    public void onNext(UserInfos info) {

                        if (info == null) {
                            handler.sendEmptyMessage(LOGIN_FILED);
                            return;
                        }
                        if (info.getMessage() != null) {
                            handler.sendEmptyMessage(LOGIN_FILED);
                            return;
                        }
                        userInfo = info;
                        byte[] bytes = ParcelableUtil.marshall(info);
                        String s = Base64.encodeToString(bytes, Base64.DEFAULT);
                        App.getSharedPreferences().edit().putString("userinfo", s)
                                .putBoolean("isAdmin", info.getRusergid().equals("1") ? true : false)
                                .commit();
                        handler.sendMessage(Message.obtain(handler, LOGIN_SUCCESS));
                    }
                });
    }


}
