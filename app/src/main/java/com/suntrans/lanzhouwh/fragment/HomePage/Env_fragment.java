package com.suntrans.lanzhouwh.fragment.HomePage;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedFragment;
import com.suntrans.lanzhouwh.bean.sixsensor.EnvironmentResult;
import com.suntrans.lanzhouwh.databinding.FragmentEnvDetailBinding;
import com.suntrans.lanzhouwh.utils.RxBus;
import com.suntrans.lanzhouwh.views.ScrollChildSwipeRefreshLayout;

/**
 * Created by Looney on 2017/3/8.
 */

public class Env_fragment extends BasedFragment {

    private EnvironmentResult info;
    private FragmentEnvDetailBinding binding;
    private Rect winRect;
    private boolean isShowProgressBar;
    private boolean isShowProgressBar1;

    public static Env_fragment newInstance(EnvironmentResult result) {
        Env_fragment fragment = new Env_fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", result);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_env_detail, container, false);

        this.info = (EnvironmentResult) getArguments().getSerializable("info");
        info.setEva();
        binding.setInfo(info);
        return binding.getRoot();

    }


    public void setData(EnvironmentResult info) {
        EnvironmentResult infs = info;
        infs.setEva();
        binding.setInfo(infs);
        if (binding.refreshlayout != null) {
            binding.refreshlayout.setRefreshing(false);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Point p = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(p);
        winRect = new Rect(0, 0, p.x, p.y);
        binding.progressBar1.setText(info.pm25 == null ? "" : info.pm25);
        binding.progressBar1.setProgressColor(Color.parseColor("#5bb7e2"));
        binding.progressBar.setProgressColor(Color.WHITE);
        binding.progressBar1.setProgress(info.pm25 == null ? 0 : Float.valueOf(info.pm25).intValue());
        binding.progressBar.setProgress(info.shidu == null ? 0 : Float.valueOf(info.shidu).intValue());
        binding.progressBar.setText(info.shidu + "%");
        binding.progressBar1.reStart();
        binding.progressBar.reStart();
        binding.refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RxBus.getInstance().post("startRefresh");
            }
        });
        if (binding.progressBar.getLocalVisibleRect(winRect)) {
            this.isShowProgressBar = true;
        } else {
            this.isShowProgressBar = false;
        }
        if (binding.progressBar1.getLocalVisibleRect(winRect)) {
            isShowProgressBar1 = true;
        } else {
            isShowProgressBar1 = false;

        }
        binding.scrollView.setSmoothScrollingEnabled(true);
        binding.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int[] prolocation = new int[2];
                int[] pro1location = new int[2];
                binding.progressBar.getLocationInWindow(prolocation);
                binding.progressBar1.getLocationInWindow(pro1location);
                if (binding.progressBar.getLocalVisibleRect(winRect)) {
                    if (!isShowProgressBar)
                        binding.progressBar.reStart();
                    isShowProgressBar = true;
                } else {
                    isShowProgressBar = false;
                }
                if (binding.progressBar1.getLocalVisibleRect(winRect)) {
                    if (!isShowProgressBar1)
                        binding.progressBar1.reStart();
                    isShowProgressBar1 = true;
                } else {
                    isShowProgressBar1 = false;
                }
            }
        });
//        final MyAdapter adapter = new MyAdapter();
//        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
//        recyclerview.setLayoutManager(manager);
//        recyclerview.setAdapter(adapter);
//
//        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        refreshlayout.setRefreshing(false);
//                    }
//                },2000);
//            }
//        });
//        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (newState == 2){
//                    int i = manager.findLastVisibleItemPosition();
//                    View view1 = manager.findViewByPosition(i);
//                    if (i>=0){
//                        adapter.notifyItemChanged(1);
//                        adapter.notifyItemChanged(2);
//                    }
//                }
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
//        recyclerview.addItemDecoration(new DividerGridItemDecoration(getContext()));
    }


