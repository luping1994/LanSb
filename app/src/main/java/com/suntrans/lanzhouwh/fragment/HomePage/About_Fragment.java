package com.suntrans.lanzhouwh.fragment.HomePage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.MainActivity;
import com.suntrans.lanzhouwh.activity.base.BasedFragment;

/**
 * Created by Looney on 2016/12/9.
 */

public class About_Fragment extends BasedFragment implements View.OnClickListener {
    /**
     * toolbar控件
     */
    private RelativeLayout toolbarRight;
    private TextView titleName;
    private TextView textView;
    private ImageView ivRight;
    LinearLayout leftIcon;
    ImageView leftImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
    }

    private void initView(View view) {
        titleName = (TextView) view.findViewById(R.id.title_name);
        toolbarRight = (RelativeLayout) view.findViewById(R.id.layout_right);
        textView = (TextView) view.findViewById(R.id.tv_right);
        ivRight = (ImageView) view.findViewById(R.id.iv_right);
        ivRight.setImageDrawable(view.getResources().getDrawable(R.drawable.alert));
        leftIcon = (LinearLayout) view.findViewById(R.id.left_icon);
        leftImage = (ImageView) view.findViewById(R.id.left_image);
        titleName.setText("关于");
        leftIcon.setOnClickListener(this);
        toolbarRight.setOnClickListener(this);
    }

    public static About_Fragment newInstance() {
        return new About_Fragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_icon:
                ((MainActivity) getActivity()).drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.layout_right:
                break;
        }
    }
}
