package com.suntrans.lansb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.suntrans.lansb.activity.PersonalActivity;
import com.suntrans.lansb.fragment.Comapny_Fragment;
import com.suntrans.lansb.fragment.Main_fragment;
import com.suntrans.lansb.utils.SharedPreferrenceHelper;
import com.suntrans.lansb.utils.StatusBarCompat;
import com.suntrans.lansb.utils.UiUtils;
import com.suntrans.lansb.views.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    @InjectView(R.id.content)
    FrameLayout content;
    @InjectView(R.id.nav_view)
    NavigationView navView;
    @InjectView(R.id.drawer_layout)
     DrawerLayout drawerLayout;


    @InjectView(R.id.toolbar)
    LinearLayout toolbar;
    @InjectView(R.id.left_icon)
    LinearLayout leftIcon;
    @InjectView(R.id.left_image)
    ImageView leftImage;



    private int theme = 0;
    private static final String TAG = "MainActivity";
    private Fragment[] fragments;
    private int currentIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        initFragments();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme",theme);
    }

    private void setupNavagationView() {
        View view = navView.inflateHeaderView(R.layout.nav_header_main);
        CircleImageView nav = (CircleImageView) view.findViewById(R.id.nav_head_avatar);
        nav.setOnClickListener(this);
        leftIcon.setOnClickListener(this);
        navView.setNavigationItemSelectedListener(this);

    }

    private void initFragments() {
        Main_fragment main_fragment = Main_fragment.newInstance();
        Comapny_Fragment comapnyFragment =Comapny_Fragment.newInstance();
        fragments = new Fragment[]{
                main_fragment,
                comapnyFragment
        };
        getSupportFragmentManager().beginTransaction().replace(R.id.content,main_fragment).commit();
    }
   @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_icon:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.nav_head_avatar:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PersonalActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                changFragment(0);
                break;
            case R.id.nav_per:
                changFragment(1);
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
        drawerLayout.closeDrawers();
        return true;
    }

    private void changFragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[currentIndex]);
        if (!fragments[index].isAdded()){
            transaction.add(R.id.content,fragments[index]);
        }
        transaction.show(fragments[index]).commit();
        currentIndex=index;
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

    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(theme==UiUtils.getAppTheme(this)){
        }else{
            reload();
        }
    }
}
