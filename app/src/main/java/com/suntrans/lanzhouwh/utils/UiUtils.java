package com.suntrans.lanzhouwh.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;

import java.util.HashMap;
import java.util.Map;


public class UiUtils {

	private static Toast mToast;

	public static void showToast(String str) {
		if (mToast == null) {
			mToast = Toast.makeText(App.getApplication(), str, Toast.LENGTH_SHORT);
		}
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 获取到字符数组
	 *
	 * @param tabNames 字符数组的id
	 */
	public static String[] getStringArray(int tabNames) {
		return getResource1().getStringArray(tabNames);
	}

	public static Resources getResource1() {
		return App.getApplication().getResources();
	}

	/**
	 * dip转换px
	 */
	public static int dip2px(int dip) {
		final float scale = getResource1().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/**
	 * dip转换px
	 */
	public static int dip2px(float dip,Context context) {
		final float scale =context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/**
	 * pxz转换dip
	 */

	public static int px2dip(int px) {
		final float scale = getResource1().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	public static Context getContext() {
		return App.getApplication();
	}

	//	public static void runOnUiThread(Runnable runnable) {
//		// 在主线程运行
//		if(android.os.Process.myTid()==App.getMainTid()){
//			runnable.run();
//		}else{
//			//获取handler
//			App.getHandler().post(runnable);
//		}
//	}
	public static void runOnUiThread(Runnable runnable) {
		if (android.os.Process.myTid() == App.getMainTid()) {
			runnable.run();
		} else {
			App.getHandler().post(runnable);
		}
	}

	/**
	 * 加载view
	 *
	 * @param layoutId
	 * @return
	 */
	public static View inflate(int layoutId) {
		return View.inflate(getContext(), layoutId, null);
	}

	public static int getDimens(int homePictureHeight) {
		return (int) getResource1().getDimension(homePictureHeight);
	}

	/**
	 * 检查网络是否可用
	 */
	public static boolean isNetworkAvailable() {
		Context context = UiUtils.getContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
//					System.out.println(i + "===状态===" + networkInfo[i].getState());
//					System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static int getDisPlayWidth() {
		WindowManager wm = (WindowManager) UiUtils.getContext().getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}
//
	/**
	 * 将十六进制的字符串转化为十进制的数值
	 */
	public static long HexToDec(String hexStr) {
		Map<String, Integer> hexMap = prepareDate(); // 先准备对应关系数据
		int length = hexStr.length();
		long result = 0L; // 保存最终的结果
		for (int i = 0; i < length; i++) {
			result += hexMap.get(hexStr.subSequence(i, i + 1)) * Math.pow(16, length - 1 - i);
		}
//        System.out.println("hexStr=" + hexStr + ",result=" + result);
		return result;
	}

	/**
	 * 准备十六进制字符对应关系。如("1",1)...("A",10),("B",11)
	 */
	private static HashMap<String, Integer> prepareDate() {
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		for (int i = 0; i <= 9; i++) {
			hashMap.put(i + "", i);
		}
		hashMap.put("a", 10);
		hashMap.put("b", 11);
		hashMap.put("c", 12);
		hashMap.put("d", 13);
		hashMap.put("e", 14);
		hashMap.put("f", 15);
		return hashMap;
	}


	public static int getAppTheme(Context context){
		String value = SharedPreferrenceHelper.gettheme(context);
		switch (Integer.valueOf(value)){
			case 1:
				return R.style.AppTheme_NoActionBar;
			case 2:
				return R.style.AppTheme_NoActionBar_green;
			default:
				return R.style.AppTheme_NoActionBar;
		}
	}

	public static void switchAppTheme(Context context){
		String value = SharedPreferrenceHelper.gettheme(context);
		switch (Integer.valueOf(value)){
			case 1:
				SharedPreferrenceHelper.settheme(context,"2");
				break;
			case 2:
				SharedPreferrenceHelper.settheme(context,"1");
				break;
			default:
				SharedPreferrenceHelper.settheme(context,"1");
				break;
		}
	}

	public static int getColor(Context context, int color) {
		TypedValue tv = new TypedValue();
		context.getTheme().resolveAttribute(color, tv, true);
		return tv.data;
	}

	public static boolean isVaild(String value){
		if (value != null) {
			value = value.replace(" ", "");
			if (!TextUtils.equals("", value))
				return true;
		}
		return false;
	}
}
