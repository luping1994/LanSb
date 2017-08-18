package com.suntrans.lanzhouwh.fragment.HomePage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.MainActivity;
import com.suntrans.lanzhouwh.activity.SwitchCon_activity;
import com.suntrans.lanzhouwh.activity.base.BasedFragment;
import com.suntrans.lanzhouwh.adapter.Decoration_item;
import com.suntrans.lanzhouwh.adapter.DividerGridItemDecoration;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.genitem.FloorItem;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.RxBus;
import com.suntrans.lanzhouwh.utils.StatusBarCompat;
import com.suntrans.lanzhouwh.views.LoadingPage;
import com.trello.rxlifecycle.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/3/9.
 */

public class MainFragment_pre extends BasedFragment {

    Toolbar toolbar;
    RecyclerView recyclerview;
    SwipeRefreshLayout refreshlayout;
    Button btError;
    RelativeLayout errorpage;
    RelativeLayout loadingpage;

    private MyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return MainFragment_pre.this.createSuccessView();  //  拿到当前viewPager 添加这个framelayout
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        View statusBar = view.findViewById(R.id.statusbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = StatusBarCompat.getStatusBarHeight(getContext());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) statusBar.getLayoutParams();
            params.height = statusBarHeight;
            statusBar.setLayoutParams(params);
            statusBar.setVisibility(View.VISIBLE);
        }else {
            statusBar.setVisibility(View.GONE);

        }

        loadingpage = (RelativeLayout) view.findViewById(R.id.loadingpage);
        errorpage = (RelativeLayout) view.findViewById(R.id.errorpage);
        btError = (Button) view.findViewById(R.id.bt_error);
        btError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClicked();
            }
        });
        refreshlayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        setUpToolBar(view);
        initView(view);
        load();
    }

    private View createSuccessView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main_pre, null, false);
        return view;
    }

    private void load() {
        loadingpage.setVisibility(View.VISIBLE);
        errorpage.setVisibility(View.INVISIBLE);
        getData();
    }


    public void setUpToolBar(View view) {
        toolbar.setTitle("智能控制");
        toolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        DrawerLayout drawer = ((MainActivity) getActivity()).drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        RxBus.getInstance().toObserverable(String.class)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals("hideHomeIndicator")) {
                            actionBar.setDisplayHomeAsUpEnabled(false);
                            actionBar.setDisplayShowTitleEnabled(true);
                        }

                    }
                });
    }


    List<FloorItem> datas = new ArrayList<>();

    private void initView(View view) {

        datas.clear();

        adapter = new MyAdapter(R.layout.item_floor, datas);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshlayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), SwitchCon_activity.class);
                intent.putExtra("did", datas.get(position).did);
                intent.putExtra("floor", datas.get(position).floor);
                LogUtil.e("did=" + datas.get(position).did);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        });

    }


    public static MainFragment_pre newInstance() {

        return new MainFragment_pre();
    }

    public void onViewClicked() {
        if (loadingpage != null)
            loadingpage.setVisibility(View.VISIBLE);

        if (errorpage != null)
            errorpage.setVisibility(View.INVISIBLE);
        getData();
    }


    public static class MyAdapter extends BaseQuickAdapter<FloorItem, BaseViewHolder> {

        public MyAdapter(int layoutResId, List<FloorItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FloorItem item) {
            int zall = Integer.valueOf(item.zmoff) + Integer.valueOf(item.zmon);
            int call = Integer.valueOf(item.czon) + Integer.valueOf(item.czoff);
            helper.setText(R.id.zhaoming, item.zmon + "/" + zall)
                    .setText(R.id.cazuo, item.czon + "/" + call)
                    .setText(R.id.online, item.online + "/" + item.allstate)
                    .setText(R.id.name, item.floor)
                    .addOnClickListener(R.id.item_content);
//                    .setOnItemClickListener(R.id.item_content,)
        }

    }


    private void getData() {
        RetrofitHelper.getApi2()
                .getAdminDevice()
                .compose(this.<List<FloorItem>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<FloorItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (refreshlayout != null)
                                    refreshlayout.setRefreshing(false);
                                if (datas.size() == 0) {
                                    loadingpage.setVisibility(View.INVISIBLE);
                                    errorpage.setVisibility(View.VISIBLE);
                                }
                            }
                        }, 500);
                    }

                    @Override
                    public void onNext(List<FloorItem> infos) {
                        if (refreshlayout != null)

                            refreshlayout.setRefreshing(false);
                        if (infos != null) {
                            refreshView(infos);
                        } else {
                            if (errorpage != null)
                                errorpage.setVisibility(View.VISIBLE);
                        }

                    }
                });
    }

    private void refreshView(List<FloorItem> infos) {
        if (loadingpage != null)
            loadingpage.setVisibility(View.INVISIBLE);
        datas.clear();
        datas.addAll(infos);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    private Handler handler = new Handler();
}
