
#include <jni.h>


/*************************
��ȡIMSI
*************************/
JNIEXPORT jstring JNICALL Java_com_so_idea_DeviceInfo_getImsi(JNIEnv *env, jclass thiz , jobject mContext)
{
	/******************************
	c++����д
	(*env).functions->FindClass
	c������д
	(*env)->FindClass
	
	**********************************/
	#if 0
	
	//��ȡcontext
	jclass cls_context=(*env).functions->FindClass(env , "android/content/Context");
	if(cls_context == 0){
		return (*env).functions->NewStringUTF(env, "");
	}
	
	//��ȡgetSystemService ����
	jmethodID getSystemService = (*env).functions->GetMethodID(env, cls_context, "getSystemService" , "(Ljava/lang/String;)Ljava/lang/Object;");
	if(getSystemService == 0){
		(*env).functions->DeleteLocalRef(env, cls_context);
		return (*env).functions->NewStringUTF(env, "");
	}
	
	//��ȡ����ID  TELEPHONY_SERVICE
	jfieldID TELEPHONY_SERVICE = (*env).functions->GetStaticFieldID(env, cls_context, "TELEPHONY_SERVICE", "Ljava/lang/String;");  
    if(TELEPHONY_SERVICE == 0){  
    		(*env).functions->DeleteLocalRef(env, cls_context);
		return (*env).functions->NewStringUTF(env, "");
    }

	jstring str =(jstring) (*env).functions->GetStaticObjectField(env, cls_context, TELEPHONY_SERVICE);

	jobject telephonymanager = (*env).functions->CallObjectMethod(env, cls_context, getSystemService, str); 
	if(telephonymanager == 0)
	{
		(*env).functions->DeleteLocalRef(env, cls_context);
		return (*env).functions->NewStringUTF(env, "");
	}		
	
	jclass cls_tm = (*env).functions->FindClass(env, "android/telephony/TelephonyManager");
	if(cls_tm == 0){
		(*env).functions->DeleteLocalRef(env, cls_context);
		(*env).functions->DeleteLocalRef(env, telephonymanager);
		return (*env).functions->NewStringUTF(env, "");		
	}
	
	jmethodID getSubscriberId = (*env).functions->GetMethodID(env, cls_tm, "getSubscriberId", "()Ljava/lang/String;");  
	if( getSubscriberId == 0){
		return (*env).functions->NewStringUTF(env, "");		
	}
	
	jstring imsi = (jstring)(*env).functions->CallObjectMethod(env, telephonymanager, getSubscriberId); 
	
	(*env).functions->DeleteLocalRef(env, cls_context);
	(*env).functions->DeleteLocalRef(env, telephonymanager);
	
	
	return imsi;
	#else
		//��ȡcontext
	jclass cls_context=(*env)->FindClass(env , "android/content/Context");
	if(cls_context == 0){
		return (*env)->NewStringUTF(env, "");
	}
	
	//��ȡgetSystemService ����
	jmethodID getSystemService = (*env)->GetMethodID(env, cls_context, "getSystemService" , "(Ljava/lang/String;)Ljava/lang/Object;");
	if(getSystemService == 0){
		(*env)->DeleteLocalRef(env, cls_context);
		return (*env)->NewStringUTF(env, "");
	}
	
	//��ȡ����ID  TELEPHONY_SERVICE
	jfieldID TELEPHONY_SERVICE = (*env)->GetStaticFieldID(env, cls_context, "TELEPHONY_SERVICE", "Ljava/lang/String;");  
    if(TELEPHONY_SERVICE == 0){  
    		(*env)->DeleteLocalRef(env, cls_context);
		return (*env)->NewStringUTF(env, "");
    }

	jstring str =(jstring) (*env)->GetStaticObjectField(env, cls_context, TELEPHONY_SERVICE);

	jobject telephonymanager = (*env)->CallObjectMethod(env, mContext, getSystemService, str); 
	if(telephonymanager == 0)
	{
		(*env)->DeleteLocalRef(env, cls_context);
		return (*env)->NewStringUTF(env, "");
	}		
	
	jclass cls_tm = (*env)->FindClass(env, "android/telephony/TelephonyManager");
	if(cls_tm == 0){
		(*env)->DeleteLocalRef(env, cls_context);
		(*env)->DeleteLocalRef(env, telephonymanager);
		return (*env)->NewStringUTF(env, "");		
	}
	
	jmethodID getSubscriberId = (*env)->GetMethodID(env, cls_tm, "getSubscriberId", "()Ljava/lang/String;");  
	if( getSubscriberId == 0){
		return (*env)->NewStringUTF(env, "");		
	}
	
	jstring imsi = (jstring)(*env)->CallObjectMethod(env, telephonymanager, getSubscriberId); 
	
	(*env)->DeleteLocalRef(env, cls_context);
	(*env)->DeleteLocalRef(env, telephonymanager);
	
	
	return imsi;
	#endif
}