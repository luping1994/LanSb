package com.suntrans.lansb.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntrans.lansb.R;
import com.suntrans.lansb.adapter.DividerGridItemDecoration;
import com.suntrans.lansb.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Looney on 2016/11/26.
 * des:用电信息fragment
 */

public class EleinfoFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    private ArrayList<Map<String, String>> datas = new ArrayList<>();   //数据
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ele,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        manager =new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        adapter= new MyAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        datas.clear();
        Map<String, String> map0 = new HashMap<String, String>();
        map0.put("Name", "用电量");
        map0.put("Value", "--");  //当前时间
        map0.put("Title","-1");  //此栏是否需要显示标题,=-1表示不需要，否则，就填写标题的名称
        map0.put("Image", String.valueOf(R.drawable.time));   //时间图标
        datas.add(map0);

        Map<String,String> map7=new HashMap<String,String>();
        map7.put("Name", "电压");
        map7.put("Value", "--");
        map7.put("Evaluate", "--");
        map7.put("Title", "-1");
        map7.put("Image", String.valueOf(R.drawable.pm));
        datas.add(map7);
        Map<String,String> map9=new HashMap<String,String>();
        map9.put("Name", "电流");
        map9.put("Value", "--");
        map9.put("Evaluate", "--");
        map9.put("Title", "-1");
        map9.put("Image", String.valueOf(R.drawable.pm));
        datas.add(map9);
        Map<String,String> map8=new HashMap<String,String>();
        map8.put("Name", "有功功率");
        map8.put("Value", "--");
        map8.put("Evaluate", "--");
        map8.put("Title", "-1");
        map8.put("Image", String.valueOf(R.drawable.pm));
        datas.add(map8);
        Map<String,String> map5=new HashMap<String,String>();
        map5.put("Name", "功率因素");
        map5.put("Value", "--");
        map5.put("Evaluate", "--");
        map5.put("Title", "-1");
        map5.put("Image", String.valueOf(R.drawable.smoke));  //烟雾图标
        datas.add(map5);

    }

    public static EleinfoFragment newInstance() {
        return new EleinfoFragment();
    }


    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = new viewHolder1(LayoutInflater.from(
                    getActivity()).inflate(R.layout.elecinfo_listview, parent, false));

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
//                if (Evaluate != null)
//                    Value = Value + "(" + Evaluate + ")";
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
                image.clearColorFilter();
                image.setColorFilter(UiUtils.getColor(getActivity(), R.attr.colorPrimary));
                name = (TextView) view.findViewById(R.id.name);
                value = (TextView) view.findViewById(R.id.value);
            }
        }
    }
}
