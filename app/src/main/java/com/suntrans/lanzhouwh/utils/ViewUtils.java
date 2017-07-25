package com.suntrans.lanzhouwh.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class ViewUtils {
	public static void removeParent(View v){
		ViewParent parent = v.getParent();
		if(parent instanceof ViewGroup){
			ViewGroup group=(ViewGroup) parent;
			group.removeView(v);
		}
	}
}
