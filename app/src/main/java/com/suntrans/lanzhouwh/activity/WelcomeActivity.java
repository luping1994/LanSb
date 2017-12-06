package com.suntrans.lanzhouwh.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.WindowManager;

import com.github.mikephil.charting.formatter.IFillFormatter;
import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.LoginResult.LoginResult;
import com.suntrans.lanzhouwh.bean.userinfo.UserInfos;
import com.suntrans.lanzhouwh.utils.Encryp;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.ParcelableUtil;
import com.suntrans.lanzhouwh.utils.UiUtils;
import com.trello.rxlifecycle.android.ActivityEvent;

import retrofit2.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/1/6.
 */

public class WelcomeActivity extends BasedActivity {

    private UserInfos info;
    private boolean isRemote = false;
    private String gotoType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    final int START_LOGIN = 0;
    final int START_MAIN = 1;
    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            switch (msg.what) {
                case START_LOGIN:
                    intent.setClass(WelcomeActivity.this, LoginActivity.class);
                    break;
                case START_MAIN:
                    intent.putExtra("info", info);
                    intent.setClass(WelcomeActivity.this, MainActivity.class);
                    break;
            }
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    };

    private void initView() {
//        String account = getIntent().getStringExtra("username");
//        if (account != null) {
//            checkRemoteAccount(account);
//        } else {
        checkLocalState();
//        }
    }

    private void checkLocalState() {


        String account = App.getSharedPreferences().getString("account", "-1");
        String password = App.getSharedPreferences().getString("password", "-1");
        if ("-1".equals(account) || "-1".equals(password)) {
            handler.sendEmptyMessage(LOGIN_FILED);
            return;
        }
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
                        handler.sendEmptyMessageDelayed(LOGIN_FILED,2000);


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
                                handler.sendEmptyMessageDelayed(LOGIN_FILED,2000);

                            }
                        } else {
                            LogUtil.i(info.error_description);
                            handler.sendEmptyMessageDelayed(LOGIN_FILED,2000);

                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        handler2.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    public UserInfos userInfo;

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
                        Intent intent = new Intent();
                        intent.setClass(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onNext(UserInfos info) {

                        if (info == null) {
                            handler.sendEmptyMessageDelayed(LOGIN_FILED,2000);
                            return;
                        }
                        if (info.getMessage() != null) {
                            handler.sendEmptyMessageDelayed(LOGIN_FILED,2000);

                            return;
                        }
                        userInfo = info;
                        byte[] bytes = ParcelableUtil.marshall(info);
                        String s = Base64.encodeToString(bytes, Base64.DEFAULT);
                        App.getSharedPreferences().edit().putString("userinfo", s)
                                .putBoolean("isAdmin", info.getRusergid().equals("1") ? true : false)
                                .putString("rusergid", info.getRusergid())
                                .putString("nickname", info.getNickname())
                                .putString("realname",info.getRusername())

                                .commit();
                        Message obtain = Message.obtain(handler, LOGIN_SUCCESS);
                        obtain.obj = info.getRusergid();
                        handler.sendMessageDelayed(obtain,1800);
                    }
                });
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
                    Intent intent2 = new Intent();
                    intent2.setClass(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent2);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                    break;
                case LOGIN_SUCCESS:
                    Intent intent = new Intent();
                    intent.putExtra("rusergid", (String) msg.obj);
                    intent.setClass(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    break;
                case GETINFO:
                    getUserInfo();
                    break;
            }
        }
    };

}
