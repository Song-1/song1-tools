/**
 * 
 */
package com.upload.aliyun.runnable.enjoy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.upload.aliyun.MusicConstants;
import com.upload.aliyun.util.ImageFileUtil;
import com.upload.aliyun.util.OSSUploadUtil;
import com.upload.aliyun.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class EnjoyFileEachUtil {

	// /// test
	public static void main(String[] args) {
		test("E:/享CD/地域", EnjoyToDoType.SINGERTYPE);
		test("E:/享CD/风格", EnjoyToDoType.ALBUMSTYLE);
		System.out.println(ALBUM_STYLE_FILES_MAPPING);
	}

	public static final String[] EXCEL_STUFFIXS = { ".xls", ".xlsx" };
	public static final String[] ALBUM_SONG_STUFFIXS = { ".ape", ".wav" ,".flac",".mp3"};

	public static final Map<String, List<File>> ALBUM_STYLE_FILES_MAPPING = new HashMap<String, List<File>>();

	public static void addMappings(String key, File file) {
		if (StringUtil.isEmptyString(key)) {
			return;
		}
		List<File> files = ALBUM_STYLE_FILES_MAPPING.get(key);
		if (files == null) {
			files = new ArrayList<File>();
		}
		files.add(file);
		ALBUM_STYLE_FILES_MAPPING.put(key, files);
	}
	
	public static void doEnjoy(){
		String buket = MusicConstants.getPropertyValue("enjoy.aliyun.bucket");
		EnjoyThread.ALIYUN_BUKET_NAME = StringUtil.isEmptyString(buket)? MusicConstants.BUKET_NAME:buket;
		String aliyunBasePath = MusicConstants.getPropertyValue("enjoy.aliyun.base.path");
		EnjoyThread.ALIYUN_SERVER_PATH_ROOT = StringUtil.isEmptyString(aliyunBasePath)?MusicConstants.SERVER_PATH_ROOT:aliyunBasePath;
		String serverBaseUrl = MusicConstants.getPropertyValue("enjoy.aliyun.server.base.url");
		EnjoyThread.BASEPATH = StringUtil.isEmptyString(serverBaseUrl)?EnjoyThread.BASEPATH:serverBaseUrl;
		String singerTypeBasePath = MusicConstants.getPropertyValue("enjoy.local.file.base.path.singertype");
		String albumStyleBasePath = MusicConstants.getPropertyValue("enjoy.local.file.base.path.albumstyle");
		test(singerTypeBasePath, EnjoyToDoType.SINGERTYPE);
		test(albumStyleBasePath, EnjoyToDoType.ALBUMSTYLE);
	}

	/**
	 * 判断后缀是否相匹配
	 * @param str
	 * @param stuffixs
	 * @return
	 */
	public static boolean isMatchTheStuffix(String str, String[] stuffixs) {
		if (StringUtil.isEmptyString(str)) {
			return false;
		} else if (stuffixs == null) {
			return false;
		} else {
			str = str.toLowerCase();
			for (String s : stuffixs) {
				if (str.endsWith(s))
					return true;
			}
		}
		return false;
	}

	public static void test(String path, EnjoyToDoType type) {
		if (StringUtil.isEmptyString(path)) {
			return;
		}
		ALBUM_STYLE_FILES_MAPPING.clear();
		File baseFile = new File(path);
		switch (type) {
		case SINGERTYPE:
			eachFile(baseFile, "", 1);
			doSingerAndSingerTypeData();
			break;
		case ALBUMSTYLE:
			eachFile(baseFile, "", 0);
			doEnjoyData(path);
			break;
		default:
			break;
		}
	}
	
	public static void doEnjoyData(String path){
		if(path.endsWith("/")){
			path = new String(path.substring(0,path.length() - 1)) + File.separator;
		}else if(!path.endsWith(File.separator)){
			path = path + File.separator;
		}
		EnjoyThread.LOCAL_FILE_BASE_PATH = path;
		for (Map.Entry<String, List<File>> entry : ALBUM_STYLE_FILES_MAPPING.entrySet()) {
			EnjoyAlbumData data = new EnjoyAlbumData();
			data.addNameStr(entry.getKey());
			List<File> files = entry.getValue();
			if (files != null) {
				for (File file : files) {
					if (file == null) {
						continue;
					}
					String key = file.getAbsolutePath();
					key = key.replace(path, "");
					key = key.replace(File.separator, "/");
					if (!key.startsWith(MusicConstants.SERVER_PATH_ROOT)) {
						key = MusicConstants.SERVER_PATH_ROOT + key;
					}
					if(isMatchTheStuffix(file.getAbsolutePath(),ALBUM_SONG_STUFFIXS)){
//						boolean flag = OSSUploadUtil.isObjectExist(MusicConstants.BUKET_NAME, key);
//						if(flag){
//							data.addSongs(file);
//						}
						data.addSongs(file);
					}else if(ImageFileUtil.isImageFile(file.getAbsolutePath())){
						data.addImages(file);
					}else{
						continue;
					}
				}
			}
			EnjoyThread.saveAlbum(data);
		}
	}

	/**
	 * 读取享CD地域下面的Excel文件
	 */
	public static void doSingerAndSingerTypeData() {
		for (Map.Entry<String, List<File>> entry : ALBUM_STYLE_FILES_MAPPING.entrySet()) {
			List<File> files = entry.getValue();
			if (files != null) {
				for (File file : files) {
					if (file != null && isMatchTheStuffix(file.getAbsolutePath(),EXCEL_STUFFIXS)) {
						new GetSingerInfoDataFromExcel().doExcel(file);
					}
				}
			}
		}
		//System.out.println(GetSingerInfoDataFromExcel.SINGER_TYPE_MAPPING);
	}

	public static void eachFile(File file, String parentFloderName, int floders) {
		if (file != null && file.exists()) {
			if (isFloder(file)) {
				if (floders > 0) {
					if (StringUtil.isEmptyString(parentFloderName)) {
						parentFloderName = file.getName();
					} else {
						parentFloderName += "/" + file.getName();
					}
				}
				File[] files = file.listFiles();
				if (files != null) {
					floders++;
					for (File file2 : files) {
						eachFile(file2, parentFloderName, floders);
					}
				}
			} else {
				addMappings(parentFloderName, file);
			}
		}
	}

	/**
	 * 判断文件是否是文件夹.<br>
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isFloder(File file) {
		if (file != null && file.exists() && file.isDirectory()) {
			return true;
		} else {
			return false;
		}
	}

}
