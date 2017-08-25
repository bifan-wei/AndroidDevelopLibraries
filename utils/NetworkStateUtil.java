package com.lancoo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStateUtil {
	public static final  int NETWORK_STATE_WIFE = 01;
	public static final  int NETWORK_STATE_MOBILE = 02;
	public static final  int NETWORK_STATE_NOT_INTERNET = 03;

	/**
	 * 获取手机的网络连接状态
	 */
	public static int getNetworkState(Context context0) {
		Context context = context0;
		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conMan.getActiveNetworkInfo();

		if (info != null && info.isConnected() && info.isAvailable()) {
			String type = info.getTypeName();
			if (type.equalsIgnoreCase("wifi"))
				return NETWORK_STATE_WIFE;
			else if (type.equalsIgnoreCase("mobile"))
				return NETWORK_STATE_MOBILE;
		}
		return NETWORK_STATE_NOT_INTERNET;
	}

	
	public static Boolean HasNet(Context context0){
		return getNetworkState(context0)!=NETWORK_STATE_NOT_INTERNET;
	}
	
}
