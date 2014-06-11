/**
 * 
 */
package com.upload.aliyun;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.upload.aliyun.util.FileDoUtil;

/**
 * @author Administrator
 *
 */
public class MusicConstants {
	// 阿里云信息
	public static final String ALIYUN_ACCESSKEYID = "ndm6c0zcwyz1x6n5hqe66rig";
	public static final String ALIYUN_ACCESSKEYSECRET = "uDbjGwyWTrXwzGjDZUoMdRUq9cE=";
	public static final String ALIYUN_IMAGE_HOST = "oss-cn-hangzhou.aliyuncs.com";
	//
	public static String BUKET_NAME = "";
	public static final String PROTOCOL = "http://";
	public static String SERVER_PATH_ROOT = "";
	//
	public static String BASE_FILE_PATH = "";
	
	public static List<FileUploadInfoModel> UPLOAD_FILE_LIST = new ArrayList<FileUploadInfoModel>(200);
	
	//
	public static final String PATH_CONFIG_UPLOAD_PROPERTIES = "config/upload.properties";
	public static final String PATH_CONFIG_DOJSON_JS = "config/doJson.js";
	
	public static String URL_SAVE_DATA_BOOK = "";
	public static String URL_SAVE_DATA_BOOK_LIST = "";
	/**
	 * 加载配置文件
	 * @throws Exception
	 */
	public static void loadConfig() throws Exception{
		File file = FileDoUtil.findFile(PATH_CONFIG_UPLOAD_PROPERTIES);
		if(file != null){
			FileInputStream fIn = new FileInputStream(file);
			Properties p = new Properties();
			p.load(fIn);
			BUKET_NAME = p.getProperty("upload.to.buket");
			SERVER_PATH_ROOT = p.getProperty("upload.to.root");
			BASE_FILE_PATH = p.getProperty("upload.from.file.path").replace("/", File.separator);
			URL_SAVE_DATA_BOOK = p.getProperty("do.save.data.url.book");
			URL_SAVE_DATA_BOOK_LIST = p.getProperty("do.save.data.url.booklist");
			fIn.close();
			fIn = null;
		}else{
			System.out.println("[error]the file not found......");
		}
	}
	
	public static void main(String[] args)throws Exception {
		loadConfig();
		System.out.println(BUKET_NAME);
		System.out.println(BASE_FILE_PATH);
	}
	
	public final static Map<String,String> contentTypeMap = new HashMap<String,String>() {
		private static final long serialVersionUID = 1839149028666833299L;
		{
			put("stream", "application/octet-stream");
			put("mp3", "audio/mpeg");
			put("jpeg", "image/jpeg");
			put("jpg", "application/x-jpg");
		}
	};
	
	/**
	 * 获取文件的url
	 * @param key
	 * @return
	 */
	public static String getUrl(String key){
		StringBuffer strb = new StringBuffer(100);
		strb.append(PROTOCOL);
		strb.append(BUKET_NAME);
		strb.append(".");
		strb.append( ALIYUN_IMAGE_HOST);
		if(!key.startsWith("/")){
			strb.append("/");
		}
		strb.append(key);
		return strb.toString();
	}
	
}
