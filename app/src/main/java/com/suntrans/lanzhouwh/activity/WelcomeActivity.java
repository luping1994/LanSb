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

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/1/6.
 */

public class WelcomeActivity extends BasedActivity {

    private UserInfos info;
    private boolean isRemote =false;
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
    private static final String key = "QxrLiqUU1tB5YIMKioiNT0az6f7xl1hK";
    private void checkRemoteAccount(String account) {
        String time = getIntent().getStringExtra("time");
        String sig = getIntent().getStringExtra("sig");
        String siged = Encryp.md5(time+key);
        gotoType = getIntent().getStringExtra("type");
        if (!sig.equals(siged)||gotoType==null){
            new AlertDialog.Builder(WelcomeActivity.this)
                    .setMessage("非法操作")
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create().show();
            return;
        }
        isRemote = true;
        LogUtil.i("account="+account);
        LogUtil.i("time="+time);
        LogUtil.i("sig="+sig);
        LogUtil.i("siged="+siged);
        RetrofitHelper.getLoginApi2().login2("password", "suntrans", "suntrans", account, "YvhndJ3QhWvyZfUHHDVdjeF6r3TYl9HtvfwX8ET3")
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
                                    editor.putString("expires_in1", info.expires_in);
                                    editor.putString("access_token1", info.access_token);
                                    editor.putLong("firsttime1", System.currentTimeMillis());
                                    editor.commit();
                                    handler.sendEmptyMessageDelayed(GETINFO, 300);
                                }
                            }else {
                                handler.sendEmptyMessage(LOGIN_FILED);
                            }
                        } else {
                            handler.sendEmptyMessage(LOGIN_FILED);
                            LogUtil.i(info.error_description);
                        }
                    }
                });
    }

    private void checkLocalState() {
        String access_token = App.getSharedPreferences().getString("access_token1", "-1");
        String userinfo = App.getSharedPreferences().getString("userinfo", "-1");
        info = null;
        if (!userinfo.equals("-1")) {
            byte[] s = Base64.decode(userinfo, Base64.DEFAULT);
            Parcel parcel = ParcelableUtil.unmarshall(s);
            info = UserInfos.CREATOR.createFromParcel(parcel);
        }
        if (access_token.equals("-1")) {
            handler2.sendEmptyMessageDelayed(START_LOGIN, 1800);
        } else {
            try {
                String expires_in = App.getSharedPreferences().getString("expires_in1", "-1");
                long firsttime = App.getSharedPreferences().getLong("firsttime1", -1l);
                long currenttime = System.currentTimeMillis();
                long d = (currenttime - firsttime) / 1000;
//                LogUtil.i("时间差" + d);
//                LogUtil.i("过期时间" + expires_in);
//                LogUtil.i("过期时间-6天:" + (Long.valueOf(expires_in) - 6 * 24 * 3600));
//                String a = d > (Long.valueOf(expires_in) - 6 * 24 * 3600) ? "过期了" : "没过期";
//                LogUtil.i("是否过期:" + a);
                if (d > (Long.valueOf(expires_in) - 6 * 24 * 3600)) {
                    handler2.sendEmptyMessageDelayed(START_LOGIN, 2000);
                } else {
                    if (info != null)
                        handler2.sendEmptyMessageDelayed(START_MAIN, 2000);
                    else
                        handler2.sendEmptyMessageDelayed(START_LOGIN, 2000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler2.sendEmptyMessageDelayed(START_LOGIN, 2000);

            }

        }
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
                        handler.sendEmptyMessage(LOGIN_FILED);
                    }

                    @Override
                    public void onNext(UserInfos info) {

                        if (info == null) {
                            handler.sendEmptyMessage(LOGIN_FILED);
                            return;
                        }
                        if (info.getMessage()!=null){
                            handler.sendEmptyMessage(LOGIN_FILED);
                            return;
                        }
                        userInfo=info;
                        byte[] bytes = ParcelableUtil.marshall(info);
                        String s = Base64.encodeToString(bytes,Base64.DEFAULT);
                        App.getSharedPreferences().edit().putString("userinfo", s)
                                .putBoolean("isAdmin",info.getRusergid().equals("1")?true:false)
                                .commit();
                        handler.sendMessage(Message.obtain(handler, LOGIN_SUCCESS));
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
                   new AlertDialog.Builder(WelcomeActivity.this)
                           .setMessage("登录失败")
                           .setCancelable(false)
                           .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   finish();
                               }
                           })
                           .create().show();
                    break;
                case LOGIN_SUCCESS:
                    Intent intent = new Intent();
                    if (gotoType.equals("main")){
                        intent.putExtra("info",userInfo);
                        intent.putExtra("isRemote",isRemote);
                        intent.setClass(WelcomeActivity.this, MainActivity.class);
                    }else if (gotoType.equals("env")){
                        intent.setClass(WelcomeActivity.this, Env_activity.class);
                        intent.putExtra("isRemote",isRemote);
                    }else {
                        UiUtils.showToast("非法操作");
                        break;
                    }
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
