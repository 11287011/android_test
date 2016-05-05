package com.test.fun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;


import android.graphics.LinearGradient;
import android.os.Environment;
import android.text.TextUtils;

/** 
* 类说明 
*
* 
* @version 创建时间：2016年4月21日 下午2:27:55 
*/
public class TestSdcard {
	
	public void test(){
		initServerList();
	};
	
	private void initServerList()
	{
		//检测服务器列表文件是否存在
		String disk = Environment.getExternalStorageDirectory().getPath();
		
		String space = File.separator;
		String poolFileName = "PoolServer.txt";
		String[] arrayList = null;
		BufferedReader br = null;
		if(TextUtils.isEmpty(disk)){
			return;
		}
		String fullPath = disk+space+poolFileName;
		File file = new File(fullPath);
		if(file.exists()){
			try {
				br = new BufferedReader(new InputStreamReader( new FileInputStream(fullPath)));
				String line = null;
				int numLine = 0;
				ArrayList<String> aList = new ArrayList<String>();
				while( (line=br.readLine())!=null){
					numLine++;
					aList.add(line);
				}
				if(numLine > 0){
					arrayList = new String[numLine];
					int i = 0;
					while(i < numLine){
						arrayList[i]=aList.get(i++);
						
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(br != null){
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	

	
}
