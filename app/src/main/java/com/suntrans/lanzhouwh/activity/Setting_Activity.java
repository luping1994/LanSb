package com.suntrans.lanzhouwh.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;
import com.suntrans.lanzhouwh.bean.CmdMsg;
import com.suntrans.lanzhouwh.bean.SensusSettingResult;
import com.suntrans.lanzhouwh.databinding.ActivitySensussettingBinding;
import com.suntrans.lanzhouwh.service.WebScketService;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.RxBus;
import com.suntrans.lanzhouwh.utils.UiUtils;
import com.suntrans.lanzhouwh.views.LoadingDialog;

import org.json.JSONObject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by pc on 2016/8/20.
 */
public class Setting_Activity extends BasedActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private LoadingDialog dialog;
    private WebScketService.ibinder binder;
    private ActivitySensussettingBinding binding;
    private SensusSettingResult.Commmand commmand;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (WebScketService.ibinder) service;
            LogUtil.i("服务连接成功");
            getData(sensus_id);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.i("绑定失败");
        }
    };
    private int sensus_id;
    private int userid;
    private String order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sensussetting);

        binding.toolbar.setTitle("");
        setSupportActionBar(binding.toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar!=null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        init();

        sensus_id = Integer.valueOf(getIntent().getStringExtra("id"));
        String id = App.getSharedPreferences().getString("id", "1");
        userid = Integer.valueOf(id);
        dynamicAddSkinEnableView(binding.toolbar,"background",R.color.colorPrimary);
    }

    private void init() {

        Intent intent = new Intent(this, WebScketService.class);
        this.bindService(intent, connection, Context.BIND_AUTO_CREATE);

        dialog = new LoadingDialog(this, R.style.loading_dialog);
        dialog.setWaitText("请稍后");
        dialog.setCancelable(false);

        binding.switchButtonWendu.setOnCheckedChangeListener(this);
        binding.switchButtonJiaquan.setOnCheckedChangeListener(this);
        binding.switchButtonPm25.setOnCheckedChangeListener(this);
        binding.switchButtonYanwu.setOnCheckedChangeListener(this);
        binding.switchButtonZhengdong.setOnCheckedChangeListener(this);
        binding.switchButtonRenyuan.setOnCheckedChangeListener(this);

        binding.yuzhiWendu.setOnClickListener(this);
        binding.yuzhiPm25.setOnClickListener(this);
        binding.yuzhiJiaquan.setOnClickListener(this);
        binding.yuzhiYanwu.setOnClickListener(this);
        binding.yuzhiZhengdong.setOnClickListener(this);


        RxBus.getInstance().toObserverable(CmdMsg.class)
                .compose(this.<CmdMsg>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CmdMsg>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        UiUtils.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(CmdMsg msg) {
                        parse(msg);
                    }
                });
        binding.refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(sensus_id);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (binding.refreshlayout != null)
                            binding.refreshlayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
//        binding.setCommand(new SensusSettingResult.Commmand("111111"));
//        binding.setParam(new SensusSettingResult.Param());

        binding.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(id);
                id--;
            }
        });
    }

    int id = 373;

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void parse(CmdMsg msg) {

        if (binding.refreshlayout != null)
            binding.refreshlayout.setRefreshing(false);
        if (msg.getStatus() == CmdMsg.TIPS) {
            UiUtils.showToast(msg.getMsg());
        } else {
            try {
//                JSONObject jsonObject = new JSONObject(msg.getMsg());
                SensusSettingResult result = JSON.parseObject(msg.getMsg(), SensusSettingResult.class);

//                String device = jsonObject.getString("device");
//                if (!device.equals("sensus"))
//                    return;
                String code = result.getCode();
                if (code.equals("404")) {
                    String mess = result.getMsg();
                    UiUtils.showToast("设备不在线");
                } else if (code.equals("200")) {
                    binding.setParam(result.getParam());
                    commmand = binding.getCommand();
                    if (commmand == null) {
                        commmand = new SensusSettingResult.Commmand(result.getCommand());
                        binding.setCommand(commmand);
                    } else {
                        commmand.setCommmand(result.getCommand());
                        binding.executePendingBindings();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void getData(int id) {
        JSONObject order = new JSONObject();
        try {
            order.put("device", "sensus");
            order.put("action", "rconfig");
            order.put("devID", id);
            binder.sendOrder(order.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        if (connection != null) {
            unbindService(connection);
        }
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        showAlertDialog(v.getId());
    }

    Handler handler = new Handler();

    public void showAlertDialog(final int id) {
        if (binding.getParam() == null) {
            UiUtils.showToast("当前设备不在线");
            return;
        }

//        editText.setText(string);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.item_dialog, null, false);
        final EditText ed = (EditText) view.findViewById(R.id.value);
        builder.setView(view);
        builder.setTitle("请输入阈值");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ed.getText().toString() == null) {
                    UiUtils.showToast("不能为空");
                    return;
                }
                if (ed.getText().toString().equals("")) {
                    UiUtils.showToast("不能为空");
                    return;
                }
                String value = ed.getText().toString();
                switch (id) {
                    case R.id.yuzhi_wendu:
                        binding.getParam().setV1(value);
                        break;
                    case R.id.yuzhi_yanwu:
                        binding.getParam().setV2(value);
                        break;
                    case R.id.yuzhi_jiaquan:
                        binding.getParam().setV3(value);
                        break;
                    case R.id.yuzhi_pm25:
                        binding.getParam().setV4(value);
                        break;
                    case R.id.yuzhi_zhengdong:
                        binding.getParam().setV5(value);
                        break;
                }
                binding.executePendingBindings();
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
        SensusSettingResult.Commmand commmand = binding.getCommand();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device", "sensus");
            jsonObject.put("action", "wconfig");
            jsonObject.put("devID", sensus_id);
            if (commmand != null)
                jsonObject.put("command", commmand.toString());
            jsonObject.put("param", binding.getParam().toJson());
            binder.sendOrder(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (binding.getCommand() == null) {
            UiUtils.showToast("当前设备不在线");
        }
        SensusSettingResult.Commmand commmand = binding.getCommand();
        if (commmand == null) {
            UiUtils.showToast("无法获取当前报警开关状态");
            return;
        }
        switch (buttonView.getId()) {
            case R.id.switchButton_jiaquan:
                commmand.setJiaquan(isChecked);
                break;
            case R.id.switchButton_pm25:
                commmand.setPm25(isChecked);
                break;
            case R.id.switchButton_renyuan:
                commmand.setRenyuan(isChecked);
                break;
            case R.id.switchButton_wendu:
                commmand.setWendu(isChecked);
                break;
            case R.id.switchButton_yanwu:
                commmand.setYanwu(isChecked);
                break;
            case R.id.switchButton_zhengdong:
                commmand.setZhengdong(isChecked);
                break;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device", "sensus");
            jsonObject.put("action", "wconfig");
            jsonObject.put("devID", sensus_id);
            jsonObject.put("command", commmand.toString());
            jsonObject.put("param", binding.getParam().toJson());
            binder.sendOrder(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
