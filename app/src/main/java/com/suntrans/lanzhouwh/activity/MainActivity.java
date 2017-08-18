package com.suntrans.lanzhouwh.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pgyersdk.update.PgyUpdateManager;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;
import com.suntrans.lanzhouwh.bean.userinfo.UserInfos;
import com.suntrans.lanzhouwh.fragment.HomePage.About_Fragment;
import com.suntrans.lanzhouwh.fragment.HomePage.MainFragment_pre;
import com.suntrans.lanzhouwh.fragment.HomePage.MainFragment_rent;
import com.suntrans.lanzhouwh.fragment.theme.ChangeSkinFragmentTheme;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.utils.UiUtils;
import com.suntrans.lanzhouwh.views.CircleImageView;

public class MainActivity extends BasedActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    FrameLayout content;
    NavigationView navView;
    public DrawerLayout drawerLayout;

    private static final String TAG = "MainActivity";
    private Fragment[] fragments;
    private int currentIndex = 0;
    private UserInfos infos;
    private String rusergid;
    private boolean isRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("MainActivity创建了");

        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        initView();
        setupNavagationView();
        initFragments();
        PgyUpdateManager.register(this, "com.suntrans.lanzhouwh.fileProvider");

    }

    private void initView() {
        infos = getIntent().getParcelableExtra("info");
        if (infos != null)
            rusergid = infos.getRusergid();
        content = (FrameLayout) findViewById(R.id.content);
        navView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setupNavagationView() {
        View view = navView.inflateHeaderView(R.layout.nav_header_main);
        navView.inflateMenu(R.menu.menu_nav);
        TextView name = (TextView) view.findViewById(R.id.name);
        if (infos != null)
            name.setText(infos.getNickname());
        CircleImageView nav = (CircleImageView) view.findViewById(R.id.nav_head_avatar);
        nav.setOnClickListener(this);
        navView.setNavigationItemSelectedListener(this);
//        dynamicAddSkinEnableView(to, "background", R.color.colorPrimary);
        dynamicAddSkinEnableView(navView.getHeaderView(0), "background", R.color.colorPrimary);
        dynamicAddSkinEnableView(navView, "navigationViewMenu", R.color.colorPrimary);
    }

    private void initFragments() {
        if (infos.getRusergid().equals("1")) {
            MainFragment_pre main_fragment = MainFragment_pre.newInstance();
//            Comapny_Fragment comapnyFragment = Comapny_Fragment.newInstance();
//            Wallet_Fragment wallet_fragment = Wallet_Fragment.newInstance();
            About_Fragment about_fragment = About_Fragment.newInstance();
            ChangeSkinFragmentTheme fragment = new ChangeSkinFragmentTheme();
            fragments = new Fragment[]{
                    main_fragment,
//                    comapnyFragment,
//                    wallet_fragment,
                    about_fragment,
                    fragment
            };
        } else if (infos.getRusergid().equals("2")) {
            MainFragment_rent main_fragment = MainFragment_rent.newInstance();
//            Comapny_Fragment comapnyFragment = Comapny_Fragment.newInstance();
//            Wallet_Fragment wallet_fragment = Wallet_Fragment.newInstance();
            About_Fragment about_fragment = About_Fragment.newInstance();
            ChangeSkinFragmentTheme fragment = new ChangeSkinFragmentTheme();
            fragments = new Fragment[]{
                    main_fragment,
//                    comapnyFragment,
//                    wallet_fragment,
                    about_fragment,
                    fragment
            };
        }
        changFragment(0, R.id.nav_home);

//        SlidingPaneLayout
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_head_avatar:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PersonalActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (!rusergid.equals("1") && !rusergid.equals("2")) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.nav_home:
                changFragment(0, R.id.nav_home);
                break;
            case R.id.nav_about:
                changFragment(1, R.id.nav_about);
                break;
            case R.id.nav_theme:
                changFragment(2, R.id.nav_theme);
                break;
            case R.id.nav_env:
                startActivity(new Intent(this, Env_activity.class));
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);

                break;
            case R.id.nav_ele:
                startActivity(new Intent(this, EleInfo_Activity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);


                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }

    private void changFragment(int index, int menuId) {
//        navView.setCheckedItem(menuId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[currentIndex]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.content, fragments[index]);
        }
        transaction.show(fragments[index]).commit();
        currentIndex = index;
    }


    private long[] mHits = new long[2];

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawers();
                return true;
            }
            if (currentIndex != 0) {
                changFragment(0, R.id.nav_home);
                return true;
            }
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - 2000)) {
                finish();
//                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                UiUtils.showToast("再按一次退出");
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("MainActivity销毁了");
        PgyUpdateManager.unregister();
    }

    @Override
    public boolean isApplyStatusBarColor() {
        return false;
    }


    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    //表示按了home键,程序到了后台
                    finish();
                } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                    //表示长按home键,显示最近使用的程序列表
                }
            }
        }

        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";


    };
}
