package com.suntrans.lanzhouwh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.genitem.MeterItem;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.suntrans.lanzhouwh.api.RetrofitHelper.getApi2;
import static com.suntrans.lanzhouwh.utils.UiUtils.getContext;

/**
 * Created by Looney on 2017/3/9.
 */

public class EleInfo_Activity extends BasedActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout refreshlayout;
    @BindView(R.id.bt_error)
    Button btError;
    @BindView(R.id.errorpage)
    RelativeLayout errorpage;
    @BindView(R.id.loadingpage)
    RelativeLayout loadingpage;
    @BindView(R.id.content)
    RelativeLayout content;
    private MyAdapter adapter;
    private boolean isAdmin;
    private Observable<MeterItem> observable = null;

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleinfo);
        ButterKnife.bind(this);
        setUpToolBar();
        initView();
    }

    public void setUpToolBar() {
        toolbar.setTitle("用电信息");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }

    List<MeterItem.MeterInfo> datas = new ArrayList<>();

    private void initView() {
        dynamicAddSkinEnableView(toolbar, "background", R.color.colorPrimary);
        isAdmin = App.getSharedPreferences().getBoolean("isAdmin", true);
        if (isAdmin) {
            observable = getApi2().getAdminMeter();

        } else {
            observable = RetrofitHelper.getApi2().getRentMeter();
        }
        datas.clear();
        adapter = new MyAdapter(R.layout.item_eleinfo, datas);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter);
        recyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(EleInfo_Activity.this, EleInfo_detail_Activity.class);
                intent.putExtra("ssid", datas.get(position).ssid);
                intent.putExtra("addr", datas.get(position).addr);
                startActivity(intent);
            }
        });
        refreshlayout.setOnRefreshListener(this);
    }

    @OnClick(R.id.bt_error)
    public void onViewClicked() {
        getData();
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshlayout != null)
                    refreshlayout.setRefreshing(false);
            }
        }, 2000);
        getData();
    }

    public class MyAdapter extends BaseQuickAdapter<MeterItem.MeterInfo, BaseViewHolder> {

        public MyAdapter(int layoutResId, List<MeterItem.MeterInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, MeterItem.MeterInfo item) {
            helper.setText(R.id.addr, "表号：" + item.addr);
            helper.setText(R.id.dev_name, item.devname);
            helper.setText(R.id.didname, item.didname);
            helper.setText(R.id.balance, "账户余额：" + item.balance + "元");
            helper.setText(R.id.dianya, "电压：" + item.dianya + "V");
            helper.setText(R.id.dianliu, "电流：" + item.dianliu + "A");
            if (item instanceof MeterItem.ThreeMeter) {
                helper.setText(R.id.yougonggonglv, "有功功率：" + ((MeterItem.ThreeMeter) item).activepower);
                helper.setText(R.id.gonglvyinsu, "功率因素：" + ((MeterItem.ThreeMeter) item).powerrate);
            } else {
                helper.setText(R.id.yougonggonglv, "有功功率：" + item.yougonggonglv);
                helper.setText(R.id.gonglvyinsu, "功率因素：" + item.gonglvyinshu);
            }
        }

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

    private void getData() {
        setLoadingState();
        observable
                .compose(this.<MeterItem>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MeterItem>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.sendEmptyMessageDelayed(ERROR, 500);
                    }

                    @Override
                    public void onNext(MeterItem infos) {
                        if (refreshlayout != null)
                            refreshlayout.setRefreshing(false);
                        if (infos != null) {
                            refreshView(infos);
                            LogUtil.i(infos.meterlist.size() + "sd");
                        } else {
                            setErrorPage();
                        }

                    }
                });
    }

    private void refreshView(MeterItem infos) {

        setSuccessState();
        if (infos.meterlist != null || infos.threemeterlist != null) {
            datas.clear();
            if (infos.meterlist != null)
                datas.addAll(infos.meterlist);
            if (infos.threemeterlist != null)
                datas.addAll(infos.threemeterlist);
            adapter.notifyDataSetChanged();
        } else {
            handler.sendEmptyMessageDelayed(ERROR, 500);
        }
    }


    protected static final int ERROR = 0;
    private static final int SUCCESS = 1;
    private static final int LOADING = 2;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ERROR:
                    setErrorPage();
                    break;
                case SUCCESS:
                    setSuccessState();

                    break;
                case LOADING:
                    setLoadingState();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
