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

import com.tools.song1.util.FileDoUtil;
import com.tools.song1.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class MusicConstants {
	// 阿里云信息
	public static final String ALIYUN_ACCESSKEYID = "ndm6c0zcwyz1x6n5hqe66rig";
	public static final String ALIYUN_ACCESSKEYSECRET = "uDbjGwyWTrXwzGjDZUoMdRUq9cE=";
	public static final String ALIYUN_IMAGE_HOST = "oss-cn-hangzhou.aliyuncs.com";
	/**
	 * 要上传的bucket名称
	 */
	public static String BUKET_NAME = "";
	/**
	 * 协议
	 */
	public static final String PROTOCOL = "http://";
	/**
	 * 阿里云服务器根目录
	 */
	public static String SERVER_PATH_ROOT = "";
	/**
	 * 本地根目录
	 */
	public static String BASE_FILE_PATH = "";
	/**
	 * 需要上传的列表
	 */
	public static List<FileUploadInfoModel> UPLOAD_FILE_LIST = new ArrayList<FileUploadInfoModel>(200);
	
	//
	public static final String PATH_CONFIG_UPLOAD_PROPERTIES = "config/upload.properties";
	public static final String PATH_CONFIG_DOJSON_JS = "config/doJson.js";
	
	public static String DO_TYPE = "";
	/**
	 * 保存书集的接口url
	 */
	public static String URL_SAVE_DATA_BOOK = "";
	/**
	 * 保存书单的接口url
	 */
	public static String URL_SAVE_DATA_BOOK_LIST = "";
	/**
	 * 保存歌曲的接口url
	 */
	public static String URL_SAVE_DATA_SONG = "";
	/**
	 * 跟新歌曲的接口url
	 */
	public static String URL_UPDATE_DATA_SONG = "";
	/**
	 * 保存歌单的接口url
	 */
	public static String URL_SAVE_DATA_SONG_LIST = "";
	
	//
	public static String MUSIC_TIME_TYPE_MAPPING_FILE_PATH = "";
	
	public static Map<String,String> MUSIC_LOAD_EXCEL_CELL_MAPING = new HashMap<String, String>();
	public static Properties CONFIG_PROPERTIES = null;
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
			FileDoUtil.outLog("BUKET_NAME:"+BUKET_NAME);
			SERVER_PATH_ROOT = p.getProperty("upload.to.root");
			if (!SERVER_PATH_ROOT.endsWith("/")) {
				SERVER_PATH_ROOT = SERVER_PATH_ROOT + "/";
			}
			FileDoUtil.outLog("SERVER_PATH_ROOT:"+SERVER_PATH_ROOT);
			BASE_FILE_PATH = p.getProperty("upload.from.file.path").replace("/", File.separator);
			if (!BASE_FILE_PATH.endsWith("/")) {
				BASE_FILE_PATH = BASE_FILE_PATH + "/";
			}
			URL_SAVE_DATA_BOOK = p.getProperty("do.save.data.url.book");
			URL_SAVE_DATA_BOOK_LIST = p.getProperty("do.save.data.url.booklist");
			URL_SAVE_DATA_SONG = p.getProperty("do.save.data.url.song");
			URL_SAVE_DATA_SONG_LIST = p.getProperty("do.save.data.url.songlist");
			DO_TYPE = p.getProperty("do.save.data.type");
			FileDoUtil.outLog("需要上传的是:"+DO_TYPE);
			MUSIC_LOAD_EXCEL_CELL_MAPING.put("listname", p.getProperty("music.songlist.name"));
			MUSIC_LOAD_EXCEL_CELL_MAPING.put("enverionment", p.getProperty("music.songlist.enverionment"));
			MUSIC_LOAD_EXCEL_CELL_MAPING.put("category", p.getProperty("music.songlist.category"));
			MUSIC_LOAD_EXCEL_CELL_MAPING.put("desc", p.getProperty("music.songlist.desc"));
			MUSIC_TIME_TYPE_MAPPING_FILE_PATH = p.getProperty("music.time.type.mapping.file.path");
			CONFIG_PROPERTIES = p;
			fIn.close();
			fIn = null;
		}else{
			FileDoUtil.outLog("[error]the file not found......");
		}
	}
	
	public static String getPropertyValue(String key){
		if(StringUtil.isEmptyString(key)){
			return null;
		}else if(CONFIG_PROPERTIES == null){
			return null;
		}else{
			return CONFIG_PROPERTIES.getProperty(key);
		}
	}
	
	public static void main(String[] args)throws Exception {
		loadConfig();
		FileDoUtil.outLog(BUKET_NAME);
		FileDoUtil.outLog(BASE_FILE_PATH);
	}
	
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
	
	/**
	 * 获取文件的url
	 * @param key
	 * @return
	 */
	public static String getUrl(String key){
		StringBuffer strb = new StringBuffer(100);
//		strb.append(PROTOCOL);
//		strb.append(BUKET_NAME);
//		strb.append(".");
//		strb.append( ALIYUN_IMAGE_HOST);
//		if(!key.startsWith("/")){
//			strb.append("/");
//		}
		strb.append(key);
		return strb.toString();
	}
	
	public static int getVlaueFromMusicLoadExcelCellMaping(String key){
		String value = MUSIC_LOAD_EXCEL_CELL_MAPING.get(key);
		if(StringUtil.isEmptyString(value)){
			return 0;
		}else{
			return Integer.parseInt(value);
		}
	}
	
	private static Map<String,String> FILE_STUFFIX_IMAGE_MAPPING = new HashMap<String,String>(){
		private static final long serialVersionUID = -4255850737776740070L;
		{
			put(".mp3", "/images/icon_mp3.png");
			put(".wav", "/images/icon_wma.png");
			put(".jpg", "/images/icon_jpeg.png");
			put(".png", "/images/icon_jpeg.png");
			put(".jpeg", "/images/icon_jpeg.png");
			put(".bmp", "/images/icon_jpeg.png");
			put(".gif", "/images/icon_jpeg.png");
			put(".txt", "/images/icon_txt.png");
		}
	};
	
	public static String getIconByStuffix(String stuffix){
		String result = "";
		if(StringUtil.isEmptyString(stuffix)){
			result = "/images/folder.png";
			return result;
		}
		stuffix = stuffix.trim();
		if(!stuffix.startsWith(".")){
			stuffix  = "."+stuffix;
		}
		result = FILE_STUFFIX_IMAGE_MAPPING.get(stuffix);
		if(StringUtil.isEmptyString(result)){
			result = "/images/icon_3gpp.png";
		}
		return result;
	}
}
