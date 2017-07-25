package com.suntrans.lanzhouwh.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;
import com.suntrans.lanzhouwh.bean.Search;
import com.suntrans.lanzhouwh.fragment.Main.HardwareFragment;
import com.suntrans.lanzhouwh.fragment.Main.SearchFragment;
import com.suntrans.lanzhouwh.utils.RxBus;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends BasedActivity {

    private SearchView mSearchView;
    private SearchHistoryTable mHistoryDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSearchView = (SearchView) findViewById(R.id.searchView);

        SearchFragment fragment = SearchFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setSearchView();
        mSearchView.setArrowOnly(true);
        // mSearchView.setTextOnly(R.string.search);
        mSearchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
            @Override
            public void onMenuClick() {
                finish();
            }
        });
        customSearchView();
        mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
        // mSearchView.setShouldClearOnOpen(true);
    }

    private void customSearchView() {
        mSearchView.setVersion(SearchView.VERSION_TOOLBAR);
        mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
        mSearchView.setTheme(SearchView.THEME_LIGHT, true);
        mSearchView.setQuery(getIntent().getStringExtra("key"), false);
    }

    private void setSearchView() {
        mHistoryDatabase = new SearchHistoryTable(this);

        mSearchView = (SearchView) findViewById(R.id.searchView);
        if (mSearchView != null) {
            mSearchView.setHint(R.string.search);
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getData(query);
                    mSearchView.close(false);
                    return true;
                }


                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public boolean onOpen() {

                    return true;
                }

                @Override
                public boolean onClose() {

                    return true;
                }
            });
            mSearchView.setVoice(false);
        }
        List<SearchItem> suggestionsList = new ArrayList<>();

        SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
        searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                String query = textView.getText().toString();
                getData(query);
                mSearchView.close(false);
            }
        });
        mSearchView.setAdapter(searchAdapter);
        getData(getIntent().getStringExtra("key"));

    }

    @Override
    public void onBackPressed() {
        finish();// NAV UTILS
    }

    @CallSuper
    private void getData(String query) {
        mHistoryDatabase.addItem(new SearchItem(query));
        Search search = new Search(query);
        search.did = getIntent().getStringExtra("did");
        RxBus.getInstance().post(search);
    }

    @Override
    public void finish() {
        super.finish();
       overridePendingTransition(0,0);
    }
}