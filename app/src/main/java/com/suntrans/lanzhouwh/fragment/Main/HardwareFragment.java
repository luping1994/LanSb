package com.suntrans.lanzhouwh.fragment.Main;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedFragment;
import com.suntrans.lanzhouwh.adapter.DividerGridItemDecoration;
import com.suntrans.lanzhouwh.adapter.HardwareFragment_adapter;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.CmdMsg;
import com.suntrans.lanzhouwh.bean.genitem.FloorDetailItem;
import com.suntrans.lanzhouwh.service.WebScketService;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.RxBus;
import com.suntrans.lanzhouwh.utils.UiUtils;
import com.suntrans.lanzhouwh.utils.ViewUtils;
import com.suntrans.lanzhouwh.views.LoadingDialog;
import com.suntrans.lanzhouwh.views.LoadingPage;
import com.suntrans.lanzhouwh.views.SwitchButton;
import com.trello.rxlifecycle.android.FragmentEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CopyOnWriteArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/11/26.
 * des:硬件管理fragment
 */
public class HardwareFragment extends BasedFragment  {
    final String TAG = "HardwareFragment";

    private CopyOnWriteArrayList<FloorDetailItem.Channel> datas = new CopyOnWriteArrayList<>();//显示的通道
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private HardwareFragment_adapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private LoadingDialog shutdown_dialog;
    private LoadingDialog dialog;
    private boolean isAdmin;

//    private SenderWebSocket socket;

    private boolean flag = true;

    private String did;
    private Subscription subscribe;
    private String type="";

    public static HardwareFragment newInstance(String type) {
        HardwareFragment fragment = new HardwareFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }
    private LoadingPage loadingPage;

    private WebScketService.ibinder binder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (WebScketService.ibinder) service;
            LogUtil.i("服务连接成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.i("绑定失败");
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData();

        if (loadingPage == null) {  // 之前的frameLayout 已经记录了一个爹了  爹是之前的
            loadingPage = new LoadingPage(getActivity()) {
                @Override
                public View createSuccessView() {
                    return HardwareFragment.this.createSuccessView();
                }

                @Override
                protected void load() {
                    HardwareFragment.this.load();
                }
            };
        } else {
            ViewUtils.removeParent(loadingPage);// 移除frameLayout之前的爹
        }

        return loadingPage;  //  拿到当前viewPager 添加这个framelayout
    }

    private View createSuccessView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_hardware, null, false);
        initView(view);
        return view;
    }

    private void load() {
        getDeviceList();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loadingPage.showPage();
        load();
        dialog = new LoadingDialog(getActivity(), R.style.loading_dialog);
        dialog.setCancelable(false);
        Intent intent = new Intent(getActivity(), WebScketService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);

        subscribe = RxBus.getInstance().toObserverable(CmdMsg.class)
//                .compose(this.<CmdMsg>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CmdMsg>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i("onError");

                        UiUtils.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(CmdMsg msg) {
                        onMessage(msg);
                    }
                });
    }

