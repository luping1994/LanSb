package com.suntrans.lansb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.suntrans.lansb.fragment.HardwareFragment;
import com.suntrans.lansb.fragment.InnerroomFragment;
import com.suntrans.lansb.fragment.Main_fragment;
import com.suntrans.lansb.fragment.WalletFragment;
import com.suntrans.lansb.utils.SharedPreferrenceHelper;
import com.suntrans.lansb.utils.StatusBarCompat;
import com.suntrans.lansb.utils.UiUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, OnTabSelectListener {
    @InjectView(R.id.content)
    FrameLayout content;
    @InjectView(R.id.nav_view)
    NavigationView navView;
    @InjectView(R.id.drawer_layout)
     DrawerLayout drawerLayout;
    @InjectView(R.id.title_name)
    public TextView titleName;

    @InjectView(R.id.toolbar)
    LinearLayout toolbar;
    @InjectView(R.id.left_icon)
    LinearLayout leftIcon;
    @InjectView(R.id.left_image)
    ImageView leftImage;

    private BottomBar bottomBar;
    private HardwareFragment hardwareFragment;
    private InnerroomFragment innerroomFragment;
    private WalletFragment walletFragment;
    private LinearLayout toolbarRight;
    private Main_fragment main_fragment;
    private int theme = 0;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreat");
        if(savedInstanceState==null){
            theme= UiUtils.getAppTheme(this);
        }else{
            theme=savedInstanceState.getInt("theme");
        }
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarCompat.compat(this, Color.TRANSPARENT);
        ButterKnife.inject(this);
        setupNavagationView();
        initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme",theme);
    }

    private void initView() {
        int type = getIntent().getIntExtra("type",0);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        toolbarRight = (LinearLayout) findViewById(R.id.layout_right);
        bottomBar.setOnTabSelectListener(this);
        titleName.setText("硬件管理");
    }

    private void setupNavagationView() {
        View view = navView.inflateHeaderView(R.layout.nav_header_main);
        view.setOnClickListener(this);
        leftIcon.setOnClickListener(this);
        titleName.setText(getResources().getString(R.string.app_name));
        navView.setNavigationItemSelectedListener(this);
        main_fragment = new Main_fragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.content,main_fragment).commit();
    }


   @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_icon:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_per:
                break;
            case R.id.nav_money:
                break;
            case R.id.nav_about:
                break;
            case R.id.nav_theme:
                if (SharedPreferrenceHelper.gettheme(this).equals("1")){
                    SharedPreferrenceHelper.settheme(this,"1");
                }else {
                    SharedPreferrenceHelper.settheme(this,"2");
                }
                UiUtils.switchAppTheme(this);
                reload();
                break;
        }
        return true;
    }


    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId){
            case R.id.tab_one:
                titleName.setText("硬件管理");
                if (hardwareFragment==null){
                    hardwareFragment = new HardwareFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.content,hardwareFragment).commit();
                break;
            case R.id.tab_two:
                titleName.setText("室内环境");
                if (innerroomFragment==null){
                    innerroomFragment = new InnerroomFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.content,innerroomFragment).commit();
                break;
            case R.id.tab_three:
                titleName.setText("我的钱包");
                if (walletFragment==null){
                    walletFragment = new WalletFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.content,walletFragment).commit();
                break;
        }
    }


    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);//不设置进入退出动画
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG,"onRestart");

    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause");
    }

    @Override
    protected void onResume() {
        Log.i(TAG,"onResume");
        super.onResume();
        if(theme==UiUtils.getAppTheme(this)){
        }else{
            reload();
        }
    }
}
