package com.suntrans.lanzhouwh.fragment.Innerroom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Looney on 2017/1/7.
 */

public class RuleFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("公司制度如下:");
        return textView;

    }

    public static RuleFragment newInstance(){
        return new RuleFragment();
    }
}
