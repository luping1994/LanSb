package com.suntrans.lanzhouwh.activity.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.utils.StatusBarCompat;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.LinkedList;
import java.util.List;

import ren.solid.skinloader.base.SkinBaseActivity;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by Looney on 2017/1/5.
 */
public class BasedActivity extends SkinBaseActivity implements LifecycleProvider<ActivityEvent> {

    public final static List<BasedActivity> mlist = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.TRANSPARENT);
        synchronized (mlist){
            mlist.add(this);
        }
        lifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    @Override
    protected void onDestroy() {
        synchronized (mlist){
            mlist.remove(this);
        }
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }

    public void killAll(){
        List<BasedActivity> copy;
        synchronized (mlist){
            copy = new LinkedList<BasedActivity>(mlist);
            for (BasedActivity a :
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



    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }


    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }


}
