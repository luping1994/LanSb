package com.suntrans.lansb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.FrameStats;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntrans.lansb.R;

/**
 * Created by Looney on 2016/11/26.
 * des:室内环境fragment
 */

public class InnerroomFragment extends Fragment {
    private View rootView;
    private TabLayout tabLayout;
    private ViewPager pager;
    private MyAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_room,container,false);
        TextView textView = new TextView(getActivity());
        textView.setText("室内环境");
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        return textView;
   }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        pager = (ViewPager) rootView.findViewById(R.id.vp);
        adapter = new MyAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);

    }

    class MyAdapter extends FragmentPagerAdapter{
        private String [] titles = {"空气质量","温湿度","房屋倾斜度"};
        private Fragment[] fragments;
        public MyAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        //这个方法返回Tab显示的文字。这里通过在实例化TabFragment的时候，传入的title参数返回标题。
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