//    boolean isGetData = false;

    private void initView(View view) {
        did = getActivity().getIntent().getStringExtra("did");
        if (did != null)
            LogUtil.i(did);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(getActivity());
        adapter = new HardwareFragment_adapter(getActivity(), datas);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDeviceList();
            }
        });
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                recyclerviewState = newState;
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        adapter.setOnItemClickListener(new HardwareFragment_adapter.onItemClickListener() {
            @Override
            public void onShutdownClick() {
                new AlertDialog.Builder(getActivity())
                        .setMessage("是否关闭所有设备?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new ShutDownTask().execute();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }

            @Override
            public void onSwitchClick(final SwitchButton switchView, int position, final boolean state) {
                if (position == -3) {
                    UiUtils.showToast("请不要频繁操作!");
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("device", "slc");
                    jsonObject.put("action", "switch");
                    jsonObject.put("devID", Integer.valueOf(datas.get(position).ssid));
                    jsonObject.put("command", state ? "1" : "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonObject.toString() != null) {
//                    LogUtil.i(TAG,"发送："+jsonObject.toString());
                    if (!binder.sendOrder(jsonObject.toString())) {
//                        socket.connect();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                switchView.setChecked(!state);
                            }
                        }, 500);
                    } else {
                        if (dialog != null) {
                            if (!dialog.isShowing()) {
                                String s = "";
                                if (state)
                                    s = "开启设备中,请稍后...";
                                else
                                    s = "关闭设备中,请稍后...";
                                handler2.sendMessage(Message.obtain(handler2, MSG_START, s));
                                handler2.postDelayed(runnable, 3000);
                            }
                        }
                    }
                }


            }

            @Override
            public void onSwitchRootClick(int position) {
                if (position == -3) {
                    UiUtils.showToast("请不要频繁操作!");
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("device", "slc");
                    jsonObject.put("action", "switch");
                    jsonObject.put("devID", Integer.valueOf(datas.get(position).ssid));
                    jsonObject.put("command", datas.get(position).state.equals("0") ? "1" : "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonObject.toString() != null) {
                    binder.sendOrder(jsonObject.toString());
                    if (dialog != null) {
                        if (!dialog.isShowing()) {
                            String s = "";
                            if (datas.get(position).state.equals("0"))
                                s = "开启设备中,请稍后...";
                            else
                                s = "关闭设备中,请稍后...";
                            handler2.sendMessage(Message.obtain(handler2, MSG_START, s));
                            handler2.postDelayed(runnable, 3000);
                        }
                    }
                }
            }

            @Override
            public void onContentClick(int position) {
//                changedChannelName(position);
            }
        });
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                isUpdateUi = true;
            }
        });

    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isUpdateUi) {
                adapter.notifyDataSetChanged();
            }
            if (dialog != null)
                if (dialog.isShowing())
                    dialog.dismiss();
        }
    };
    boolean isUpdateUi = true;
    public int recyclerviewState = 0;

    private void initData() {
        isAdmin = App.getSharedPreferences().getBoolean("isAdmin", true);
        dialog = new LoadingDialog(getActivity(), R.style.loading_dialog);
        shutdown_dialog = new LoadingDialog(getActivity(), R.style.loading_dialog);
        dialog.setCancelable(false);
        shutdown_dialog.setCancelable(false);

        type = getArguments().getString("type");
//        socket = new SenderWebSocket();
//        socket.setOnReceiveListener(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.i(TAG, "onAttach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("onDestroy");

//        socket.closeWebSocket();
        if (connection!=null){
            getActivity().unbindService(connection);
        }
        if (!subscribe.isUnsubscribed()){
            subscribe.unsubscribe();
            LogUtil.i("没有解除订阅,正在解除订阅");
        }
//        getActivity().unregisterReceiver(myNetReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.i(TAG, "onPause");
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    public void onMessage(CmdMsg result) {
        if (result.getStatus()==0){
            UiUtils.showToast(result.getMsg());
            return;
        }
//        LogUtil.i(TAG,"收到:"+msg1);
        String msg1 =result.getMsg();
        try {
            JSONObject jsonObject = new JSONObject(msg1);
//            String device = jsonObject.getString("device");
//            if (!device.equals("slc")){
//                return;
//            }
            String code = jsonObject.getString("code");
            if (code.equals("200")) {
                String devID = jsonObject.getString("devID");
                String state = jsonObject.getString("status");
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).ssid.equals(devID)) {
                        datas.get(i).state = state;
                    }
                }
//                LogUtil.e("朱亚明收到");
                handler2.removeCallbacksAndMessages(null);
                handler2.sendMessage(Message.obtain(handler, MSG_CON_SUCCESS));
            } else if (code.equals("404")) {
                String msg = jsonObject.getString("msg");
                UiUtils.showToast("设备不在线");
                handler2.removeCallbacksAndMessages(null);
                handler2.sendMessage(Message.obtain(handler, MSG_CON_FAILED, msg));
            } else if (code.equals("101")) {
                String msg = jsonObject.getString("msg");
                handler2.removeCallbacksAndMessages(null);
                handler2.sendMessage(Message.obtain(handler, MSG_CON_FAILED, msg));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public void onFailure(Throwable t) {
//        if (t instanceof ConnectException) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    UiUtils.showToast("无法连接到控制服务器");
//                }
//            });
////            UiUtils.showMessageWarning(getActivity().findViewById(android.R.id.content), "无法连接到服务器");
//        } else if (t instanceof SocketTimeoutException) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    UiUtils.showToast("连接超时...正在尝试重新连接");
//                }
//            });
////            socket.connect();
//
//        }
//        LogUtil.e("" + t.toString());
//    }
//
//    public void onOpen() {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                UiUtils.showToast("通讯成功");
//            }
//        });
//        LogUtil.i("websocket is opened");
//    }



    class ShutDownTask extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            if (flag == true) {
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shutdown_dialog.show();
                    }
                });
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).state.equals("1")) {
                        publishProgress((i + 1) + "/" + datas.size());

//                        binder.sendOrder(datas.get(i).closeCommand);
                    } else {
                        publishProgress((i + 1) + "/" + datas.size());
                    }
