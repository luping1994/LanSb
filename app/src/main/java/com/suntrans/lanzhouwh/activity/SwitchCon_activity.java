package com.suntrans.lanzhouwh.activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;
import com.suntrans.lanzhouwh.adapter.FragmentAdapter;
import com.suntrans.lanzhouwh.fragment.Main.HardwareFragment;
import com.suntrans.lanzhouwh.views.IViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Looney on 2017/3/9.
 */

public class SwitchCon_activity extends BasedActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private SearchView searchView;
    private SearchHistoryTable mHistoryDatabase;
    private HardwareFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_con);
        ButterKnife.bind(this);
        setUpToolBar();
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        dynamicAddSkinEnableView(appBarLayout, "background", R.color.colorPrimary);
    }

    @Override
    public boolean isTranslucentStatus() {
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpSearchView();
        setViewPager();
    }

    protected void setViewPager() {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment( HardwareFragment.newInstance("照明"), getString(R.string.zhaoming));
        adapter.addFragment( HardwareFragment.newInstance("插座"), getString(R.string.sockets));
        ViewPager viewPager = (IViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                System.out.println("tab is selected ,position ="+tab.getPosition());
                Animator anim = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0.0f, finalRadius);
//                    anim.setInterpolator(new AccelerateDecelerateInterpolator());
//                    anim.setDuration(1000);
//                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void setUpSearchView() {
        String floor = getIntent().getStringExtra("floor");


        mHistoryDatabase = new SearchHistoryTable(this);

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setArrowOnly(true);

        searchView.setVersion(SearchView.VERSION_TOOLBAR);
//        searchView.setVersion(SearchView.VERSION_MENU_ITEM);
        searchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
        searchView.setTheme(SearchView.THEME_LIGHT, true);
//        searchView.setGoogleIcons();
        searchView.setQuery(null, false);
        searchView.setVoice(false);
        if (searchView != null) {
            searchView.setHint("在第"+floor+"楼中搜索");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getData(query, 0);
                    searchView.close(false);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            searchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public boolean onOpen() {

                    return true;
                }

                @Override
                public boolean onClose() {

                    return true;
                }
            });
        }
        List<SearchItem> suggestionsList = new ArrayList<>();
        SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
        searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                String query = textView.getText().toString();
                getData(query, position);
                searchView.close(false);
            }
        });
        searchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
            @Override
            public void onMenuClick() {
                finish();
            }
        });
        searchView.setAdapter(searchAdapter);
    }

    public void setUpToolBar() {
        toolbar.setTitle(getIntent().getStringExtra("floor"));

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
//        fragment = HardwareFragment.newInstance();
//        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true; // false
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                searchView.open(true, item);
                return true;
            /* case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START); finish();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @CallSuper
    protected void getData(String text, int position) {
        mHistoryDatabase.addItem(new SearchItem(text));
        Intent intent = new Intent();
        intent.setClass(SwitchCon_activity.this,SearchActivity.class);
        intent.putExtra("key",text);
        intent.putExtra("did",getIntent().getStringExtra("did"));
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    @Override
    public boolean isApplyStatusBarColor() {
        return true;
    }
}
