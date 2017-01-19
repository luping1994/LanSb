package com.suntrans.lanzhouwh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;
import com.suntrans.lanzhouwh.fragment.HomePage.About_Fragment;
import com.suntrans.lanzhouwh.fragment.HomePage.Comapny_Fragment;
import com.suntrans.lanzhouwh.fragment.HomePage.Main_fragment;
import com.suntrans.lanzhouwh.fragment.HomePage.Main_fragment_staff;
import com.suntrans.lanzhouwh.fragment.HomePage.Wallet_Fragment;
import com.suntrans.lanzhouwh.fragment.theme.ChangeSkinFragmentTheme;
import com.suntrans.lanzhouwh.utils.UiUtils;
import com.suntrans.lanzhouwh.views.CircleImageView;
import com.tencent.bugly.Bugly;

public class MainActivity extends BasedActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    FrameLayout content;
    NavigationView navView;
    public DrawerLayout drawerLayout;

    private static final String TAG = "MainActivity";
    private Fragment[] fragments;
    private int currentIndex = 0;
    private static String rusergid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bugly.init(getApplicationContext(), "678ba11223", false);
        initView();
        setupNavagationView();
        initFragments();
    }

    private void initView() {
        rusergid = getIntent().getStringExtra("rusergid");
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
        if (rusergid.equals("1")) {
            navView.inflateMenu(R.menu.menu_nav);
        } else {
            navView.inflateMenu(R.menu.menu_nav_staff);
        }
        CircleImageView nav = (CircleImageView) view.findViewById(R.id.nav_head_avatar);
        nav.setOnClickListener(this);
        navView.setNavigationItemSelectedListener(this);
//        dynamicAddSkinEnableView(to, "background", R.color.colorPrimary);
        dynamicAddSkinEnableView(navView.getHeaderView(0), "background", R.color.colorPrimary);
        dynamicAddSkinEnableView(navView, "navigationViewMenu", R.color.colorPrimary);

    }

    private void initFragments() {
        if (rusergid.equals("1")) {
            Main_fragment main_fragment = Main_fragment.newInstance();
            Comapny_Fragment comapnyFragment = Comapny_Fragment.newInstance();
            Wallet_Fragment wallet_fragment = Wallet_Fragment.newInstance();
            About_Fragment about_fragment = About_Fragment.newInstance();
            ChangeSkinFragmentTheme fragment = new ChangeSkinFragmentTheme();
            fragments = new Fragment[]{
                    main_fragment,
                    comapnyFragment,
                    wallet_fragment,
                    about_fragment,
                    fragment
            };
            getSupportFragmentManager().beginTransaction().replace(R.id.content, main_fragment).commit();
        } else if (rusergid.equals("2")) {
            Main_fragment_staff main_fragment = Main_fragment_staff.newInstance();
            Comapny_Fragment comapnyFragment = Comapny_Fragment.newInstance();
            Wallet_Fragment wallet_fragment = Wallet_Fragment.newInstance();
            About_Fragment about_fragment = About_Fragment.newInstance();
            ChangeSkinFragmentTheme fragment = new ChangeSkinFragmentTheme();
            fragments = new Fragment[]{
                    main_fragment,
                    comapnyFragment,
                    wallet_fragment,
                    about_fragment,
                    fragment
            };
            getSupportFragmentManager().beginTransaction().replace(R.id.content, main_fragment).commit();
        }

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
            case R.id.nav_per:
                changFragment(1, R.id.nav_per);
                break;
            case R.id.nav_money:
                changFragment(2, R.id.nav_money);
                break;
            case R.id.nav_about:
                changFragment(3, R.id.nav_about);
                break;
            case R.id.nav_theme:
                changFragment(4, R.id.nav_theme);
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }

    private void changFragment(int index, int menuId) {
        navView.setCheckedItem(menuId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[currentIndex]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.content, fragments[index]);
        }
        transaction.show(fragments[index]).commit();
        currentIndex = index;
    }


    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onresume");
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
//                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                UiUtils.showToast("再按一次退出");
            }
            return true;
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
//            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
