package com.andian.sdk.longIntP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;
import android.provider.ContactsContract.Contacts.Data;
import android.sax.EndElementListener;


/** 
* 类说明 
*	列出被要求拦截的短信号码
*	compareStr 字符串比较规则：已字符串长度较短的
*
* @author gaowei: 
* @version 创建时间：2015年12月7日 下午9:24:22 
* 
*/
public class ListIntP {
	//定义存储数径
	private static final String fullPathFile = Environment.getExternalStorageDirectory()+"/"+"NumList.txt";
	private static final String strWildcard = "*"; //通配符字符串
	private static final int increaseSpace = 50;
	private static final String charsetName = "UTF-8";	//文件的字符编码
	private static final String breakCharset = "\n";
	class DataStruct{
		public String number = null; //存储的号码
		public long stopTime = 0; //停止时间
	}
	class DataInfo{
		public int numPN = 0; //存储了多少号码;
		public int numSpace = 0; //有多少的存储空间
		DataStruct[] arrayNum = null;	
	}
	
	private DataInfo dataNum = null; //存储与拦截号码相关的所有信息，流化存入文件 。号码是升序排列
	
	
	/**
	 *函数说明
	 *	从文件中读出列表的数据
	 *@author gaowei
	 *@version 2015年12月8日上午9:24:47
	 */
	public ListIntP() {
		// TODO Auto-generated constructor stub
		File file = new File(fullPathFile);
		dataNum = new DataInfo();
		if(file.exists())
		{
			//从文件中读取存储内容存入listIntP
			readFromFile();
		}else{
			//无文件
			
		}
		
	}
	
