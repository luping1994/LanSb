package com.suntrans.lanzhouwh.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.sixsensor.ConfigInfo;
import com.suntrans.lanzhouwh.bean.sixsensor.SixSensorconfigResult;
import com.suntrans.lanzhouwh.service.Service;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.UiUtils;
import com.suntrans.lanzhouwh.views.Switch;
import com.suntrans.lanzhouwh.views.WaitDialog;
import com.trello.rxlifecycle.android.ActivityEvent;


import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by pc on 2016/8/20.
 */
public class Setting_Activity extends BasedActivity implements Switch.OnSwitchChangedListener, View.OnClickListener {


    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tem_value)
    TextView temValue;
    @BindView(R.id.tem_switch)
    Switch temSwitch;
    @BindView(R.id.smoke_value)
    TextView smokeValue;
    @BindView(R.id.smoke_switch)
    Switch smokeSwitch;
    @BindView(R.id.pm_value)
    TextView pmValue;
    @BindView(R.id.pm_switch)
    Switch pmSwitch;
    @BindView(R.id.jiaquan_value)
    TextView jiaquanValue;
    @BindView(R.id.jiaquan_switch)
    Switch jiaquanSwitch;
    @BindView(R.id.zhendong_value)
    TextView zhendongValue;
    @BindView(R.id.zhengdong_switch)
    Switch zhengdongSwitch;
    @BindView(R.id.yuyin_switch)
    Switch yuyinSwitch;
    @BindView(R.id.ren_switch)
    Switch renSwitch;
    String commaand;
    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.ll_huifu)
    LinearLayout llHuifu;


    private WaitDialog dialog;
    private Service.ibinder binder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (Service.ibinder) service;
            LogUtil.i("服务连接成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("绑定失败");
        }
    };
    private int sensus_id;
    private int userid;
    private String order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        ButterKnife.bind(this);
        setListener();
        dialog = new WaitDialog(this, android.support.design.R.style.Base_Theme_AppCompat_Dialog);
        dialog.setCancelable(false);
        sensus_id = Integer.valueOf(getIntent().getStringExtra("id"));
        String id = App.getSharedPreferences().getString("id", "-1");
        userid = Integer.valueOf(id);
        getData(sensus_id);
    }

    private void setListener() {

        Intent intent = new Intent(this, Service.class);
        this.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.suntrans.bejing.RECEIVE1");
        this.registerReceiver(broadcastReceiver, filter);


        yuyinSwitch.setOnChangeListener(this);
        renSwitch.setOnChangeListener(this);
        temSwitch.setOnChangeListener(this);
        smokeSwitch.setOnChangeListener(this);
        jiaquanSwitch.setOnChangeListener(this);
        pmSwitch.setOnChangeListener(this);
        zhengdongSwitch.setOnChangeListener(this);

        temValue.setOnClickListener(this);
        smokeValue.setOnClickListener(this);
        pmValue.setOnClickListener(this);
        jiaquanValue.setOnClickListener(this);
        zhendongValue.setOnClickListener(this);


        llHuifu.setOnClickListener(this);
        layoutBack.setOnClickListener(this);


    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //aa67eb00
        }
    };

    public void getData(final int id) {
        RetrofitHelper.getApi().getSixSensorConfig(String.valueOf(id))
                .compose(this.<SixSensorconfigResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SixSensorconfigResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (dialog.isShowing())
                            dialog.dismiss();
                    }

                    @Override
                    public void onNext(SixSensorconfigResult result) {
                        if (result != null) {
                            ConfigInfo info = result.info;
                            if (info != null) {
                                refreshUI(info);
                            }
                        }
                    }
                });
    }

    private void refreshUI(ConfigInfo info) {
        if (dialog.isShowing())
            dialog.dismiss();
        temValue.setText(info.temperature);
        smokeValue.setText(info.smog);
        pmValue.setText(info.pm25);
        jiaquanValue.setText(info.methanal);
        zhendongValue.setText(info.vibrancy);

        System.out.println(info.toString());
        commaand = info.command;

        yuyinSwitch.setState(info.command.charAt(0) == '1' ? true : false);
        renSwitch.setState(info.command.charAt(1) == '1' ? true : false);
        temSwitch.setState(info.command.charAt(2) == '1' ? true : false);
        smokeSwitch.setState(info.command.charAt(3) == '1' ? true : false);
        jiaquanSwitch.setState(info.command.charAt(4) == '1' ? true : false);
        pmSwitch.setState(info.command.charAt(5) == '1' ? true : false);
        zhengdongSwitch.setState(info.command.charAt(6) == '1' ? true : false);
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onSwitchChange(Switch switchView, boolean isChecked) {
        dialog.show();
        saveComm();
        if (commaand == null)
            return;
        switch (switchView.getId()) {
            case R.id.yuyin_switch:
                break;
            case R.id.ren_switch:
                break;
            case R.id.tem_switch:
                break;
            case R.id.smoke_switch:
                break;
            case R.id.jiaquan_switch:
                break;
            case R.id.pm_switch:
                break;
            case R.id.zhengdong_switch:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (connection != null) {
            unbindService(connection);
        }
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
//        dialog.show();


        switch (v.getId()) {
            case R.id.tem_value:
                showAlertDialog(temValue, temValue.getText().toString());
                break;
            case R.id.smoke_value:
                showAlertDialog(smokeValue, smokeValue.getText().toString());
                break;
            case R.id.pm_value:
                showAlertDialog(pmValue, pmValue.getText().toString());

                break;
            case R.id.jiaquan_value:
                showAlertDialog(jiaquanValue, jiaquanValue.getText().toString());

                break;
            case R.id.zhendong_value:
                showAlertDialog(zhendongValue, zhendongValue.getText().toString());
                break;
            case R.id.layout_back:
                finish();
                break;
            case R.id.ll_huifu:
                new AlertDialog.Builder(this).setMessage("是否恢复默认设置?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogs, int which) {
                                dialog.show();
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.addProperty("op", "saveParm");
                                jsonObject.addProperty("sensus_id", sensus_id);
                                jsonObject.addProperty("user_id", userid);
                                jsonObject.addProperty("temperature", 45);
                                jsonObject.addProperty("smog", 534);
                                jsonObject.addProperty("methanal", 0.2);
                                jsonObject.addProperty("pm25", 100);
                                jsonObject.addProperty("vibrancy", 0.3);
                                order = jsonObject.toString() + "\n";

                                JsonObject jsonObject2 = new JsonObject();
                                jsonObject2.addProperty("op", "saveComm");
                                jsonObject2.addProperty("sensus_id", sensus_id);
                                jsonObject2.addProperty("user_id", userid);
                                jsonObject2.addProperty("bin", "1110000");

                              final String  order2 = jsonObject2.toString() + "\n";
                                new Thread(){
                                    @Override
                                    public void run() {
                                        super.run();
                                        binder.sendOrder(order);
                                        try {
                                            Thread.sleep(300);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        binder.sendOrder(order2);
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                getData(sensus_id);
                                            }
                                        }, 300);
                                    }
                                }.start();

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();

                break;
        }


    }

    Handler handler = new Handler();

    public void showAlertDialog(final TextView editText, final String string) {
        editText.setText(string);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.item_dialog, null, false);
        final EditText ed = (EditText) view.findViewById(R.id.value);
        builder.setView(view);
        builder.setTitle("请输入阈值");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ed.getText().toString()==null){
                    UiUtils.showToast("不能为空");
                    return;
                }
                if (ed.getText().toString().equals("")){
                    UiUtils.showToast("不能为空");
                    return;
                }
                editText.setText(ed.getText().toString());
                sendParm();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void sendParm() {
        dialog.show();
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("op", "saveParm");
            jsonObject.addProperty("sensus_id", sensus_id);
            jsonObject.addProperty("user_id", userid);
            jsonObject.addProperty("temperature", Double.valueOf(temValue.getText().toString()));
            jsonObject.addProperty("smog", Double.valueOf(smokeValue.getText().toString()));
            jsonObject.addProperty("methanal", Double.valueOf(jiaquanValue.getText().toString()));
            jsonObject.addProperty("pm25", Double.valueOf(pmValue.getText().toString()));
            jsonObject.addProperty("vibrancy", Double.valueOf(zhendongValue.getText().toString()));
            order = jsonObject.toString() + "\n";
            binder.sendOrder(order);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getData(sensus_id);
                }
            }, 300);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void saveComm() {
        String a;
        String b;
        String c;
        String d;
        String e;
        String f;
        String g;
        if (yuyinSwitch.getState()) {
            a = "1";
        } else {
            a = "0";
        }

        if (renSwitch.getState()) {
            b = "1";
        } else {
            b = "0";
        }
        if (temSwitch.getState()) {
            c = "1";
        } else {
            c = "0";
        }
        if (smokeSwitch.getState()) {
            d = "1";
        } else {
            d = "0";
        }
        if (jiaquanSwitch.getState()) {
            e = "1";
        } else {
            e = "0";
        }
        if (pmSwitch.getState()) {
            f = "1";
        } else {
            f = "0";
        }
        if (zhengdongSwitch.getState()) {
            g = "1";
        } else {
            g = "0";
        }

        String s = a+b+c+d+e+f+g;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("op", "saveComm");
        jsonObject.addProperty("sensus_id", sensus_id);
        jsonObject.addProperty("user_id", userid);
        jsonObject.addProperty("bin", s);

        order = jsonObject.toString() + "\n";
        binder.sendOrder(order);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData(sensus_id);
            }
        }, 300);
    }
}
