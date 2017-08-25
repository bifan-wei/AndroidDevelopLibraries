package com.hw.libraries.logger;

import android.util.Log;

public class Loger {
	
	public static void i(String tag,String msg){
		if(msg==null) msg = "";
		Log.i(tag, msg);
	}
	

	public static void e(String tag,String msg){
		if(msg==null) msg = "";
		Log.e(tag, msg);
	}
}
