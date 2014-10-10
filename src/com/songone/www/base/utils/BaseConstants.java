/**
 * 
 */
package com.songone.www.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Jelly.Liu
 *
 */
public final class BaseConstants {
	private static final Logger logger = LogManager.getLogger(BaseConstants.class);
	public static final int SAVE_RADIOAUDIO_DATAS_PAGESIZE = 100;
	public static final int QUERY_DATAS_PAGESIZE = 100;
	public static final String SONG_ONE_API_RETUEN_STATUS_SUCCESS = "\"status\":\"1000\"";
	// 阿里云信息
	public static final String ALIYUN_ACCESSKEYID = "ndm6c0zcwyz1x6n5hqe66rig";
	public static final String ALIYUN_ACCESSKEYSECRET = "uDbjGwyWTrXwzGjDZUoMdRUq9cE=";
	public static final String ALIYUN_IMAGE_HOST = "oss-cn-hangzhou.aliyuncs.com";
	// //  协议
	public static final String PROTOCOL = "http://";
	//// 阿里云文件上传content type集合
	public final static Map<String,String> contentTypeMap = new HashMap<String,String>() {
		private static final long serialVersionUID = 1839149028666833299L;
		{
			put("stream", "application/octet-stream");
			put("mp3", "audio/mpeg");
			put("jpeg", "image/jpeg");
			put("jpg", "application/x-jpg");
			put("png", "image/x-png");
		}
	};
	//// 音乐1号服务器
	public static String SONG_ONE_SERVER_HOST = "localhost:8080/song1";
	
	///// 阿里云bucket
	public static String ALIYUN_BUCKET = "testupload2";
	
	public static Properties BASE_CONSTANTS_PROPERTIES = null;
	
	/**
	 * 初始化配置文件的数据
	 */
	public static void init(){
		Properties p = loadProperties();
		if(p == null){
			return ;
		}
		BASE_CONSTANTS_PROPERTIES = p;
		String songOneServerHost = p.getProperty("song1.server.host");
		if(!StringUtil.isEmptyString(songOneServerHost)){
			SONG_ONE_SERVER_HOST = songOneServerHost;
		}
		String aliyunBucket = p.getProperty("aliyun.bucket");
		if(!StringUtil.isEmptyString(aliyunBucket)){
			ALIYUN_BUCKET = aliyunBucket;
		}
	}
	
	/**
	 * 获取constants.properties 文件的键值对
	 * @param key
	 * @return
	 */
	public static String getConstantsPropertyValue(String key){
		if(StringUtil.isEmptyString(key)){
			return null;
		}
		if(BASE_CONSTANTS_PROPERTIES == null){
			return null;
		}
		return BASE_CONSTANTS_PROPERTIES.getProperty(key);
	}
	
	/**
	 * 加载数据库连接配置
	 * 
	 * @return
	 */
	private static Properties loadProperties() {
		Properties p = new Properties();
		FileInputStream fileInputStream = null;
		File file = FileDoUtil.findFile("constants.properties");
		if (file != null && file.exists()) {
			try {
				fileInputStream = new FileInputStream(file);
				p.load(fileInputStream);
				fileInputStream.close();
				return p;
			} catch (Exception e) {
				fileInputStream = null;
				logger.error(e.getMessage(), e);
			}
		} else {
			logger.error(" 找不到数据库连接配置文件 ");
		}
		return null;
	}

}
