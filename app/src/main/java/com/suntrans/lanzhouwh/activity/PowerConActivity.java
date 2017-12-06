package com.suntrans.lanzhouwh.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.widget.ProgressBar;

import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.LoginResult.LoginResult;
import com.suntrans.lanzhouwh.bean.userinfo.UserInfos;
import com.suntrans.lanzhouwh.fragment.HomePage.MainFragment_pre_remote;
import com.suntrans.lanzhouwh.fragment.HomePage.MainFragment_rent_remote;
import com.suntrans.lanzhouwh.utils.Encryp;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.ParcelableUtil;
import com.trello.rxlifecycle.android.ActivityEvent;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/7/19.
 */

public class PowerConActivity extends BasedActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.power_con);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        checkRemoteAccount(getIntent().getStringExtra("username"));
    }

    private static final String key = "QxrLiqUU1tB5YIMKioiNT0az6f7xl1hK";
    private void checkRemoteAccount(String account) {
        if (account == null) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("账号为空")
                    .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create().show();
            return;
        }
        String time = getIntent().getStringExtra("time");
        String sig = getIntent().getStringExtra("sig");
        String siged = Encryp.md5(time + key);
        if (!sig.equals(siged)) {
            new AlertDialog.Builder(PowerConActivity.this)
                    .setMessage("无效的用户名")
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
        LogUtil.i("account=" + account);
        LogUtil.i("time=" + time);
        LogUtil.i("sig=" + sig);
        LogUtil.i("siged=" + siged);
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
                        showAlertDialog("无法连接到服务器");

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
                                    progressBar.setVisibility(View.GONE);
                                    getUserInfo();
                                }else {
                                    showAlertDialog("错误");

                                }
                            } else {
                            }
                        } else {
                            showAlertDialog("错误");
                            LogUtil.i(info.error_description);
                        }
                    }
                });
    }

    private void showAlertDialog(String msg) {
        new AlertDialog.Builder(PowerConActivity.this)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create().show();
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
                    }

                    @Override
                    public void onNext(UserInfos info) {

                        if (info == null) {
                            return;
                        }
                        if (info.getMessage() != null) {
                            return;
                        }
                        userInfo = info;
                        byte[] bytes = ParcelableUtil.marshall(info);
                        String s = Base64.encodeToString(bytes, Base64.DEFAULT);
                        App.getSharedPreferences().edit().putString("userinfo", s)
                                .putBoolean("isAdmin", info.getRusergid().equals("1") ? true : false)
                                .commit();
                        checkShenfen(userInfo);
                    }
                });
    }

    private void checkShenfen(UserInfos userInfo) {
        if (userInfo.getRusergid().equals("1")){
            MainFragment_pre_remote main_fragment = MainFragment_pre_remote.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.content,main_fragment).commit();
        }else if (userInfo.getRusergid().equals("2")){
            MainFragment_rent_remote main_fragment = MainFragment_rent_remote.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.content,main_fragment).commit();

        }
    }
}
