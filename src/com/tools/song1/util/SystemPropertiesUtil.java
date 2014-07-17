/**
 * 
 */
package com.tools.song1.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * @author Administrator
 *
 */
public class SystemPropertiesUtil {

	private static Properties properties = new Properties();
	public static final String PATH_CONFIG_UPLOAD_PROPERTIES = "config/system.properties";

	/**
	 * 初始化加载系统配置
	 */
	public static void init() {
		FileInputStream fIn = null;
		try {
			File file = FileDoUtil.findFile(PATH_CONFIG_UPLOAD_PROPERTIES);
			if (file != null && file.exists()) {
				fIn = new FileInputStream(file);
				properties.load(fIn);
				fIn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			FileDoUtil.outLog("加载文件流发生异常.............\r\n"+e.getMessage());
		} finally {
			try {
				if (fIn != null) {
					fIn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				FileDoUtil.outLog("关闭加载文件流发生异常.............\r\n"+e.getMessage());
			} finally {
				fIn = null;
			}
		}
	}
	
	public static String getProperty(String key){
		if(StringUtil.isEmptyString(key)){
			return "";
		}
		String str = properties.getProperty(key);
		if(StringUtil.isEmptyString(str)){
			str = "";
		}
		return str;
	}
	
	public static void writeProperties(){
		FileOutputStream fOut = null;
		try {
			File file = FileDoUtil.findFile(PATH_CONFIG_UPLOAD_PROPERTIES);
			if (file != null && file.exists()) {
				fOut = new FileOutputStream(file);
				properties.store(fOut, "system config properties");
				fOut.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			FileDoUtil.outLog("加载文件流发生异常.............\r\n"+e.getMessage());
		} finally {
			try {
				if (fOut != null) {
					fOut.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				FileDoUtil.outLog("关闭加载文件流发生异常.............\r\n"+e.getMessage());
			} finally {
				fOut = null;
			}
		}
	}
	
	public static void addProperty(String key,String value){
		if(StringUtil.isEmptyString(key)){
			return;
		}else if(StringUtil.isEmptyString(value)){
			return;
		}
		String str = properties.getProperty(key);
		if(!StringUtil.isEmptyString(str)){
			FileDoUtil.outLog("system properties 文件的配置发生覆盖:key="+key+",原始值为:"+str+",覆盖之后值为:"+value);
		}
		properties.setProperty(key, value);
	}

	// //// test
	public static void main(String[] args) {

	}

}
