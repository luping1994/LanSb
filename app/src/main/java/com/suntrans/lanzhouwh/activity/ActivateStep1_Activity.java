package com.suntrans.lanzhouwh.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BaseActivity;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.activate.ActiveResult;
import com.suntrans.lanzhouwh.utils.UiUtils;
import com.suntrans.lanzhouwh.views.EditView;

import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/8.
 */

public class ActivateStep1_Activity extends BaseActivity {
    ImageView leftIcon;
    TextView titleName;

    @BindView(R.id.account)
    EditView account;

    @BindView(R.id.phone)
    EditView phone;
    @BindView(R.id.yanzhengma)
    EditView yanzhengma;
    @BindView(R.id.button)
    Button button;
    private String phone1 = "";
    private String account1;
    private ProgressDialog dialog;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                if (msg.what==0){
                    Intent intent = new Intent();
                    intent.putExtra("account", account1);
                    intent.putExtra("phone", phone1);
                    intent.setClass(ActivateStep1_Activity.this, ActivateStep2_Activity.class);
                    startActivityForResult(intent,300);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
        }
    };
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    private void initView() {
        leftIcon = (ImageView) findViewById(R.id.left_image);
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("激活账号(1/2)");
        leftIcon.setImageResource(android.support.design.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SMSSDK.registerEventHandler(eh); //注册短信回调

        timer = new Timer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
        SMSSDK.unregisterEventHandler(eh);
    }


    //获取验证码
    public void getCheckCode(View v) {

        phone1 = phone.getText().toString();
        if (phone1.equals("") || phone1 == null) {
            UiUtils.showToast("手机号码为空!");
            return;
        }
        if (!phone1.matches("^(((13[0-9]{1})|(15[0-35-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\\d{8})$")) {
            UiUtils.showToast("手机号码格式不正确");
            return;
        }
        SMSSDK.getVerificationCode("86", phone1);
    }

    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                    LogUtil.i("提交验证码成功");
                    //提交验证码成功
                    System.out.println("验证结果为:" + data);

                    String s = data.toString();
                    //{phone=15899694150, country=86}
                    if (s.substring(7, 18).equals(phone1)) {
                        System.out.println("验证成功");
                        handler.sendEmptyMessage(0);
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                UiUtils.showToast("验证码错误");
                            }
                        });
                    }

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            UiUtils.showToast("验证码已发送,请查收");
                        }
                    });
                    new CountDownTimer(65000, 1000) {
                        @Override
                        public void onTick(final long millisUntilFinished) {
                            UiUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    button.setClickable(false); //设置不可点击
                                    button.setText(millisUntilFinished / 1000 + "S");  //设置倒计时时间
                                }
                            });
                        }

                        @Override
                        public void onFinish() {
                            UiUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    button.setClickable(true); //设置不可点击
                                    button.setText("重新发送验证码");  //设置倒计时时间
                                }
                            });

                        }
                    }.start();
                    System.out.println("获取验证码成功==>" + data.toString());

                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                    System.out.println(data.toString());
                }
            } else {
                ((Throwable) data).printStackTrace();
            }
        }
    };

    //下一步
    public void nextStep(View view) {
        phone1 = phone.getText().toString();
        account1 = account.getText().toString();
        if (account1.equals("") || account1 == null) {
            UiUtils.showToast("账号为空!");
            return;
        }
        if (phone1.equals("") || phone1 == null) {
            UiUtils.showToast("手机号码为空");
            return;
        }

//        dialog = new ProgressDialog(this);
//        dialog.setMessage("请稍后");
//        dialog.show();
        checkAccount(account1);
    }

    //检测账号是否存在
    private void checkAccount(String s) {
        RetrofitHelper.getApi().checkAccountExist(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ActiveResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ActiveResult activeResult) {
                        if (activeResult == null) {
                            return;
                        }
                        if (activeResult.result.equals("false")) {
//                            dialog.dismiss();
                            UiUtils.showToast(activeResult.reason);
                            return;
                        }
                        if (activeResult.result.equals("false")) {
//                            dialog.dismiss();
                            String verificationCode = yanzhengma.getText().toString();
                            SMSSDK.submitVerificationCode("86", phone1, verificationCode);

//                            Intent intent = new Intent();
//                            intent.putExtra("account", account1);
//                            intent.putExtra("phone", phone1);
//                            intent.setClass(ActivateStep1_Activity.this, ActivateStep2_Activity.class);
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            return;
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==200){
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
