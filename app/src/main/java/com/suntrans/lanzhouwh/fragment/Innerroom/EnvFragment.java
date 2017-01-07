package com.suntrans.lanzhouwh.fragment.Innerroom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.adapter.DividerGridItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ren.solid.skinloader.base.SkinBaseFragment;
import ren.solid.skinloader.entity.MyImageView;

/**
 * Created by Looney on 2016/11/26.
 * des:室内环境中的空气纯度fragment
 */

public class EnvFragment extends SkinBaseFragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    private ArrayList<Map<String, String>> datas = new ArrayList<>();   //数据

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_env, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));


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
        map7.put("Name", "PM1");
        map7.put("Value", "null");
        map7.put("Evaluate", "null");
        map7.put("Title", "");
        map7.put("Image", String.valueOf(R.drawable.pm));
        datas.add(map7);
        Map<String, String> map9 = new HashMap<String, String>();
        map9.put("Name", "PM10");
        map9.put("Value", "null");
        map9.put("Evaluate", "null");
        map9.put("Title", "-1");
        map9.put("Image", String.valueOf(R.drawable.pm));
        datas.add(map9);
        Map<String, String> map8 = new HashMap<String, String>();
        map8.put("Name", "PM2.5");
        map8.put("Value", "null");
        map8.put("Evaluate", "null");
        map8.put("Title", "-1");
        map8.put("Image", String.valueOf(R.drawable.pm));
        datas.add(map8);
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("Name", "甲醛");
        map5.put("Value", "--ppm");
        map5.put("Evaluate", "null");
        map5.put("Title", "-1");
        map5.put("Image", String.valueOf(R.drawable.smoke));  //烟雾图标
        datas.add(map5);


        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("Name", "烟雾");     //参数名称
        map1.put("Value", "--ppm");         //值
        map1.put("Evaluate", "null");    //评估，冷、热等
        map1.put("Title", "");
        map1.put("Image", String.valueOf(R.drawable.smoke));  //温度图标
        datas.add(map1);
    }


    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.elecinfo_listview, parent, false);
            RecyclerView.ViewHolder holder = new viewHolder1(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            //判断holder是哪个类，从而确定是哪种布局
            /////布局1
            if (holder instanceof viewHolder1) {
                viewHolder1 viewholder = (viewHolder1) holder;
                Map<String, String> map = datas.get(position);
                String Name = map.get("Name");
                String Value = map.get("Value");
                String Evaluate = map.get("Evaluate");
                String Title = map.get("Title");
                if (Evaluate != null)
                    Value = Value + "(" + Evaluate + ")";
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
            MyImageView image;    //图标
            TextView name;    //名称
            TextView value;    //参数值

            public viewHolder1(View view) {
                super(view);
                layout = (LinearLayout) view.findViewById(R.id.layout);
                list_header = (TextView) view.findViewById(R.id.list_header);
                image = (MyImageView) view.findViewById(R.id.image);

                name = (TextView) view.findViewById(R.id.name);
                value = (TextView) view.findViewById(R.id.value);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("条目被点击了"+getAdapterPosition());
                    }
                });
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}