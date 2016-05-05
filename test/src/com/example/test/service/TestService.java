package com.example.test.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/** 
* @author gaowei: 
* @version ����ʱ�䣺2015��10��28�� ����9:53:32 
* ��˵�� 
* ���Բ��ԣ��������APK������Щ������ͬ�����֣��Ƿ���Ա�����������
*/
public class TestService extends Service{

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		Log.d(getClass().getName() , "source class onCreate");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.d(getClass().getName() , "source class onStart");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d(getClass().getName() , "source class onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(getClass().getName() , "source class onDestroy");
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		Log.d(getClass().getName() , "source class onLowMemory");
	}

	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
		Log.d(getClass().getName() , "source class onTrimMemory");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(getClass().getName() , "source class intent");
		return null;
	}

}