//    class MyAdapter extends RecyclerView.Adapter {
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//
//            if (viewType == 0)
//                return new ViewHolder2(LayoutInflater.from(getActivity()).inflate(R.layout.item_env0, parent, false));
//            else if (viewType == 1)
//                return new ViewHolder1(LayoutInflater.from(getActivity()).inflate(R.layout.item_env1, parent, false));
//            else
//                return new ViewHolder3(LayoutInflater.from(getActivity()).inflate(R.layout.item_env2, parent, false));
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            if (holder instanceof ViewHolder1)
//                ((ViewHolder1) holder).setData(position);
//            if (holder instanceof ViewHolder3)
//                ((ViewHolder3) holder).setData(position);
//            if (holder instanceof ViewHolder2)
//                ((ViewHolder2) holder).setData(position);
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return position;
//        }
//
//        @Override
//        public int getItemCount() {
//            return 3;
//        }
//
//        class ViewHolder1 extends RecyclerView.ViewHolder {
//            CircleTextProgressbar progressBar;
//            TextView pm25;
//            TextView pm10;
//            TextView pm1;
//            TextView smoke;
//            TextView jianquan;
//            public ViewHolder1(View itemView) {
//                super(itemView);
//                progressBar = (CircleTextProgressbar) itemView.findViewById(R.id.progressBar);
//                progressBar.setOutLineColor(Color.parseColor("#97989d"));
//                progressBar.setProgressColor(Color.parseColor("#59c4ee"));
//                progressBar.setMaxValue(500);
//                pm25= (TextView) itemView.findViewById(R.id.pm25);
//                pm10= (TextView) itemView.findViewById(R.id.pm10);
//                pm1= (TextView) itemView.findViewById(R.id.pm1);
//                smoke = (TextView) itemView.findViewById(R.id.smoke);
//                jianquan = (TextView) itemView.findViewById(R.id.jiaquan);
//            }
//
//
//            public void setData(int position) {
//                progressBar.setText("轻度污染");
//                progressBar.setProgress(info.pm25==null?0:Float.valueOf(info.pm25).intValue());
//                progressBar.reStart();
//                pm25.setText(info.pm25==null?"暂无数据":info.pm25+"");
//                pm10.setText(info.pm10==null?"暂无数据":info.pm10);
//                pm1.setText(info.pm1==null?"暂无数据":info.pm1);
//                smoke.setText(info.yanwu==null?"暂无数据":info.yanwu);
//                jianquan.setText(info.jiaquan==null?"暂无数据":info.jiaquan);
//            }
//        }
//
//        class ViewHolder2 extends RecyclerView.ViewHolder {
//            TextView wendu;
//            public ViewHolder2(View itemView) {
//                super(itemView);
//                wendu = (TextView) itemView.findViewById(R.id.wendu);
//            }
//
//            public void setData(int position) {
//                wendu.setText(info.wendu==null?"暂无数据":info.wendu+"°");
//            }
//        }
//
//        class ViewHolder3 extends RecyclerView.ViewHolder {
//            CircleTextProgressbar progressBar;
//            TextView wendu ;
//            TextView shidu;
//            TextView light;
//            TextView daqita;
//            public ViewHolder3(View itemView) {
//                super(itemView);
//                progressBar = (CircleTextProgressbar) itemView.findViewById(R.id.progressBar);
//                progressBar.setOutLineColor(Color.parseColor("#97989d"));
//                progressBar.setProgressColor(Color.WHITE);
//                progressBar.setMaxValue(100);
//                wendu  = (TextView) itemView.findViewById(R.id.wendu);
//                shidu = (TextView) itemView.findViewById(R.id.shidu);
//                light = (TextView) itemView.findViewById(R.id.light);
//                daqita = (TextView) itemView.findViewById(R.id.daqiya);
//            }
//
//            public void setData(int position) {
//                progressBar.setText(info.shidu==null?"暂无数据":info.shidu+"%");
//                progressBar.setProgress(info.shidu==null?0:Float.valueOf(info.shidu).intValue());
//                progressBar.reStart();
//                wendu.setText(info.wendu==null?"暂无数据":info.wendu+"℃");
//                shidu.setText(info.shidu==null?"暂无数据":info.shidu+"%");
//                light.setText(info.guangxianqd==null?"暂无数据":info.guangxianqd);
//                daqita.setText("暂无数据");
//            }
//        }
//    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    Handler handler = new Handler();

}
