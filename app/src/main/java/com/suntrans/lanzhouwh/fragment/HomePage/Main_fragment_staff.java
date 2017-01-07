package com.suntrans.lanzhouwh.fragment.HomePage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.MainActivity;
import com.suntrans.lanzhouwh.activity.Setting_Activity;
import com.suntrans.lanzhouwh.fragment.Innerroom.RuleFragment;
import com.suntrans.lanzhouwh.fragment.Main.HardwareFragment;
import com.suntrans.lanzhouwh.fragment.Main.InnerroomFragment;

import ren.solid.skinloader.base.SkinBaseFragment;

/**
 * Created by Looney on 2016/11/28.
 * des:进入app默认的fragment
 */
public class Main_fragment_staff extends SkinBaseFragment implements OnTabSelectListener, View.OnClickListener {

    private View rootView;
    private BottomBar bottomBar;
    private RelativeLayout toolbarRight;
    private TextView titleName;
    private TextView textView;
    private ImageView ivRight;

    LinearLayout leftIcon;
    ImageView leftImage;

    private Fragment[] fragments;
    private int currentIndex =0;
    public static Main_fragment_staff newInstance(){
        return new Main_fragment_staff();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initFragments();
        rootView = inflater.inflate(R.layout.fragment_main_staff,container,false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    private void initView(View view) {
        bottomBar = (BottomBar) view.findViewById(R.id.bottomBar);
        titleName = (TextView) view.findViewById(R.id.title_name);
        toolbarRight = (RelativeLayout) view.findViewById(R.id.layout_right);
        textView = (TextView) view.findViewById(R.id.tv_right);
        ivRight = (ImageView) view.findViewById(R.id.iv_right);
        ivRight.setImageDrawable(view.getResources().getDrawable(R.drawable.alert));
        leftIcon = (LinearLayout) view.findViewById(R.id.left_icon);
        leftImage = (ImageView) view.findViewById(R.id.left_image);
        leftIcon.setOnClickListener(this);
        bottomBar.setOnTabSelectListener(this);
        toolbarRight.setOnClickListener(this);

        dynamicAddSkinView(bottomBar,"bb_activeTabColor",R.color.colorPrimary);
//        dynamicAddSkinView(bottomBar,"bb_inActiveTabColor",R.color.colorPrimary);

    }

    private void initFragments(){
         HardwareFragment hardwareFragment = HardwareFragment.newInstance();
         InnerroomFragment innerroomFragment = InnerroomFragment.newInstance();
         RuleFragment eleinfoFragment = RuleFragment.newInstance();
        fragments = new Fragment[]{
                hardwareFragment,
                innerroomFragment,
                eleinfoFragment
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_icon:
                ((MainActivity)getActivity()).drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.layout_right:
                switch (currentIndex){
                    case 0:
//                        System.out.println("我是硬件管理");
                        break;
                    case 1:
                        System.out.println("我是室内环境");
                        Intent intent = new Intent(getActivity(), Setting_Activity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                        break;
                    case 2:
                        System.out.println("我是用电信息");
                        break;
                }
                break;
        }
    }


    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId){
            case R.id.tab_one:
                titleName.setText("硬件管理");
                textView.setVisibility(View.GONE);
                ivRight.setVisibility(View.VISIBLE);
                ivRight.setColorFilter(Color.WHITE);
                switchFragment(0);
                currentIndex =  0;
                break;
            case R.id.tab_two:
                textView.setVisibility(View.VISIBLE);
                textView.setText("报警配置");
                ivRight.setVisibility(View.GONE);
                titleName.setText("室内环境");
                switchFragment(1);
                currentIndex =  1;
                break;
            case R.id.tab_three:
                textView.setVisibility(View.INVISIBLE);
//                textView.setText("我的账单");
                ivRight.setVisibility(View.GONE);
                titleName.setText("公司制度");
                switchFragment(2);
                currentIndex=2;
                break;
        }
    }

    private void switchFragment(int index) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.hide(fragments[currentIndex]);
        if (!fragments[index].isAdded()){
            transaction.add(R.id.content1,fragments[index]);
        }
        transaction.show(fragments[index]).commit();
    }
}
