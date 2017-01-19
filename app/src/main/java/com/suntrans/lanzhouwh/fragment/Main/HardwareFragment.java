package com.suntrans.lanzhouwh.fragment.Main;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedFragment;
import com.suntrans.lanzhouwh.adapter.DividerGridItemDecoration;
import com.suntrans.lanzhouwh.adapter.HardwareFragment_adapter;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.userinfo.Channel;
import com.suntrans.lanzhouwh.bean.userinfo.ChannelGroup;
import com.suntrans.lanzhouwh.bean.userinfo.ChannelType;
import com.suntrans.lanzhouwh.bean.userinfo.UserInfo;
import com.suntrans.lanzhouwh.bean.userinfo.UserInfoResult;
import com.suntrans.lanzhouwh.service.MainService;
import com.suntrans.lanzhouwh.utils.Converts;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.UiUtils;
import com.suntrans.lanzhouwh.views.Switch;
import com.suntrans.lanzhouwh.views.WaitDialog;
import com.trello.rxlifecycle.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.R.attr.handle;
import static android.R.attr.id;
import static android.R.attr.inflatedId;
import static android.R.attr.numbersBackgroundColor;

/**
 * Created by Looney on 2016/11/26.
 * des:硬件管理fragment
 */
public class HardwareFragment extends BasedFragment {
    final String TAG = "HardwareFragment";

