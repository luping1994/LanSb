package com.suntrans.lanzhouwh.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.suntrans.lanzhouwh.bean.CmdMsg;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.RxBus;
import com.suntrans.lanzhouwh.websocket.WebSocketWrapper;

/**
 * Created by Looney on 2017/5/27.
 */

public class WebScketService extends Service implements WebSocketWrapper.onReceiveListener {
    private WebSocketWrapper webSocketWrapper;
    private IBinder binder;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i("service is onCreate!");
        if (webSocketWrapper == null) {
            webSocketWrapper = new WebSocketWrapper();
            webSocketWrapper.setOnReceiveListener(this);
        }
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);   //注册接收网络连接状态改变广播接收器
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.i("service is onBind!");
        binder = new ibinder() {
            @Override
            public boolean sendOrder(String order) {
                boolean isSuccess = webSocketWrapper.sendMessage(order);
                if (!isSuccess) {
                    webSocketWrapper.connect();
                }
                return isSuccess;
            }
        };
        return binder;
    }


    @Override
    public void onMessage(String s) {
//        LogUtil.i("webservice收到：" + s);

        CmdMsg msg = new CmdMsg(CmdMsg.SUCCESS, s);
        RxBus.getInstance().post(msg);
    }

    @Override
    public void onFailure(Throwable t) {
        CmdMsg msg = new CmdMsg(CmdMsg.TIPS, "连接服务器失败");
        RxBus.getInstance().post(msg);
    }


    @Override
    public void onOpen() {
        CmdMsg msg = new CmdMsg(CmdMsg.TIPS, "通讯成功");
        RxBus.getInstance().post(msg);

    }

    public class ibinder extends Binder {
        /****
         * 发送命令
         *
         * @param order 控制命令内容，从控制子地址开始，到校验码之前
         * @return 发送成功返回true，失败返回false
         */
        public boolean sendOrder(String order) {
            return true;
        }
    }

    @Override
    public void onDestroy() {
        LogUtil.i("service is onDestroy!");

        if (webSocketWrapper != null)
            webSocketWrapper.closeWebSocket();
        if (myNetReceiver != null) {
            unregisterReceiver(myNetReceiver);   //注销接收网络变化的广播通知的广播接收器
        }
        super.onDestroy();
    }

    private ConnectivityManager mConnectivityManager;
    private NetworkInfo netInfo;
    /////////////监听网络状态变化的广播接收器
    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {
                    webSocketWrapper.connect();
                    String name = netInfo.getTypeName();
                    LogUtil.i(name);
                } else {

                }
            }

        }
    };
}
