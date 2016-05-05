package com.example.test.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.util.Log;
import android.widget.Toast;

/** 
* 类说明 
*
* @author gaowei: 
* @version 创建时间：2015年11月24日 下午2:43:32 
* 
*/
public class ReceiverSms extends BroadcastReceiver{
	private ReceiverSms instance = null;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		abortBroadcast();
		Log.d("gaowei", "receive broadcast");
		
		if(intent != null)
		{
			if(intent.getAction().equals("android.intent.action.MOMS_SMS_RECEIVED"))
			{
				Toast.makeText(context, "receive broadcast MOMS_SMS_RECEIVED", Toast.LENGTH_SHORT).show();
				return ;
			}
		}	
			Toast.makeText(context, "receive broadcast", Toast.LENGTH_SHORT).show();
				
	}
	
	public ReceiverSms getInstance()
	{
		if (instance == null) {
			instance = new ReceiverSms();
		}
		return instance;
	}
	

}