    private CopyOnWriteArrayList<Channel> datas = new CopyOnWriteArrayList<>();//显示的通道
    private CopyOnWriteArrayList<Channel> datasCopy = new CopyOnWriteArrayList<>();//所有通道
    private CopyOnWriteArrayList<Channel> lastData = new CopyOnWriteArrayList<>();//本次楼层的所有通道
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private HardwareFragment_adapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private MainService.ibinder binder;
    private WaitDialog shutdown_dialog;
    private WaitDialog dialog;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MainService.ibinder) service;
            LogUtil.i("service绑定成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("绑定失败");
        }
    };
    private boolean flag = true;
    private Spinner spinner1;
    private Spinner spinner2;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayAdapter<String> spinner2Adapter;
    private List<String> strings;
    private List<String> strings2;

    public static HardwareFragment newInstance() {
        return new HardwareFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hardware, null, false);
        initData();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

    }

    boolean isGetData = false;
    int spinner1SelectPosition = 0;
    int spinner2SelectPosition = 0;

    private void initView(View view) {

        spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        spinner2 = (Spinner) view.findViewById(R.id.spinner2);
        strings = new ArrayList<>();
        strings2 = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter(getActivity(), R.layout.item_spinner, R.id.tv_spinner, strings);
        spinner2Adapter = new ArrayAdapter(getActivity(), R.layout.item_spinner, R.id.tv_spinner, strings2);
        spinner1.setAdapter(spinnerAdapter);
        spinner2.setAdapter(spinner2Adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isGetData) {
                    return;
                }
                if (channel_group == null) {
                    return;
                }
                if (channel_type == null)
                    return;
                if (!dialog.isShowing())
                    dialog.show();
                if (datasCopy.size() != 0) {
                    handler.removeCallbacksAndMessages(null);
                    spinner1SelectPosition = position;
                    if (position == 0) {
                        isUpdateUi = false;
                        datas.clear();
                        lastData.clear();
                        datas.addAll(datasCopy);
                        lastData.addAll(datasCopy);
                        if (recyclerviewState != 0) {
                            forceStopRecyclerViewScroll(recyclerView);
                        }
                        isUpdateUi = true;
                        if (spinner2.getSelectedItemPosition() == 0) {
                            handler.sendEmptyMessageDelayed(DISSMISS_DIALOG, 500);
                        } else {
                            spinner2.setSelection(0);
                        }

                        return;
                    }
                    isUpdateUi = false;
                    CopyOnWriteArrayList<Channel> tem = new CopyOnWriteArrayList<Channel>();
                    for (int i = 0; i < datasCopy.size(); i++) {
                        String s = datasCopy.get(i).group_id;
                        if (s == null)
                            continue;
                        try {
                            if (s.equals(channel_group.get(position - 1).id)) {
                                tem.add(datasCopy.get(i));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    datas.clear();
                    lastData.clear();
                    datas.addAll(tem);
                    lastData.addAll(tem);
                    if (recyclerviewState != 0) {
                        forceStopRecyclerViewScroll(recyclerView);
                    }
                    isUpdateUi = true;
                    if (spinner2.getSelectedItemPosition() == 0) {
                        handler.sendEmptyMessageDelayed(DISSMISS_DIALOG, 500);
                    } else {
                        spinner2.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isGetData) {
                    return;
                }
                if (channel_type == null)
                    return;
                if (!dialog.isShowing())
                    dialog.show();
                if (datasCopy.size() != 0) {
                    handler.removeCallbacksAndMessages(null);
                    spinner2SelectPosition = position;
                    if (position == 0) {
                        isUpdateUi = false;
                        datas.clear();
                        datas.addAll(lastData);
                        if (recyclerviewState != 0) {
                            forceStopRecyclerViewScroll(recyclerView);
                        }
                        isUpdateUi = true;
                        handler.sendEmptyMessageDelayed(DISSMISS_DIALOG, 500);
                        return;
                    }
                    isUpdateUi = false;
                    CopyOnWriteArrayList<Channel> tem = new CopyOnWriteArrayList<Channel>();
                    System.out.println(lastData.size());
                    for (int i = 0; i < lastData.size(); i++) {
                        if (lastData.get(i).vtype.equals(channel_type.get(position - 1).id)) {
                            tem.add(lastData.get(i));
                        }
                    }
                    datas.clear();
                    datas.addAll(tem);
                    if (recyclerviewState != 0) {
                        forceStopRecyclerViewScroll(recyclerView);
                    }
                    isUpdateUi = true;
                    handler.sendEmptyMessageDelayed(DISSMISS_DIALOG, 500);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
            public void onSwitchClick(Switch switchView, int position, boolean state) {
                if (dialog != null) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                        handler.postDelayed(runnable, 3000);
                    }
                }

                if (position == -3) {
                    UiUtils.showToast("请不要频繁操作!");
                    return;
                }
                try {
                    if (state) {
                        binder.sendOrder(datas.get(position).openCommand);
                    } else {
                        binder.sendOrder(datas.get(position).closeCommand);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                isUpdateUi = true;
            }
        });

        getDeviceList();
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
        dialog = new WaitDialog(getActivity(), android.support.design.R.style.Base_Theme_AppCompat_Dialog);
        shutdown_dialog = new WaitDialog(getActivity(), android.support.design.R.style.Base_Theme_AppCompat_Dialog);
        dialog.setCancelable(false);
        shutdown_dialog.setCancelable(false);
        Intent intent = new Intent(getActivity(), MainService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.suntrans.bejing.RECEIVE");
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.i(TAG, "onAttach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG, "onDestroy");
        if (connection != null) {
            getActivity().unbindService(connection);
        }
        getActivity().unregisterReceiver(broadcastReceiver);
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

    class SendOrderTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

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
                    binder.sendOrder(datas.get(i).closeCommand);
//                    final double progress =(double) (i+1) / (double)datas.size();
                    publishProgress((i + 1) + "/" + datas.size());
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

    public List<ChannelGroup> channel_group;
    public List<ChannelType> channel_type;

    private void getDeviceList() {
        RetrofitHelper.getApi().getUserInfo().compose(this.<UserInfoResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UserInfoResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.sendEmptyMessageDelayed(GETINFO_FILED, 800);
                    }

                    @Override
                    public void onNext(UserInfoResult info) {
                        if (info == null) {
                            return;
                        }
                        if (info.user == null) {
                            UiUtils.showToast("请求失败!请检查你的网络");
                            return;
                        }
                        if (info.status_code == null) {
                            return;
                        }
                        if (info.status_code.equals("0")) {
                            UiUtils.showToast("请求失败!请检查你的网络");
                            return;
                        }


                        if (info.user.channel_group == null) {
                            strings.clear();
                            strings.add(info.user.dept_name);
                            strings2.clear();
                            strings2.add("全部");
                            channel_type = info.user.channel_type;
                            for (int i = 0; i < info.user.channel_type.size(); i++) {
                                strings2.add(info.user.channel_type.get(i).vtype);
                            }
                            spinnerAdapter.notifyDataSetChanged();
                            spinner2Adapter.notifyDataSetChanged();

                        } else {
                            channel_group = info.user.channel_group;
                            channel_type = info.user.channel_type;
                            strings.clear();
                            strings2.clear();
                            strings2.add("全部");
                            strings.add("全部");
                            for (int i = 0; i < info.user.channel_group.size(); i++) {
                                strings.add(info.user.channel_group.get(i).name);
                            }

                            for (int i = 0; i < info.user.channel_type.size(); i++) {
                                strings2.add(info.user.channel_type.get(i).vtype);
                            }
                            spinnerAdapter.notifyDataSetChanged();
                            spinner2Adapter.notifyDataSetChanged();
                        }
                        LogUtil.i(info.toString());
                        Message msg = new Message();
                        msg.what = GETINFO_SUCCESS;
                        msg.obj = info.user;
                        handler.sendMessage(msg);

                    }
                });
    }

    private final int GETINFO_SUCCESS = 0;
    private final int GETINFO_FILED = 1;
    private final int DISSMISS_DIALOG = 2;
    private final int SHOWING_DIALOG = 3;
    private final int DISSMISS_DIALOG_NOT_UPDATEUI = 4;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETINFO_SUCCESS:
                    UserInfo info = (UserInfo) msg.obj;
                    List<Channel> channels = info.channels;
                    datas.clear();
                    lastData.clear();
                    datasCopy.clear();
                    for (Channel channel :
                            channels) {
                        datasCopy.add(channel);
                        datas.add(channel);
                        lastData.add(channel);
                    }
                    isGetData = true;
                    if (spinner1SelectPosition == 0) {
                        spinner2.setSelection(0);
                    } else {
                        spinner1.setSelection(0);
                    }
                    if (isUpdateUi) {
                        adapter.notifyDataSetChanged();
                    }
                    refreshLayout.setRefreshing(false);
                    break;
                case GETINFO_FILED:
                    UiUtils.showToast("获取设备列表失败!请检查你的网络");
                    refreshLayout.setRefreshing(false);
                    break;
                case DISSMISS_DIALOG:
                    if (isUpdateUi)
                        adapter.notifyDataSetChanged();
                    if (dialog != null)
                        if (dialog.isShowing())
                            dialog.dismiss();
                    break;
                case SHOWING_DIALOG:
                    dialog.show();
                    break;
                case DISSMISS_DIALOG_NOT_UPDATEUI:
                    dialog.dismiss();
                    break;
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
        flag = false;

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //aa67eb00
            parses(intent);
        }
    };

    private synchronized void parses(Intent intent) {
        byte[] s1 = intent.getByteArrayExtra("Content");
        String s = Converts.Bytes2HexString(s1).toLowerCase();
        System.out.println("接收到" + s);
        if (!s.substring(0, 4).equals("aa67")) {
            System.out.println("无效的命令");
            return;
        }
        if (s.equals("aa6710000001")) {
            if (dialog.isShowing())
                dialog.dismiss();
            new AlertDialog.Builder(getActivity()).setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setMessage("当前控制的设备不在线！").create().show();
        }

        String id = s.substring(4, s.length() - 2);
        String state = s.substring(s.length() - 1, s.length());
        try {
            id = String.valueOf(Integer.parseInt(id, 16));
        } catch (Exception e) {
            e.printStackTrace();
        }

//        for (int i=0;i<lastData.size();i++){
//            if (lastData.get(i).id.equals(id)) {
//                lastData.get(i).setState(state);
//            }
//        }
        for (int i = 0; i < datasCopy.size(); i++) {
            if (datasCopy.get(i).id.equals(id)) {
                datasCopy.get(i).setState(state);
//                if (isUpdateUi)
//                    adapter.notifyItemChanged(i + 2, new Integer(1));
            }
        }
//        for (int i = 0; i < datas.size(); i++) {
//            if (datas.get(i).id.equals(id)) {
//                System.out.println(datas.get(i).id);
//                datas.get(i).setState(state);
//            }
//        }
        handler.removeCallbacks(runnable,null);
        handler.sendEmptyMessage(DISSMISS_DIALOG);
    }

    public void forceStopRecyclerViewScroll(RecyclerView mRecyclerView) {
        mRecyclerView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
    }
}
