package com.example.test.struct;
/** 
* 类说明 
*
* @author gaowei: 
* @version 创建时间：2015年12月1日 下午5:47:59 
* 
*/
public class Struct {

	private class ClassInfo{
		class FuncInfo{
			String name;
			Class[] parament;
		}
		String name;
		FuncInfo[] listFunc;
	}
	
	String[] listClass = new String[]{"test1","test2"};
	String[][] listFunc = new String[][]{
		{"test1_func1","test1_func1"},
		{"test2_func1","test2_func1"}
	};
	Class[][][] listParament = new Class[][][]{
		{{int.class , String.class},{int.class , String.class} },
		{{int.class , String.class},{int.class , String.class} },
	};
	
	
	
	
	
}
