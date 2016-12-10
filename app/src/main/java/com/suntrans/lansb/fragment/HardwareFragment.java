package com.suntrans.lansb.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.suntrans.lansb.R;
import com.suntrans.lansb.utils.LogUtil;
import com.suntrans.lansb.views.Switch;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Looney on 2016/11/26.
 * des:硬件管理fragment
 */
public class HardwareFragment extends Fragment {
    final String TAG = "HardwareFragment";
    Spinner spinner1;
    Spinner spinner2;
    private ArrayList<Map<String,Object>> datas = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter adapter;

    public static HardwareFragment newInstance() {
        return new HardwareFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("硬件管理");
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        View view = inflater.inflate(R.layout.fragment_hardware,null,false);
        LogUtil.i(TAG,"oncreate");
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
        spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        spinner2 = (Spinner) view.findViewById(R.id.spinner2);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(getActivity());
        adapter= new MyAdapter();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
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
            RecyclerView.ViewHolder holder= new ViewHolder1(LayoutInflater.from(getActivity())
                    .inflate(R.layout.item_switchadapter, parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder1)(holder)).setData(position);
            ((ViewHolder1)(holder)).setListener(position);
        }

        @Override
        public int getItemCount() {
            return 7;
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
                area = (TextView) itemView.findViewById(R.id.area);
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
                area.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        }
    }
}
