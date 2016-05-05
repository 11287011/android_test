package com.so.idea;

import android.content.Context;

/** 
* 类说明 
*
* 
* @version 创建时间：2016年5月4日 下午6:30:59 
*/
public class DeviceInfo {

	private static native String getImsi(Object context);
	

	
	public static String getImsiJni(Context context){
		return getImsi(context);
	}
	
}
