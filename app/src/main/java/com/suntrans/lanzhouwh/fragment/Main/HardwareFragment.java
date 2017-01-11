package com.suntrans.lanzhouwh.fragment.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.adapter.DividerGridItemDecoration;
import com.suntrans.lanzhouwh.api.RetrofitHelper;
import com.suntrans.lanzhouwh.bean.device.DeviceResult;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.views.Switch;

import java.util.ArrayList;
import java.util.Map;

import ren.solid.skinloader.base.SkinBaseFragment;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/11/26.
 * des:硬件管理fragment
 */
public class HardwareFragment extends SkinBaseFragment {
    final String TAG = "HardwareFragment";

    private ArrayList<Map<String,Object>> datas = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private String did;
    private String[] didList;

    public static HardwareFragment newInstance() {
        return new HardwareFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String deptidlist =  App.getSharedPreferences().getString("deptidlist", "-1");
        didList = deptidlist.split(",");
        View view = inflater.inflate(R.layout.fragment_hardware,null,false);
        return view;
   }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);

        LogUtil.i(TAG,"onViewCreated");
    }

    private void initView(View view) {

//        spinner1 = (Spinner) view.findViewById(spinner1);
//        spinner2 = (Spinner) view.findViewById(spinner2);
//        lock = (RelativeLayout) view.findViewById(R.id.rl_lock);
//        lock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("我被点击了");
//            }
//        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(getActivity());
        adapter= new MyAdapter();
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDeviceList(did);
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));


        did = didList[0];

    }

    private void initData() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.i(TAG,"onAttach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG,"onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.i(TAG,"onPause");
    }



    class MyAdapter extends RecyclerView.Adapter {


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder;
            if (viewType==0)
                holder= new ViewHolder1(LayoutInflater.from(getActivity())
                    .inflate(R.layout.item_switchadapter, parent,false));
            else if (viewType==1)
                holder= new ViewHolder2(LayoutInflater.from(getActivity())
                        .inflate(R.layout.fragment_hardware_item1, parent,false));
            else
                holder= new ViewHolder3(LayoutInflater.from(getActivity())
                        .inflate(R.layout.fragment_hardware_item2, parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewHolder1){
                ((ViewHolder1)(holder)).setData(position);
                ((ViewHolder1)(holder)).setListener(position);
            }

        }


        @Override
        public int getItemViewType(int position) {
           if (position==0)
               return 1;
            if (position==1)
                return 2;
            return 0;
        }

        @Override
        public int getItemCount() {
            return datas.size()+10;
        }

        class ViewHolder1 extends RecyclerView.ViewHolder{
            Switch aSwitch;
            TextView name;
            TextView area;
            TextView name_title;
            ImageView imageView;
            public ViewHolder1(View itemView) {
                super(itemView);
                aSwitch = (Switch) itemView.findViewById(R.id._switch);
                name = (TextView) itemView.findViewById(R.id.name);
                name_title = (TextView) itemView.findViewById(R.id.title_name);
//                area = (TextView) itemView.findViewById(R.id.area);
                imageView = (ImageView) itemView.findViewById(R.id.iv_icon);
            }
            public void setData(int position){

            }
            public void setListener(final int position){
                aSwitch.setOnChangeListener(new Switch.OnSwitchChangedListener() {
                    @Override
                    public void onSwitchChange(Switch switchView, boolean isChecked) {
                    }
                });
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
//                area.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                    }
//                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        }

        class ViewHolder2 extends RecyclerView.ViewHolder{

            public ViewHolder2(View itemView) {
                super(itemView);

            }
            public void setData(int position){

            }
            public void setListener(final int position){

            }
        }

        class ViewHolder3 extends RecyclerView.ViewHolder{

            public ViewHolder3(View itemView) {
                super(itemView);

            }
            public void setData(int position){

            }
            public void setListener(final int position){

            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        getDeviceList(did);
    }

    private void getDeviceList(String did) {

        LogUtil.i(TAG,"did=="+did);
        RetrofitHelper.getApi().getDeviceList(did)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DeviceResult>() {
                    @Override
                    public void onCompleted() {
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        refreshLayout.setRefreshing(false);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(DeviceResult result) {
                        refreshLayout.setRefreshing(false);
                        if (result != null) {
//                            System.out.println(result.data.toString());
                        }else {

                        }
                    }
                });
    }


}
