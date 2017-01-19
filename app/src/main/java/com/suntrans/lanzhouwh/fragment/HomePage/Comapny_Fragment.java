package com.suntrans.lanzhouwh.fragment.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.AddStaffActivity;
import com.suntrans.lanzhouwh.activity.MainActivity;
import com.suntrans.lanzhouwh.activity.base.BasedFragment;
import com.suntrans.lanzhouwh.adapter.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Looney on 2016/12/7.
 */

public class Comapny_Fragment extends BasedFragment implements View.OnClickListener {

    private TextView titleName;
    private TextView textView;
    private ImageView ivRight;
    private RelativeLayout toolbarRight;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    private ArrayList<Map<String,String>> datas = new ArrayList<>(20);
    LinearLayout leftIcon;
    ImageView leftImage;

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
        titleName = (TextView) view.findViewById(R.id.title_name);
        leftIcon = (LinearLayout) view.findViewById(R.id.left_icon);
        toolbarRight = (RelativeLayout) view.findViewById(R.id.layout_right);
        textView = (TextView) view.findViewById(R.id.tv_right);
        ivRight = (ImageView) view.findViewById(R.id.iv_right);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);

        leftImage = (ImageView) view.findViewById(R.id.left_image);

        leftIcon.setOnClickListener(this);
        titleName.setText("公司员工");
        ivRight.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        ivRight.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.add));
        toolbarRight.setOnClickListener(this);
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
                startActivity(new Intent(getActivity(), AddStaffActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.left_icon:
                ((MainActivity)getActivity()).drawerLayout.openDrawer(GravityCompat.START);
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
            return 0;
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

        }
    }
}
