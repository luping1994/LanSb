package com.suntrans.lanzhouwh.fragment.Innerroom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedFragment;
import com.suntrans.lanzhouwh.adapter.DividerGridItemDecoration;
import com.suntrans.lanzhouwh.bean.sixsensor.SixSensorInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Looney on 2016/11/26.
 * des:室内环境fragment中的房屋倾斜度fragment
 */

public class GraFragment extends BasedFragment {
    private RecyclerView recyclerView;
    //      private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    private ArrayList<Map<String, String>> datas = new ArrayList<>();   //数据
    private final String TAG = "GraFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_env, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated");
        initData();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
//          refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
//          System.out.println(datas.size());
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
//          refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//              @Override
//              public void onRefresh() {
//                  handler.sendEmptyMessageDelayed(1,1000);
//              }
//          });

    }

    private void initData() {
        datas.clear();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");   //hh为小写是12小时制，为大写HH时时24小时制
        String date = sDateFormat.format(new java.util.Date());
        Map<String, String> map0 = new HashMap<String, String>();
        map0.put("Name", "时间");
        map0.put("Value", date);  //当前时间
        map0.put("Title", "");  //此栏是否需要显示标题,=-1表示不需要，否则，就填写标题的名称
        map0.put("Image", String.valueOf(R.drawable.time));   //时间图标
        datas.add(map0);

        Map<String, String> map7 = new HashMap<String, String>();
        map7.put("Name", "X轴倾斜角");
        map7.put("Value", "--");
        map7.put("Evaluate", "");
        map7.put("Title", "");
        map7.put("Image", String.valueOf(R.drawable.angel));
        datas.add(map7);
        Map<String, String> map9 = new HashMap<String, String>();
        map9.put("Name", "Y轴倾斜角");
        map9.put("Value", "--");
        map9.put("Evaluate", "");
        map9.put("Title", "-1");
        map9.put("Image", String.valueOf(R.drawable.angle));
        datas.add(map9);
        Map<String, String> map8 = new HashMap<String, String>();
        map8.put("Name", "Z轴倾斜角");
        map8.put("Value", "--");
        map8.put("Evaluate", "");
        map8.put("Title", "-1");
        map8.put("Image", String.valueOf(R.drawable.angle));
        datas.add(map8);
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("Name", "振动强度");
        map5.put("Value", "--");
        map5.put("Evaluate", "");
        map5.put("Title", "-1");
        map5.put("Image", String.valueOf(R.drawable.shake));  //烟雾图标
        datas.add(map5);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
//                      refreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = new MyAdapter.viewHolder1(LayoutInflater.from(
                    getActivity()).inflate(R.layout.elecinfo_listview, parent, false));

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            //判断holder是哪个类，从而确定是哪种布局
            /////布局1
            if (holder instanceof MyAdapter.viewHolder1) {
                MyAdapter.viewHolder1 viewholder = (viewHolder1) holder;
                Map<String, String> map = datas.get(position);
                String Name = map.get("Name");
                String Value = map.get("Value");
                String Evaluate = map.get("Evaluate");
                String Title = map.get("Title");
//                  if(Evaluate!=null)
//                      Value = Value + "(" + Evaluate + ")";
                if (Title.equals("-1"))
                    viewholder.list_header.setVisibility(View.GONE);
                else {
                    viewholder.list_header.setVisibility(View.VISIBLE);
                    viewholder.list_header.setText(Title);
                }
                Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), Integer.valueOf(map.get("Image")));
                viewholder.image.setImageBitmap(bitmap);
                viewholder.name.setText(Name);
                viewholder.value.setText(Value);
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class viewHolder1 extends RecyclerView.ViewHolder {
            LinearLayout layout;   //整体布局
            TextView list_header;   //小标题
            ImageView image;    //图标
            TextView name;    //名称
            TextView value;    //参数值

            public viewHolder1(View view) {
                super(view);
                layout = (LinearLayout) view.findViewById(R.id.layout);
                list_header = (TextView) view.findViewById(R.id.list_header);
                image = (ImageView) view.findViewById(R.id.image);
                name = (TextView) view.findViewById(R.id.name);
                value = (TextView) view.findViewById(R.id.value);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    public void setData(SixSensorInfo info) {
        try {
            datas.get(0).put("Value", info.created_at);
            datas.get(1).put("Value", info.xzhouqxj + "°");
            datas.get(2).put("Value", info.yzhouqxj + "°");
            datas.get(3).put("Value", info.pingmianqxj + "°");
            datas.get(4).put("Value", info.zhendongqd+"G");
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ;

    @Override
    public void onDestroyView() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }
}