	/**
	 * 
	 *函数说明
	 *		判断指定的号码是否在拦截列表中，且当前时间否在拦截时间内。如果已超拦截时限，删除列表中的数据
	 *		列表中有可能存在 10086* 和 100867* 这样两个字符串，根据compareStr的比较规则，这两个字符串完全相等，所以要直到找不到有相同的字符串为止。
	 *@author gaowei
	 *@version 2015年12月8日上午10:26:44
	 * @param number	指定匹配的号码，是一个不包含通配符的全整号码
	 * @return
	 */
	public boolean isInIntP(String number)
	{
		boolean mRet = false;
		int index = 0;
		while((index = indexInList(number))>=0){
			long curTime = System.currentTimeMillis();
			if(curTime <= dataNum.arrayNum[index].stopTime)
			{
				mRet = true;
				break;
			}else{
				dataNum.numPN--;
				for(int i=index ;i<dataNum.numPN; i++)
				{
					dataNum.arrayNum[i]=dataNum.arrayNum[i+1];
				}
			}
		}
		return mRet;
	}
	/**
	 * 
	 *函数说明
	 *	向拦截列表中添加号码，加完后存入文件
	 *@author gaowei
	 *@version 2015年12月8日上午10:30:48
	 * @param arrayNum	需加添加的号码列表
	 * @param stopTime	这批号码的停止时间
	 */
	public void addIntP(String[] arrayNum , long stopTime)
	{
		boolean writeFile = false;
		for(int i=0; i<arrayNum.length; i++)
		{
			if(0 <= insertNum(arrayNum[i], stopTime)){
				writeFile = true;
			}
		}
		if(writeFile){
			//把listIntP的内容转换为流的形式，存入文件
			writeToFile();
		}
	}
	/**
	 * 
	 *函数说明
	 *	查找指定号码在列表中的位置，列表中以升序排列
	 *@author gaowei
	 *@version 2015年12月9日下午1:53:47
	 * @param number	指定号码
	 * @return			>=0	所在位置
	 * 					<0	没有找到
	 */
	private int indexInList(String number) {
		int index = -1;
		if(dataNum.numPN > 0)
		{
			int head = 0, end=dataNum.numPN-1,mid = 0;
			while(end>=head)
			{
				int retCompareHead = 0;//头部比较结果
				int retCompareEnd = 0;//尾部比较结果
				int retCompareMid = 0;//中间比较结果
				mid = (end+head)>>1;
				
				retCompareMid = compareStr(dataNum.arrayNum[mid].number,number);
				if(retCompareMid == 0)
				{
					index = mid ;
					break;
				}else{
					if(retCompareMid > 0){
						//只取前半分
						if(mid==head) //没有记录
						{
							break;
						}
						retCompareHead = compareStr(dataNum.arrayNum[head].number,number);
						if(retCompareHead ==0 )
						{
							index = head;
							break;
						}
						head++;
						end = mid-1;
					}else{
						//只取后半分
						if(mid==end) //没有记录
						{
							break;
						}						
						retCompareEnd = compareStr(dataNum.arrayNum[end].number, number); 
						if(retCompareEnd==0)
						{
							index = end;
							break;
						}
						end--;
						head=mid+1;
					}
				}
			}
		}
		return index;
	}
	/**
	 * 
	 *函数说明
	 *	把dataNum写入文件中（fullPathFile）
	 *@author gaowei
	 *@version 2015年12月9日下午1:57:48
	 * @return	<0	出错
	 * 			>=0	正常
	 */
	private int writeToFile()
	{
		int mRet = 0;
		File file = new File(fullPathFile);
		if(file.exists())
		{
			file.delete();
		}
		try {
			file.createNewFile();
			FileOutputStream fos=new FileOutputStream(file, true);
	        OutputStreamWriter osw=new OutputStreamWriter(fos, charsetName);
	        BufferedWriter  bw=new BufferedWriter(osw);			
			bw.write(""+ dataNum.numPN);
			bw.write(breakCharset);
			for(int i=0; i<dataNum.numPN; i++){
				bw.write(dataNum.arrayNum[i].number);
				bw.write(breakCharset);
				bw.write(""+dataNum.arrayNum[i].stopTime);
				bw.write(breakCharset);
			}
			bw.close();
			osw.close();
			fos.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mRet = -1;
		}
		
		
		return mRet;
	}
	/**
	 * 
	 *函数说明
	 *	从文件（fullPathFile）中读取
	 *@author gaowei
	 *@version 2015年12月9日下午1:59:48
	 * @return	<0	出错
	 * 			>=0	正常
	 */
	private int readFromFile()
	{
		int mRet = 0;
		File file = new File(fullPathFile);
		if(file.exists())
		{
			FileInputStream fileInputStream = null;
			InputStreamReader inputStreamReader =null;
			BufferedReader bufferedReader = null;
			try {
				fileInputStream = new FileInputStream(file);
				inputStreamReader = new InputStreamReader(fileInputStream);
				bufferedReader = new BufferedReader(inputStreamReader);
				String lineCon = bufferedReader.readLine();
				dataNum.numPN = Integer.parseInt(lineCon);
				dataNum.numSpace = dataNum.numPN;
				dataNum.arrayNum = new DataStruct[dataNum.numSpace];
				for(int i=0; i<dataNum.numPN ; i++){
					dataNum.arrayNum[i]=new DataStruct();
					if(dataNum.arrayNum[i] == null){
						mRet = -1;
						clear();
						break;
					}
					dataNum.arrayNum[i].number = bufferedReader.readLine();
					dataNum.arrayNum[i].stopTime = Long.parseLong(bufferedReader.readLine());
				}
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mRet=-1;
			}
			
			if(bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(inputStreamReader != null){
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fileInputStream != null){
				try {
					fileInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}else{
			mRet = -1;
		}
		return mRet;		
	}
	/**
	 * 
	 *函数说明
	 *	向dataNum中添加指定号码，以升序排列
	 *@author gaowei
	 *@version 2015年12月9日下午2:06:21
	 * @param number	号码
	 * @param stopTime	添加的号码停止拦截时间
	 * @return	>=0	插入的位置
	 * 			<0	不需要插入
	 */
	private int insertNum(String number, long stopTime)
	{
		/*
		int mRet = -1;
		int index = indexInList(number);
		if(index >= 0)
		{	//已有记录，判断上下是否有兼容的字符串
			int i = index;
			while(i>=0){
				if(number.compareTo(dataNum.arrayNum[i].number) == 0){//完全相等
					if(dataNum.arrayNum[i].stopTime < stopTime)
					{
						dataNum.arrayNum[i].stopTime = stopTime;
						mRet = index;
						insertNum(number, stopTime, index);
						break;
					}else{//还是原来的拦截时间长
						break;
					}
				}else{
					if(compareStr(dataNum.arrayNum[i].number, number) != 0) //不相等说明兼容的字符串已比较完
					{
						break;
					}else{
						
						
					}
				}
				i--;
			}
			i = index+1;
			if(mRet < 0){
				while(i<dataNum.numPN)
				{
					if(number.compareTo(dataNum.arrayNum[i].number) == 0){//完全相等
						if(dataNum.arrayNum[i].stopTime < stopTime)
						{
							dataNum.arrayNum[i].stopTime = stopTime;
							mRet = index;
							insertNum(number, stopTime, index);
							break;
						}else{//还是原来的拦截时间长
							break;
						}						
					}else{
						
					}
					i++;
				}
				
			}
			

		}else{
			//如果没有真接添加
			if(dataNum.numSpace == dataNum.numPN)
			{
				if(reMalloc()<0)//重新分配空间
				{
					return -1;
				}
			}
			int i = 0;
			for( i=dataNum.numPN; i > 0; i--)
			{
				if(compareStr(dataNum.arrayNum[i-1].number, number)>0)
				{
					dataNum.arrayNum[i]=dataNum.arrayNum[i-1];
				}else{
					dataNum.arrayNum[i].number = number;
					dataNum.arrayNum[i].stopTime = stopTime;
					dataNum.numPN++;
					mRet = i;
					break;
				}
			}
			if(i==0)	//添加的字符串最小，放在0的位置
			{
				dataNum.arrayNum[i].number = number;
				dataNum.arrayNum[i].stopTime = stopTime;
				dataNum.numPN++;
				mRet = 0;
			}			
			
		}
		*/
		int mRet = -1;
		
		//道先查找是否有完全相同的
		for(int i=0 ; i<dataNum.numPN; i++)
		{
			if(dataNum.arrayNum[i].number.compareTo(number)==0)
			{
				if(dataNum.arrayNum[i].stopTime >= stopTime)
				{
					return mRet;
				}else{
					dataNum.arrayNum[i].stopTime = stopTime;
					return i;
				}
			}
		}
		
		//需要加入号码
		if(dataNum.numSpace == dataNum.numPN)
		{
			if(reMalloc()<0)//重新分配空间
			{
				return -1;
			}
		}
		
		int i = 0;
		DataStruct item = new DataStruct();
		item.number = number;
		item.stopTime = stopTime;
		for( i=dataNum.numPN; i > 0; i--)
		{
			int compRet = dataNum.arrayNum[i-1].number.compareTo(number);
			if(compRet > 0)
			{
				dataNum.arrayNum[i]=dataNum.arrayNum[i-1];
			}else{
				dataNum.arrayNum[i]=item;
				
				mRet = i;
				break;
			}
		}
		if(i==0)	//添加的字符串最小，放在0的位置
		{
			dataNum.arrayNum[i]=item;
			mRet = 0;
		}
		dataNum.numPN++;
		
		return mRet;
	}
	/**
	 * 
	 *函数说明
	 *	向指定索引添加元素
	 *@author gaowei
	 *@version 2015年12月10日上午11:58:10
	 * @param number
	 * @param stopTime
	 * @param index
	 * @return
	 */
	private int insertNum(String number, long stopTime,int index)
	{
		if(index > dataNum.numPN)
		{
			return -1;
		}
		//如果没有真接添加
		if(dataNum.numSpace == dataNum.numPN)
		{
			if(reMalloc()<0)//重新分配空间
			{
				return -1;
			}
		}
		int i =0;
		for(i=dataNum.numPN-1;i>=index;i--){
			dataNum.arrayNum[i+1]= dataNum.arrayNum[i];
		}
		dataNum.arrayNum[index].number = number;
		dataNum.arrayNum[index].stopTime = stopTime;
		dataNum.numPN++;
		return 0;
	}
	
	/**
	 * 
	 *函数说明
	 *	字符串比较函数，支持字符串尾*号的通配符
	 *@author gaowei
	 *@version 2015年12月9日下午5:04:22
	 * @param str1	字符串1
	 * @param str2	字符串2
	 * @return	==0	相等
	 * 			>0	str1>str2
	 * 			<0	str1<str2
	 */
	public int compareStr(String str1, String str2)
	{
		int mRet = 0;
		int lenStr1 = str1.indexOf(strWildcard);
		int lenStr2 = str2.indexOf(strWildcard);
		int lenStr = 0;
		String str1Temp = null , str2Temp = null;
		if(lenStr1>=0 && lenStr2>=0){ //如果两个字符串都有通配符，取最短的字符串进行比较
			lenStr = lenStr1>lenStr2?lenStr2:lenStr1; //取长度小的做为比较字符串的长度
			str1Temp  = str1.substring(0, lenStr);
			str2Temp = str2.substring(0, lenStr);
		}else{
			if(lenStr1 >= 0) //如果str1有通配符，str2没有，lenStr1取值为本身长度，lenStr2取值为本身长度(lenStr2<lenStr1)或lenStr1(lenStr2>lenStr1)
			{
				lenStr2 = str2.length();
				lenStr2 = lenStr1>lenStr2?lenStr2:lenStr1;
				str1Temp  = str1.substring(0, lenStr1);
				str2Temp = str2.substring(0, lenStr2);				
			}else{
				if(lenStr2 >= 0) //如果str2有通配符，str1没有，lenStr2取值为str2的长度，lenStr1取值为本身长度(lenStr2>lenStr1)或lenStr2(lenStr2<lenStr1)
				{
					lenStr1 = str1.length();
					lenStr1 = lenStr1>lenStr2?lenStr2:lenStr1;
					str1Temp  = str1.substring(0, lenStr1);
					str2Temp = str2.substring(0, lenStr2);						
				}else{//都没有通配符
					str1Temp = str1;
					str2Temp = str2;
				}
			}
		}
		mRet = str1Temp.compareTo(str2Temp);
		
		return mRet;
		
	}
	/**
	 * 
	 *函数说明
	 *	给dataNum.arrayNum重新分配空间，增加increaseSpace个长度
	 *@author gaowei
	 *@version 2015年12月10日上午10:22:34
	 *@return 	>=0	正常
	 *			<0	出错
	 */
	private int reMalloc()
	{
		int mRet = 0;
		DataStruct[] tempData= dataNum.arrayNum ;
		dataNum.numSpace += increaseSpace;
		dataNum.arrayNum = new DataStruct[dataNum.numSpace];
		if(dataNum.arrayNum == null){
			mRet = -1;
			dataNum.numSpace -= increaseSpace;
			dataNum.arrayNum = tempData;
		}else{
			for(int i=0; i<dataNum.numSpace; i++)
			{
				dataNum.arrayNum[i]=new DataStruct();
			}
			if(tempData != null){
				System.arraycopy(tempData, 0, dataNum.arrayNum, 0, tempData.length);
			}
		}
		return mRet;
	}
	//为测试提供
	public String getItem(int index)
	{
		String mRet = null;
		if(index>=dataNum.numSpace)
		{
			return mRet;
		}
		mRet = dataNum.arrayNum[index].number;
		return mRet;
	}
	//为测试准备
	public void clear()
	{
		dataNum.numPN = 0;
		dataNum.numSpace = 0;
		dataNum.arrayNum = null;
	}
}
