package com.suntrans.lansb.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suntrans.lansb.R;
/**
 * Created by Looney on 2016/11/26.
 * des:室内环境fragment
 */
public class InnerroomFragment extends Fragment {
    private View rootView;
    private ViewPager pager;
    private MyAdapter adapter;
    private TextView tab1;
    private TextView tab2;
    private TextView tab3;
    private EnvFragment envFragment;
    private HumFragment humFragment;
    private GraFragment graFragment;
    private final String TAG="InnerroomFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("InnerroomFragment","onCreateView");
        rootView = inflater.inflate(R.layout.fragment_room,container,false);
        return rootView;
   }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i("InnerroomFragment","onViewCreated");
        pager = (ViewPager) rootView.findViewById(R.id.vp);
        adapter = new MyAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);

        tab1 = (TextView)view. findViewById(R.id.tab1);
        tab2 = (TextView) view.findViewById(R.id.tab2);
        tab3 = (TextView) view.findViewById(R.id.tab3);

        tab1.setOnClickListener(new MyClickListener());
        tab2.setOnClickListener(new MyClickListener());
        tab3.setOnClickListener(new MyClickListener());

        pager.addOnPageChangeListener(new MyPagerChangedListener());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("InnerroomFragment","onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("InnerroomFragment","onDestroy");
    }

    public static InnerroomFragment newInstance() {
        return new InnerroomFragment();
    }

    class MyAdapter extends FragmentPagerAdapter {
        private String [] titles = {"空气质量","温湿度","房屋倾斜度"};
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            if (position==1){
                if (humFragment==null)
                    humFragment = new HumFragment();
                return humFragment;
            }else if (position==0){
                if (envFragment == null)
                    envFragment = new EnvFragment();
                return envFragment;
            }else {
                if (graFragment==null)
                    graFragment = new GraFragment();
                return graFragment;
            }
        }
        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tab1:
                    tab1.setTextColor(Color.CYAN);
                    tab2.setTextColor(Color.BLACK);
                    tab3.setTextColor(Color.BLACK);

                    tab1.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_select));
                    tab2.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_unselect));
                    tab3.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_unselect));

                    pager.setCurrentItem(0);
                    break;
                case R.id.tab2:
                    tab2.setTextColor(Color.CYAN);
                    tab1.setTextColor(Color.BLACK);
                    tab3.setTextColor(Color.BLACK);

                    tab2.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_select));
                    tab1.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_unselect));
                    tab3.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_unselect));

                    pager.setCurrentItem(1);
                    break;
                case R.id.tab3:
                    tab3.setTextColor(Color.CYAN);
                    tab1.setTextColor(Color.BLACK);
                    tab2.setTextColor(Color.BLACK);

                    tab3.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_select));
                    tab1.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_unselect));
                    tab2.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_unselect));

                    pager.setCurrentItem(2);
                    break;
            }
        }
    }

    class MyPagerChangedListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    tab1.setTextColor(Color.CYAN);
                    tab2.setTextColor(Color.BLACK);
                    tab3.setTextColor(Color.BLACK);

                    tab1.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_select));
                    tab2.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_unselect));
                    tab3.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_unselect));

                    break;
                case 1:
                    tab2.setTextColor(Color.CYAN);
                    tab1.setTextColor(Color.BLACK);
                    tab3.setTextColor(Color.BLACK);

                    tab2.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_select));
                    tab1.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_unselect));
                    tab3.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_unselect));

                    break;
                case 2:
                    tab3.setTextColor(Color.CYAN);
                    tab1.setTextColor(Color.BLACK);
                    tab2.setTextColor(Color.BLACK);

                    tab3.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_select));
                    tab1.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_unselect));
                    tab2.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_unselect));

                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
