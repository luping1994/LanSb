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
import com.suntrans.lansb.utils.UiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Looney on 2016/11/26.
 * des:室内环境中的温湿度fragment
 */

public class HumFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    private ArrayList<Map<String, String>> datas = new ArrayList<>();   //数据
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_env,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        manager =new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter= new MyAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        datas.clear();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");   //hh为小写是12小时制，为大写HH时时24小时制
        String date = sDateFormat.format(new    java.util.Date());
        Map<String, String> map0 = new HashMap<String, String>();
        map0.put("Name", "时间");
        map0.put("Value", date);  //当前时间
        map0.put("Title","");  //此栏是否需要显示标题,=-1表示不需要，否则，就填写标题的名称
        map0.put("Image", String.valueOf(R.drawable.time));   //时间图标
        datas.add(map0);

        Map<String,String> map7=new HashMap<String,String>();
        map7.put("Name", "温度");
        map7.put("Value", "--");
        map7.put("Evaluate", "null");
        map7.put("Title", "");
        map7.put("Image", String.valueOf(R.drawable.tmp));
        datas.add(map7);
        Map<String,String> map9=new HashMap<String,String>();
        map9.put("Name", "湿度");
        map9.put("Value", "--");
        map9.put("Evaluate", "null");
        map9.put("Title", "-1");
        map9.put("Image", String.valueOf(R.drawable.humidity));
        datas.add(map9);
        Map<String,String> map8=new HashMap<String,String>();
        map8.put("Name", "气压");
        map8.put("Value", "--");
        map8.put("Evaluate", "null");
        map8.put("Title", "-1");
        map8.put("Image", String.valueOf(R.drawable.atm));
        datas.add(map8);
        Map<String,String> map5=new HashMap<String,String>();
        map5.put("Name", "光照强度");
        map5.put("Value", "--");
        map5.put("Evaluate", "null");
        map5.put("Title", "-1");
        map5.put("Image", String.valueOf(R.drawable.light));
        datas.add(map5);



        Map<String,String> map1=new HashMap<String,String>();
        map1.put("Name", "人员信息");     //参数名称
        map1.put("Value", "--");         //值
        map1.put("Evaluate", "null");    //评估，冷、热等
        map1.put("Title","");
        map1.put("Image", String.valueOf(R.drawable.people));
        datas.add(map1);
    }


    class MyAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder= new viewHolder1(LayoutInflater.from(
                    getActivity()).inflate(R.layout.elecinfo_listview, parent,false));

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            //判断holder是哪个类，从而确定是哪种布局
            /////布局1
            if(holder instanceof viewHolder1) {
                viewHolder1 viewholder = (viewHolder1) holder;
                Map<String, String> map = datas.get(position);
                String Name = map.get("Name");
                String Value = map.get("Value");
                String Evaluate = map.get("Evaluate");
                String Title = map.get("Title");
                if(Evaluate!=null)
                    Value = Value + "(" + Evaluate + ")";
                if(Title.equals("-1"))
                    viewholder.list_header.setVisibility(View.GONE);
                else{
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

        class viewHolder1 extends RecyclerView.ViewHolder
        {
            LinearLayout layout;   //整体布局
            TextView list_header;   //小标题
            ImageView image;    //图标
            TextView name;    //名称
            TextView value;    //参数值
            public viewHolder1(View view)
            {
                super(view);
                layout=(LinearLayout)view.findViewById(R.id.layout);
                list_header = (TextView) view.findViewById(R.id.list_header);
                image=(ImageView)view.findViewById(R.id.image);
                image.clearColorFilter();
                image.setColorFilter(UiUtils.getColor(getActivity(),R.attr.colorPrimary));
                name = (TextView) view.findViewById(R.id.name);
                value = (TextView) view.findViewById(R.id.value);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
