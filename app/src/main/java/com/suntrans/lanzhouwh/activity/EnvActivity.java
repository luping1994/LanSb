package com.suntrans.lanzhouwh.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.LoginResult.LoginResult;
import com.suntrans.lanzhouwh.bean.sixsensor.EnvironmentResult;
import com.suntrans.lanzhouwh.fragment.HomePage.Env_fragment;
import com.suntrans.lanzhouwh.utils.Encryp;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.RxBus;
import com.suntrans.lanzhouwh.utils.UiUtils;
import com.suntrans.lanzhouwh.views.LoadingDialog;
import com.suntrans.lanzhouwh.views.WaitDialog;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/3/8.
 */

public class EnvActivity extends BasedActivity {
    public static final String TAG = "Env_activity";

    //    @BindView(R.id.title_name)
//    TextView titleName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @BindView(R.id.errorpage)
    RelativeLayout errorpage;
    @BindView(R.id.loadingpage)
    RelativeLayout loadingpage;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.bt_error)
    Button btError;


    private List<EnvironmentResult> datas;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private List<String> spinnerDatas;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_env);
        ButterKnife.bind(this);
        setUpToolBar();
    }

    public void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        checkRemoteAccount(getIntent().getStringExtra("username"));
    }

    private void init() {
        datas = new ArrayList<>();
        adapter = new MyAdapter(getSupportFragmentManager(), datas);
        fragments = new ArrayList<>();
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new PagerListener());
        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerDatas = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, spinnerDatas);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewpager.setCurrentItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        RxBus.getInstance().toObserverable(String.class)
                .compose(this.<String>bindToLifecycle())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals("startRefresh")) {
                            getData(false);
                        }
                    }
                });
        getData(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getData(boolean isShowLoading) {
        if (isShowLoading)
            setLoadingState();
        RetrofitHelper.getApi3().getEnvironment()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<EnvironmentResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setErrorPage();
                            }
                        }, 600);
                    }

                    @Override
                    public void onNext(List<EnvironmentResult> infos) {

                        if (infos != null) {
                            initView(infos);
                        } else {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setErrorPage();
                                }
                            }, 600);
                        }

                    }
                });
    }

    private void initView(List<EnvironmentResult> infos) {
        datas.clear();
        datas.addAll(infos);
        initIndicator();

        if (infos != null) {
            spinnerDatas.clear();
            for (int i = 0; i < infos.size(); i++) {
                spinnerDatas.add(infos.get(i).devname);
            }
        }

        if (fragments.size() == 0) {
            for (int i = 0; i < datas.size(); i++) {
                Env_fragment fragment = Env_fragment.newInstance(datas.get(i));
                fragments.add(fragment);
            }
        } else {
            for (int i = 0; i < fragments.size(); i++) {
                fragments.get(i).setData(datas.get(i));
            }
        }

        adapter.notifyDataSetChanged();

        viewpager.setOffscreenPageLimit(datas.size() - 1);
        LogUtil.e(TAG, "页面数量：" + fragments.size() + "");
        spinnerAdapter.notifyDataSetChanged();
        setCurrentIndicator(currentIndex);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setSuccessState();
            }
        }, 400);

    }


    int mPointWidth;// 圆点间的距离
    private int indicator_select = UiUtils.dip2px(5);
    private int indicator_unselect = UiUtils.dip2px(3);
    LinearLayout.LayoutParams params_select;
    LinearLayout.LayoutParams params_unselected;

    private void initIndicator() {

        llPointGroup.removeAllViews();

        params_select = new LinearLayout.LayoutParams(
                indicator_select, indicator_select);
        params_unselected = new LinearLayout.LayoutParams(
                indicator_unselect, indicator_unselect);
        params_select.leftMargin = UiUtils.dip2px(10);// 设置圆点间隔
        params_unselected.leftMargin = UiUtils.dip2px(10);// 设置圆点间隔

        for (int i = 0; i < datas.size(); i++) {
            View point = new View(this);
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_point_red);// 设置引导页默认圆点
                point.setLayoutParams(params_select);// 设置圆点的大小
                llPointGroup.addView(point);// 将圆点添加给线性布局
            } else {

                point.setBackgroundResource(R.drawable.shape_point_gray);// 设置引导页默认圆点
                point.setLayoutParams(params_unselected);// 设置圆点的大小
                llPointGroup.addView(point);// 将圆点添加给线性布局
            }

        }
        // 获取视图树, 对layout结束事件进行监听
        llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    // 当layout执行结束后回调此方法
                    @Override
                    public void onGlobalLayout() {
                        llPointGroup.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        mPointWidth = llPointGroup.getChildAt(1).getLeft()
                                - llPointGroup.getChildAt(0).getLeft();
                    }
                });
    }


    List<Env_fragment> fragments;

    @OnClick(R.id.bt_error)
    public void onViewClicked() {
        getData(true);
    }


    class MyAdapter extends FragmentPagerAdapter {
        private List<EnvironmentResult> titles;

        public MyAdapter(FragmentManager fm, List<EnvironmentResult> titles) {
            super(fm);
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position).devname;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    Handler handler = new Handler();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }


    private int currentIndex = 0;

    class PagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentIndex = position;
            spinner.setSelection(position);
            setCurrentIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setCurrentIndicator(int position) {
        for (int i = 0; i < llPointGroup.getChildCount(); i++) {
            if (i == position) {
                llPointGroup.getChildAt(i).setBackgroundResource(R.drawable.shape_point_red);
                llPointGroup.getChildAt(i).setLayoutParams(params_select);
            } else {
                llPointGroup.getChildAt(i).setBackgroundResource(R.drawable.shape_point_gray);
                llPointGroup.getChildAt(i).setLayoutParams(params_unselected);
            }
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_env, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
//            UiUtils.showToast("This feature is under development and not well tested.");
            if (datas.size() == 0)
                return true;
//
            Intent intent = new Intent();
            intent.putExtra("id", datas.get(viewpager.getCurrentItem()).ssid);
//            intent.putExtra("id", "366");
            intent.setClass(this, Setting_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLoadingState() {
        loadingpage.setVisibility(View.VISIBLE);
        content.setVisibility(View.INVISIBLE);
        errorpage.setVisibility(View.INVISIBLE);
    }

    private void setSuccessState() {
        loadingpage.setVisibility(View.INVISIBLE);
        content.setVisibility(View.VISIBLE);
        errorpage.setVisibility(View.INVISIBLE);
    }

    private void setErrorPage() {
        loadingpage.setVisibility(View.INVISIBLE);
        content.setVisibility(View.INVISIBLE);
        errorpage.setVisibility(View.VISIBLE);
    }



    private LoadingDialog dialog;
    private static final String key = "QxrLiqUU1tB5YIMKioiNT0az6f7xl1hK";
    private void checkRemoteAccount(String account) {
        if (dialog==null){
            dialog = new LoadingDialog(this,R.style.loading_dialog);
            dialog.setCancelable(false);
            dialog.setWaitText("请稍后");
            dialog.show();
        }
        if (account == null) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("账号为空")
                    .create().show();
            return;
        }
        String time = getIntent().getStringExtra("time");
        String sig = getIntent().getStringExtra("sig");
        String siged = Encryp.md5(time + key);
        if (!sig.equals(siged)) {
            dialog.dismiss();
            new AlertDialog.Builder(EnvActivity.this)
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
                        dialog.dismiss();

                    }

                    @Override
                    public void onNext(LoginResult info) {
                        dialog.dismiss();

                        if (info != null) {
                            if (info.access_token != null) {
                                if (!info.access_token.equals("")) {
                                    LogUtil.i(info.toString());
                                    SharedPreferences.Editor editor = App.getSharedPreferences().edit();
                                    editor.putString("expires_in1", info.expires_in);
                                    editor.putString("access_token1", info.access_token);
                                    editor.putLong("firsttime1", System.currentTimeMillis());
                                    editor.commit();

                                    init();

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
        new AlertDialog.Builder(EnvActivity.this)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create().show();
    }

}
