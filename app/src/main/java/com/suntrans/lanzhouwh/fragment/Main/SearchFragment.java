package com.suntrans.lanzhouwh.fragment.Main;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
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
import com.suntrans.lanzhouwh.adapter.SearchFragment_adapter;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.CmdMsg;
import com.suntrans.lanzhouwh.bean.Search;
import com.suntrans.lanzhouwh.bean.genitem.FloorDetailItem;
import com.suntrans.lanzhouwh.service.WebScketService;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.RxBus;
import com.suntrans.lanzhouwh.utils.UiUtils;
import com.suntrans.lanzhouwh.views.LoadingDialog;
import com.suntrans.lanzhouwh.views.LoadingPage;
import com.suntrans.lanzhouwh.views.SwitchButton;
import com.suntrans.lanzhouwh.websocket.SenderWebSocket;
import com.trello.rxlifecycle.android.FragmentEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.security.Key;
import java.util.concurrent.CopyOnWriteArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.suntrans.lanzhouwh.R.id.msg;

/**
 * Created by Looney on 2016/11/26.
 * des:硬件管理fragment
 */
public class SearchFragment extends BasedFragment {
    final String TAG = "HardwareFragment";

    private CopyOnWriteArrayList<FloorDetailItem.Channel> datas = new CopyOnWriteArrayList<>();//显示的通道
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private SearchFragment_adapter adapter;
    private LoadingDialog dialog;
    private boolean isAdmin;

//    private SenderWebSocket socket;

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
    private Subscription subscribe1;
    private Subscription subscribe;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData();
        return createSuccessView();
    }

    private View createSuccessView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search, null, false);
        initView(view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Intent intent = new Intent(getActivity(), WebScketService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        subscribe = RxBus.getInstance().toObserverable(Search.class)
                .compose(this.<Search>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Search>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Search s) {
                        search(s.key, s.did);
                    }
                });
        subscribe1 = RxBus.getInstance().toObserverable(CmdMsg.class)
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
                        onMessage(msg);
                    }
                });
    }


    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(getActivity());
        adapter = new SearchFragment_adapter(getActivity(), datas);

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
        adapter.setOnItemClickListener(new SearchFragment_adapter.onItemClickListener() {
            @Override
            public void onShutdownClick() {
                new AlertDialog.Builder(getActivity())
                        .setMessage("是否关闭所有设备?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
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
            public void onSwitchClick(final SwitchButton switchView, int position, final boolean state) {
                if (position == -3) {
                    UiUtils.showToast("请不要频繁操作!");
                    return;
                }
                LogUtil.i(TAG, datas.get(position).ssid);

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
                    LogUtil.i(jsonObject.toString());
                    if (!binder.sendOrder(jsonObject.toString())) {
//                        if (!cmd.contains(jsonObject.toString())){
//                            cmd.add(jsonObject.toString());
//                        }
//                        socket.connect();
                        handler2.postDelayed(new Runnable() {
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
                                dialog.setWaitText(s);
                                dialog.show();
//                                handler2.sendMessage(Message.obtain(handler2, MSG_START, s));
                                handler2.postDelayed(runnable, 3000);
                            }
                        }
                    }
                }


            }

            @Override
            public void onContentClick(int position) {
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
        dialog.setCancelable(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.i(TAG, "onAttach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler2.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        if (connection != null) {
            getActivity().unbindService(connection);
        }

        if (!subscribe.isUnsubscribed()){
            subscribe.unsubscribe();
        }
        if (!subscribe1.isUnsubscribed()){
            subscribe1.unsubscribe();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    public void onMessage(CmdMsg result) {
        if (result.getStatus() == 0) {
            UiUtils.showToast(result.getMsg());
            return;
        }
//        LogUtil.i(TAG,"收到:"+msg1);
        String msg1 = result.getMsg();
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


    private void search(String key, String did) {
        Observable<FloorDetailItem> observable = null;
        if (isAdmin) {
            observable = RetrofitHelper.getApi2().searchDeviceAdmin(did, key);
        } else {
            observable = RetrofitHelper.getApi2().searchDeviceRent(key);
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
                        UiUtils.showToast("暂无搜索结果");
                    }

                    @Override
                    public void onNext(FloorDetailItem item) {
                        if (item != null) {
                            if (item.devlist != null) {
                                datas.clear();
                                datas.addAll(item.devlist);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            UiUtils.showToast("暂无搜索结果");
                        }
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler2.removeCallbacksAndMessages(null);
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

    private final int GETINFO_SUCCESS = 0;
    private final int GETINFO_FILED = 1;
    private final int SHOWING_DIALOG = 3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETINFO_SUCCESS:

                    adapter.notifyDataSetChanged();
                    break;
                case GETINFO_FILED:
                    break;
                case SHOWING_DIALOG:
                    dialog.show();
                    break;
            }
        }
    };
}
