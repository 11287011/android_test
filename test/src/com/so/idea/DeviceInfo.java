package com.so.idea;

import android.content.Context;

/** 
* ��˵�� 
*
* 
* @version ����ʱ�䣺2016��5��4�� ����6:30:59 
*/
public class DeviceInfo {

	private static native String getImsi(Object context);
	

	
	public static String getImsiJni(Context context){
		return getImsi(context);
	}
	
}
