package com.suntrans.lanzhouwh.activity.base;

import android.os.Bundle;

import com.suntrans.lanzhouwh.R;

import java.util.LinkedList;
import java.util.List;

import ren.solid.skinloader.base.SkinBaseActivity;

/**
 * Created by Looney on 2017/1/5.
 */

public class BaseActivity extends SkinBaseActivity {
    public final static List<BaseActivity> mlist = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        synchronized (mlist){
            mlist.add(this);
        }
    }

    @Override
    protected void onDestroy() {
        synchronized (mlist){
            mlist.remove(this);
        }
        super.onDestroy();
    }

    public void killAll(){
        List<BaseActivity> copy;
        synchronized (mlist){
            copy = new LinkedList<BaseActivity>(mlist);
            for (BaseActivity a :
                    copy) {
                a.finish();
            }
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
