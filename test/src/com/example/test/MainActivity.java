package com.example.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.test.receiver.ReceiverSms;
import com.example.test.regular.Regular;
import com.idea.function.RootFunction;
import com.so.idea.DeviceInfo;
import com.test.fun.TestSdcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;

import android.content.pm.PackageManager;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import dalvik.system.DexClassLoader;

public class MainActivity extends Activity 
	implements View.OnClickListener {

	private final String TAG = "test";
	
	private Button buttonTest1=null , buttonTest2=null, buttonRoot=null;
	private TextView textView = null;
	
	private LoadClass testLoadClass = null;
	
	private final String[] staticString = new String[]{"11111","22222222"};
	
	private static Handler handler = null;
	
	ReceiverSms myReceiverSms = new ReceiverSms();
	private Context context = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		buttonTest1 = (Button)findViewById(R.id.buttonLoad1);
		buttonTest1.setOnClickListener(this);
		
		buttonTest2 = (Button)findViewById(R.id.buttonLoad2);
		buttonTest2.setOnClickListener(this);
		
		textView = (TextView)findViewById(R.id.textView);
		
		buttonRoot = (Button)findViewById(R.id.buttonRoot);
		buttonRoot.setOnClickListener(this);
		initHandler();
		
		context = this;
		Message msg= new Message();
		msg.what = 1;
		handler.sendMessageDelayed(msg, 1000*10);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
	static {
		System.loadLibrary("getDeviceInfo");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
			case R.id.buttonLoad1:
				
/*			{
				TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
				if(telephonyManager == null){
					textView.setText("telephonyManager is null");
				}else{
					String NativePhoneNumber=null;  
			        NativePhoneNumber=telephonyManager.getLine1Number();
			        if (TextUtils.isEmpty(NativePhoneNumber)) {
						textView.setText("read phone number is null");
					}else{
						textView.setText(NativePhoneNumber);
					}
				}
			}*/
				
			{
				//通过SO读取IMSI
				String imsiString = DeviceInfo.getImsiJni(this);
				if( TextUtils.isEmpty( imsiString)){
					imsiString = "get imsi failed";
				}
				textView.setText(imsiString);
			}
				break;
			case R.id.buttonLoad2:

				break;
			case R.id.buttonRoot:

				break;
			default:
				break;
		}
		
	}
	
	
	private void initHandler()
	{
		if(handler != null)
		{
			return ;
		}
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
				{
					Bundle bundle = msg.getData();
					String info = (String)textView.getText();
					info = info + bundle.getString("info");
					info = info + "\n";
					textView.setText(info);
				}
					break;
				case 1://读取屏幕状态  关闭、打开
				{
					try{
						PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
						if(pm.isScreenOn())
						{
							textView.setText("screen is on");
						}else{
							textView.setText("screen is off");
						}
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
					break;
				default:
					break;
				}
				
			}
			
		};
	}
	
	private class LoadClass
	{
		DexClassLoader classLoader = null;
		Intent intentService = null;
		Class classTest = null;
		Constructor constructor = null;
		Object instance = null;
		Method getString = null;
		public void loadFile(String fileName )
		{
			String path = getFilesDir().getAbsolutePath();
			String fullFileName = path +"/"+fileName;
			classLoader = new DexClassLoader(fullFileName , path , null , getClassLoader());
			try{
				classTest =  classLoader.loadClass("com.test.loadclass.LoadedClass");
				constructor = classTest.getConstructor(new Class[]{});
				instance = constructor.newInstance(new Object[]{});
				
				getString = classTest.getMethod("getStaticString", new Class[]{});
				getString.setAccessible(true);
				
				
			}catch(Exception e)
			{
				e.printStackTrace();
				textView.setText(e.toString());
			}
		}
		
		public String getString()
		{
			try {
				return (String) getString.invoke(instance, new Object[]{});
			} catch(Exception e)
			{
				e.printStackTrace();
				textView.setText(e.toString());
			}
			return "";
		}
		
	}
	
	//改变实参的值
	public void changeVal(Integer a)
	{
		a = 10;
	}
	
	
	public static synchronized int writeLog(String msg)
	{
		int mRet = 0;
		if (msg == null) {
			return -1;
		}
		try {
			File dirStorage = Environment.getExternalStorageDirectory(); 
			if(dirStorage == null)
			{
				return -1;
			}
			String logPath = dirStorage + "/" + "log.txt";
			File file = new File(logPath);
			Date date = new Date(System.currentTimeMillis());
			msg = date.toGMTString() + msg;
			if (!file.exists())
				file.createNewFile();
			else
				msg = "\t\n\t\n" + msg;
			FileOutputStream fos=new FileOutputStream(file, true);
	        OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8");
	        BufferedWriter  bw=new BufferedWriter(osw);
	        bw.write(msg);
	        bw.close();
	        osw.close();
	        fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			mRet = -1;
		}		
		return mRet ;
	}
	
}
