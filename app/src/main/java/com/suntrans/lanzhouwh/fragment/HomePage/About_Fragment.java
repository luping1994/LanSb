package com.suntrans.lanzhouwh.fragment.HomePage;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import com.suntrans.lanzhouwh.BuildConfig;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.MainActivity;
import com.suntrans.lanzhouwh.activity.base.BasedFragment;
import android.content.Context;

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
    private TextView guangwang;

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
        leftIcon = (LinearLayout) view.findViewById(R.id.left_icon);
        leftImage = (ImageView) view.findViewById(R.id.left_image);
        titleName.setText("关于");
        leftIcon.setOnClickListener(this);
        toolbarRight.setOnClickListener(this);
        guangwang = (TextView) view.findViewById(R.id.guangwang);   guangwang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://www.suntrans.net/");
                intent.setData(content_url);
                startActivity(intent);
            }
        });

        TextView textView = (TextView) view.findViewById(R.id.version);
        textView .setText("版本号:"+BuildConfig.VERSION_NAME);
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
                Uri uri = Uri.parse("https://www.pgyer.com/WprW");
                share(uri,"分享",getActivity());
                break;
        }
    }


    public  void share(Uri uri, String desc, Context context)
    {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"");
        shareIntent.putExtra(Intent.EXTRA_TEXT,  "下载地址:https://www.pgyer.com/DiiB");
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, desc));
    }
}
