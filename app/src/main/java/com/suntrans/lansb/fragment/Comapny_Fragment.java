package com.suntrans.lansb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntrans.lansb.R;
import com.suntrans.lansb.adapter.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Looney on 2016/12/7.
 */

public class Comapny_Fragment extends Fragment implements View.OnClickListener {

    private TextView titleName;
    private TextView textView;
    private ImageView ivRight;
    private RelativeLayout toolbarRight;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    private ArrayList<Map<String,String>> datas = new ArrayList<>(20);

    public static Comapny_Fragment newInstance() {
        return new Comapny_Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compant,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        titleName = (TextView) getActivity().findViewById(R.id.title_name);
        toolbarRight = (RelativeLayout) getActivity().findViewById(R.id.layout_right);
        textView = (TextView) getActivity().findViewById(R.id.tv_right);
        ivRight = (ImageView) getActivity().findViewById(R.id.iv_right);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        titleName.setText("公司员工");
        ivRight.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        toolbarRight.setOnClickListener(this);
        ivRight.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.add));
        manager = new LinearLayoutManager(getActivity());
        adapter = new MyAdapter();
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_right:
                break;
        }
    }



    class MyAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
                view =  LayoutInflater.from(getActivity()).inflate(R.layout.item_company1,parent,false);
                return new ViewHolder2(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }


        @Override
        public int getItemCount() {
            return 20;
        }
    }

    class ViewHolder1 extends RecyclerView.ViewHolder{

        public ViewHolder1(View itemView) {
            super(itemView);
        }
    }
    class ViewHolder2 extends RecyclerView.ViewHolder{

        public ViewHolder2(View itemView) {
            super(itemView);
        }
    }
}
