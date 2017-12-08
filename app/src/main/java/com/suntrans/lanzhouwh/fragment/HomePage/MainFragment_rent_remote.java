package com.suntrans.lanzhouwh.fragment.HomePage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.MainActivity;
import com.suntrans.lanzhouwh.activity.SearchActivity;
import com.suntrans.lanzhouwh.activity.base.BasedFragment;
import com.suntrans.lanzhouwh.adapter.FragmentAdapter;
import com.suntrans.lanzhouwh.fragment.Main.HardwareFragment;
import com.suntrans.lanzhouwh.utils.RxBus;
import com.suntrans.lanzhouwh.utils.StatusBarCompat;
import com.suntrans.lanzhouwh.views.IViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/5/3.
 */

public class MainFragment_rent_remote extends BasedFragment {
    public static MainFragment_rent_remote newInstance() {
        MainFragment_rent_remote rent = new MainFragment_rent_remote();
        return rent;
    }

    private SearchView searchView;
    private SearchHistoryTable mHistoryDatabase;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main_rent, container, false);
        ButterKnife.bind(this, view);
        setUpToolBar(view);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        View statusBar = view.findViewById(R.id.statusbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = StatusBarCompat.getStatusBarHeight(getContext());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) statusBar.getLayoutParams();
            params.height = statusBarHeight;
            statusBar.setLayoutParams(params);
            statusBar.setVisibility(View.VISIBLE);
        }else {
            statusBar.setVisibility(View.GONE);

        }

    }

    public void setUpToolBar(View view) {
        toolbar.setTitle("智能控制");
        toolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
        }
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar_layout);
        dynamicAddSkinView(appBarLayout, "background", R.color.colorPrimary);

        RxBus.getInstance().toObserverable(String.class)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals("hideHomeIndicator")){
                            actionBar.setDisplayHomeAsUpEnabled(false);
                            actionBar.setDisplayShowTitleEnabled(true);
                        }

                    }
                });
    }

    private void initView(View view) {
//        HardwareFragment fragment = HardwareFragment.newInstance("照明");
//        getChildFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
        setUpSearchView(view);
        setViewPager(view);
    }
    protected void setViewPager(View view) {
        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager());
        adapter.addFragment( HardwareFragment.newInstance("照明"), getString(R.string.zhaoming));
        adapter.addFragment( HardwareFragment.newInstance("插座"), getString(R.string.sockets));

        ViewPager viewPager = (IViewPager) view.findViewById(R.id.viewPager);

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

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
    private void setUpSearchView(View view) {
        mHistoryDatabase = new SearchHistoryTable(getContext());

        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setVersion(SearchView.VERSION_MENU_ITEM);
        searchView.setVersionMargins(SearchView.VERSION_MARGINS_MENU_ITEM);
        searchView.setTheme(SearchView.THEME_LIGHT, true);
        searchView.setQuery(null, false);
        searchView.setVoice(false);

        if (searchView != null) {
            searchView.setHint(R.string.search);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getData(query);
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

        SearchAdapter searchAdapter = new SearchAdapter(getActivity(), suggestionsList);
        searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                String query = textView.getText().toString();
                getData(query);
                searchView.close(false);
            }
        });
        searchView.setAdapter(searchAdapter);
    }

    private void getData(String query) {
        mHistoryDatabase.addItem(new SearchItem(query));
        Intent intent = new Intent();
        intent.setClass(getActivity(),SearchActivity.class);
        intent.putExtra("key",query);
        intent.putExtra("did","null");
        startActivity(intent);
        getActivity().overridePendingTransition(0,0);

    }
}


