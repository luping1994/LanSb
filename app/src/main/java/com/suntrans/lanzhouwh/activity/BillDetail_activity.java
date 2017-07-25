package com.suntrans.lanzhouwh.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;
import com.suntrans.lanzhouwh.adapter.Decoration_item;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.genitem.BillItem;
import com.suntrans.lanzhouwh.bean.genitem.BillResult;
import com.suntrans.lanzhouwh.views.FullyLinearLayoutManager;
import com.suntrans.lanzhouwh.views.ScrollChildSwipeRefreshLayout;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/3/10.
 */
public class BillDetail_activity extends BasedActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.recyclerview2)
    RecyclerView recyclerview2;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.refreshlayout)
    ScrollChildSwipeRefreshLayout refreshlayout;
    @BindView(R.id.elec)
    TextView elec;
    @BindView(R.id.charge)
    TextView charge;


    private String ssid;
    private String addr;
    private Runnable runnable;
    private List<BillItem> datas1;
    private List<BillItem> datas2;
    private MyAdapter adapter1;
    private MyAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billdetail);
        ButterKnife.bind(this);
        ssid = getIntent().getStringExtra("ssid");
        addr = getIntent().getStringExtra("addr");
        setUpToolBar();
        initView();
        refreshlayout.setOnRefreshListener(this);

    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }

    private void initView() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (refreshlayout != null)
                    refreshlayout.setRefreshing(false);
            }
        };
        datas1 = new ArrayList<>();
        datas2 = new ArrayList<>();

        adapter1 = new MyAdapter(R.layout.item_bill1, datas1, 1);
        adapter2 = new MyAdapter(R.layout.item_bill2, datas2, 2);
        FullyLinearLayoutManager manager1 = new FullyLinearLayoutManager(this);
        FullyLinearLayoutManager manager2 = new FullyLinearLayoutManager(this);
        recyclerview.setLayoutManager(manager1);
        recyclerview.setAdapter(adapter1);
        recyclerview2.setLayoutManager(manager2);
        recyclerview2.setAdapter(adapter2);

        recyclerview.addItemDecoration(new Decoration_item(this));
        recyclerview2.addItemDecoration(new Decoration_item(this));
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview2.setNestedScrollingEnabled(false);
        llTop.setFocusable(true);
        llTop.setFocusableInTouchMode(true);
        llTop.requestFocus();

    }

    public void setUpToolBar() {

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        toolbar.setTitle(addr);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onRefresh() {
        getData();
        handler.postDelayed(runnable, 2000);
    }

    public class MyAdapter extends BaseQuickAdapter<BillItem, BaseViewHolder> {
        private int type;

        public MyAdapter(int layoutResId, List<BillItem> data, int type) {
            super(layoutResId, data);
            this.type = type;
        }

        @Override
        protected void convert(BaseViewHolder helper, BillItem item) {
            if (type == 1) {
                helper.setText(R.id.month, item.month);
                helper.setText(R.id.ele_total, item.elec);
                helper.setText(R.id.dianfei, item.charge);
                helper.setText(R.id.spends, item.recharge);

            } else {
                helper.setText(R.id.year, item.year);
                helper.setText(R.id.ele_total, item.elec);
                helper.setText(R.id.dianfei, item.charge);
                helper.setText(R.id.spends, item.recharge);
            }
        }
    }

    private void getData() {
        RetrofitHelper.getApi2().getMeterBill(ssid)
                .compose(this.<BillResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BillResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.removeCallbacksAndMessages(null);
                        handler.post(runnable);
                    }

                    @Override
                    public void onNext(BillResult result) {
                        handler.removeCallbacksAndMessages(null);
                        handler.post(runnable);
                        if (result != null) {
                            elec.setText("总用电量：" + result.elec);
                            charge.setText("总电费：" + result.charge);
                            datas1.clear();
                            datas2.clear();
                            datas1.addAll(result.monthlist);
                            datas2.addAll(result.yearlist);
                            adapter1.notifyDataSetChanged();
                            adapter2.notifyDataSetChanged();

                        }
                    }
                });
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
        super.onDestroy();
    }
}
