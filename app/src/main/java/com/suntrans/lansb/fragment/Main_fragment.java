package com.suntrans.lansb.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.suntrans.lansb.MainActivity;
import com.suntrans.lansb.R;

/**
 * Created by Looney on 2016/11/28.
 * des:进入app默认的fragment
 */

public class Main_fragment extends Fragment implements OnTabSelectListener {
    private View rootView;
    private BottomBar bottomBar;
    private HardwareFragment hardwareFragment;
    private InnerroomFragment innerroomFragment;
    private WalletFragment walletFragment;
    private TextView titleName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        bottomBar = (BottomBar) rootView.findViewById(R.id.bottomBar);
        titleName = ((MainActivity)getActivity()).titleName;
        bottomBar.setOnTabSelectListener(this);
    }


    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId){
            case R.id.tab_one:
                titleName.setText("硬件管理");
                if (hardwareFragment==null){
                    hardwareFragment = new HardwareFragment();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content1,hardwareFragment).commit();
                break;
            case R.id.tab_two:
                titleName.setText("室内环境");
                if (innerroomFragment==null){
                    innerroomFragment = new InnerroomFragment();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content1,innerroomFragment).commit();
                break;
            case R.id.tab_three:
                titleName.setText("我的钱包");
                if (walletFragment==null){
                    walletFragment = new WalletFragment();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content1,walletFragment).commit();
                break;
        }
    }
}