//                    final double progress =(double) (i+1) / (double)datas.size();
                    try {
                        Thread.sleep(500);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            String progress = values[0];
            LogUtil.i(progress);
            shutdown_dialog.setWaitText("执行中(" + progress + ")");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            shutdown_dialog.dismiss();
        }
    }

    private void getDeviceList() {
        Observable<FloorDetailItem> observable = null;
        if (isAdmin) {
            observable = RetrofitHelper.getApi2().getFloorDeviceAdmin(did);
        } else {
            observable = RetrofitHelper.getApi2().getFloorDeviceRent();
        }
        observable.compose(this.<FloorDetailItem>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FloorDetailItem>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(GETINFO_FILED);
                    }

                    @Override
                    public void onNext(FloorDetailItem item) {
                        if (item != null) {
                            if (item.devlist != null) {
                                datas.clear();
                               for (int i=0;i<item.devlist.size();i++){
                                   if (item.devlist.get(i).idusetype.equals(type))
                                       datas.add(item.devlist.get(i));
                               }
//                                datas.addAll(item.devlist);
                                LogUtil.i("通道1状态：" + item.devlist.get(0).state);
                                handler.sendEmptyMessage(GETINFO_SUCCESS);
                            }
                        } else {
                            handler.sendEmptyMessage(GETINFO_FILED);
                        }
                    }
                });
    }

    private final int GETINFO_SUCCESS = 0;
    private final int GETINFO_FILED = 1;
    private final int SHOWING_DIALOG = 3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETINFO_SUCCESS:
                    refreshLayout.setRefreshing(false);
                    loadingPage.setState(LoadingPage.STATE_SUCCESS);
                    adapter.notifyDataSetChanged();
                    break;
                case GETINFO_FILED:
                    loadingPage.setState(LoadingPage.STATE_ERROR);
                    break;
                case SHOWING_DIALOG:
                    dialog.show();
                    break;
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
        handler2.removeCallbacksAndMessages(null);
        flag = false;
        LogUtil.e("onDestroyView");
    }

    private static final int MSG_START = 7;
    private static final int MSG_CON_SUCCESS = 8;
    private static final int MSG_CON_FAILED = 9;
    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START:
                    String s = (String) msg.obj;
                    if (s != null)
                        dialog.setWaitText(s);
                    dialog.show();
                    break;
                case MSG_CON_SUCCESS:
                    dialog.dismiss();
                    adapter.notifyDataSetChanged();
                    break;
                case MSG_CON_FAILED:
                    dialog.setWaitText((String) msg.obj);
                    handler2.sendEmptyMessageDelayed(MSG_CON_SUCCESS, 500);
                    break;
            }
        }
    };


}
