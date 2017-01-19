package com.suntrans.lanzhouwh.fragment.Main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedFragment;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.sixsensor.SixSensorDetailResult;
import com.suntrans.lanzhouwh.bean.sixsensor.SixSensorInfo;
import com.suntrans.lanzhouwh.bean.sixsensor.UserSixseneorList;
import com.suntrans.lanzhouwh.bean.userinfo.ListInfo;
import com.suntrans.lanzhouwh.fragment.Innerroom.EnvFragment;
import com.suntrans.lanzhouwh.fragment.Innerroom.GraFragment;
import com.suntrans.lanzhouwh.fragment.Innerroom.HumFragment;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.UiUtils;
import com.suntrans.lanzhouwh.views.WaitDialog;
import com.trello.rxlifecycle.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.X;
import static com.tencent.bugly.crashreport.crash.c.e;
import static com.tencent.bugly.crashreport.crash.c.f;
import static com.tencent.bugly.crashreport.crash.c.g;

/**
 * Created by Looney on 2016/11/26.
 * des:室内环境fragment
 */
public class InnerroomFragment extends BasedFragment {
    private View rootView;
    private ViewPager pager;
    private MyAdapter adapter;
    private TextView tab1;
    private TextView tab2;
    private TextView tab3;
    private EnvFragment envFragment;
    private HumFragment humFragment;
    private GraFragment graFragment;
    private final String TAG = "InnerroomFragment";
    TabLayout tabLayout;

    private WaitDialog dialog;
    private Spinner spinner;
    private List<String> strings;
    private ArrayAdapter spinnerAdapter;
    private SixSensorInfo data;

    private int currentIndex = 0;
    private Myhadler myhadler;
    private PtrClassicFrameLayout refreshlayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("InnerroomFragment", "onCreateView");
        rootView = inflater.inflate(R.layout.fragment_room, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
    }

    private void initView(View view) {
        dialog = new WaitDialog(getActivity(), android.support.design.R.style.Base_Theme_AppCompat_Dialog);
        dialog.setCancelable(false);
        dialog.setWaitText("请稍后");
        Log.i("InnerroomFragment", "onViewCreated");
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        pager = (ViewPager) rootView.findViewById(R.id.vp);

        refreshlayout = (PtrClassicFrameLayout) view.findViewById(R.id.refreshlayout);
        refreshlayout.disableWhenHorizontalMove(true);
        myhadler = new Myhadler();
        refreshlayout.setPtrHandler(myhadler);

        adapter = new MyAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(pager);
        strings = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter(getActivity(), R.layout.item_spinner, R.id.tv_spinner, strings);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("spinner第" + position + "被选中了");
                if (lists.size() == 0) {
                    return;
                }
                currentIndex = position;
                String ids = lists.get(position).id;
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                LogUtil.i("id为" + ids);
                getSixSensorDetail(ids);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onResume() {
        getSixSensorList();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i("InnerroomFragment", "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("InnerroomFragment", "onDestroy");
    }


    class Myhadler implements PtrHandler {

        @Override
        public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
            return true;
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            if (lists == null) {
                UiUtils.showToast("获取第六感数据失败,请检查你的网络");
                handler.sendEmptyMessageDelayed(0, 1000);
                return;
            }
            if (lists.size() == 0) {
                UiUtils.showToast("获取第六感数据失败,请检查你的网络");
                handler.sendEmptyMessageDelayed(0, 1000);
                return;
            }
            String ids = lists.get(currentIndex).id;
            getSixSensorDetail(String.valueOf(ids));
            handler.sendEmptyMessageDelayed(0, 2000);

        }
    }


    public static InnerroomFragment newInstance() {
        return new InnerroomFragment();
    }

    class MyAdapter extends FragmentPagerAdapter {
        private String[] titles = {"空气质量", "温湿度", "房屋倾斜度"};

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 1) {
                if (humFragment == null)
                    humFragment = new HumFragment();
                return humFragment;
            } else if (position == 0) {
                if (envFragment == null)
                    envFragment = new EnvFragment();
                return envFragment;
            } else {
                if (graFragment == null)
                    graFragment = new GraFragment();
                return graFragment;
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public ListInfo getCurrentSix() {
        if (lists.size() == 0) {
            return null;
        }
        return lists.get(currentIndex);
    }

    ArrayList<ListInfo> lists = new ArrayList<>();

    private void getSixSensorList() {
        RetrofitHelper.getApi().getSixSensorlist()
                .compose(this.<UserSixseneorList>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserSixseneorList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(UserSixseneorList info) {
                        if (info == null) {
                            UiUtils.showToast("获取第六感信息失败");
                            return;
                        }
                        if (info.rows == null) {
                            UiUtils.showToast("获取第六感信息失败");
                            return;
                        }
                        lists.clear();
                        strings.clear();
                        lists.addAll(info.rows);
                        for (ListInfo li : info.rows) {
                            strings.add(li.name);
                        }
                        spinnerAdapter.notifyDataSetChanged();
                    }
                });
    }


    private void getSixSensorDetail(String id) {
        RetrofitHelper.getApi().getSixSensorDetailInfo(id)
                .compose(this.<SixSensorDetailResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SixSensorDetailResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        UiUtils.showToast("获取第六感信息失败");
                        dialog.dismiss();
                        if (refreshlayout.isRefreshing())
                            refreshlayout.refreshComplete();
                    }

                    @Override
                    public void onNext(SixSensorDetailResult info) {
                        dialog.dismiss();
                        if (info == null) {
                            UiUtils.showToast("获取第六感信息失败");
                            return;
                        }
                        try {
                            if (info.info.isOnline.equals("0")) {
                                new AlertDialog.Builder(getActivity()).setMessage("当前第六感不在线,显示的为最后一次的数据")
                                        .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).create().show();
                            }
                        } catch (Exception e) {

                        }

                        if (info != null) {
                            handler.sendEmptyMessageDelayed(0, 300);
                            data = info.row;
                            LogUtil.i(info.row.toString());
                            if (humFragment != null) {
                                humFragment.setData(data);
                            }
                            if (envFragment != null) {
                                envFragment.setData(data);
                            }
                            if (graFragment != null) {
                                graFragment.setData(data);
                            }
                        }
                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (refreshlayout.isRefreshing())
                        refreshlayout.refreshComplete();
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}
