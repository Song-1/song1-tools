/**
 * 
 */
package com.upload.aliyun.runnable.enjoy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.upload.aliyun.MusicConstants;
import com.upload.aliyun.util.FileDoUtil;
import com.upload.aliyun.util.ImageFileUtil;
import com.upload.aliyun.util.OSSUploadUtil;
import com.upload.aliyun.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class EnjoyFileEachUtil {
	
	public static final Log log = LogFactory.getLog(EnjoyFileEachUtil.class);

	// /// test
	public static void main(String[] args) {
		test("E:/享CD/地域", EnjoyToDoType.SINGERTYPE);
		test("E:/享CD/风格", EnjoyToDoType.ALBUMSTYLE);
		System.out.println(ALBUM_STYLE_FILES_MAPPING);
	}

	public static final String[] EXCEL_STUFFIXS = { ".xls", ".xlsx" };
	public static final String[] ALBUM_SONG_STUFFIXS = { ".ape", ".wav" ,".flac",".mp3"};

	public static final Map<String, List<File>> ALBUM_STYLE_FILES_MAPPING = new HashMap<String, List<File>>();
	public static final List<File> FILES = new ArrayList<File>();

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
	/**
	 * 添加文件到list
	 * @param file
	 */
	public static void addFiles(File file) {
		FileDoUtil.debugLog("添加歌曲中...：" + file.getAbsolutePath());
		FILES.add(file);
	}
	/**
	 * 添加文件到list
	 * @param file
	 */
	public static void addSingers(File file) {
		FileDoUtil.debugLog("添加歌曲中...：" + file.getAbsolutePath());
		SINGERS.add(file.getName());
	}
	/**
	 * 添加文件到list
	 * @param file
	 */
	public static void addStyle(File file) {
		FileDoUtil.debugLog("添加风格中...：" + file.getName());
		STYLES.add(file.getName());
	}
	
	public static void doEnjoy(){
		initCheck();
		String singerTypeBasePath = MusicConstants.getPropertyValue("enjoy.local.file.base.path.singertype");
		String albumStyleBasePath = MusicConstants.getPropertyValue("enjoy.local.file.base.path.albumstyle");
		test(singerTypeBasePath, EnjoyToDoType.SINGERTYPE);
		test(albumStyleBasePath, EnjoyToDoType.ALBUMSTYLE);
	}
	/**
	 * 检查享CD文件
	 */
	public static void checkEnjoy(){
		initCheck();
		String singerTypeBasePath = MusicConstants.getPropertyValue("enjoy.local.file.base.path.singertype");
//		String albumStyleBasePath = MusicConstants.getPropertyValue("enjoy.local.file.base.path.albumstyle");
		check(singerTypeBasePath);
	}

	private static void initCheck() {
		String buket = MusicConstants.getPropertyValue("enjoy.aliyun.bucket");
		EnjoyThread.ALIYUN_BUKET_NAME = StringUtil.isEmptyString(buket)? MusicConstants.BUKET_NAME:buket;
		String aliyunBasePath = MusicConstants.getPropertyValue("enjoy.aliyun.base.path");
		EnjoyThread.ALIYUN_SERVER_PATH_ROOT = StringUtil.isEmptyString(aliyunBasePath)?MusicConstants.SERVER_PATH_ROOT:aliyunBasePath;
		String serverBaseUrl = MusicConstants.getPropertyValue("enjoy.aliyun.server.base.url");
		EnjoyThread.BASEPATH = StringUtil.isEmptyString(serverBaseUrl)?EnjoyThread.BASEPATH:serverBaseUrl;
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
	
	public static void check(String path) {
		if (StringUtil.isEmptyString(path)) {
			return;
		}
		File baseFile = new File(path);
		scanFile(baseFile);
		doCheckData(path);
	}
	
	/**
	 * 享CD数据上传处理
	 * @param path
	 */
	public static void doEnjoyData(String path){
		path = standardPath(path);
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
					String key = generateKey(path, file);
					if(isMatchTheStuffix(file.getAbsolutePath(),ALBUM_SONG_STUFFIXS)){
						boolean flag = OSSUploadUtil.isObjectExist(MusicConstants.BUKET_NAME, key);
						if(flag){
							data.addSongs(key,file);
						}
//						data.addSongs(key,file);
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
	 * 检查文件数据
	 * @param path
	 */
	public static void doCheckData(String path){
		FileDoUtil.outLog("检查文件数据...");
		path = standardPath(path);
		for (int i = 0; i < FILES.size(); i++) {
			File file = FILES.get(i);
			String key = generateKey(path, file);
			if(isMatchTheStuffix(file.getAbsolutePath(),ALBUM_SONG_STUFFIXS)){
				boolean flag = OSSUploadUtil.isObjectExist(MusicConstants.BUKET_NAME, key);
				if(flag){
					FileDoUtil.debugLog(key + ":存在");
				}else {
					FileDoUtil.outLog(key + ":不存在");
				}
			}else if(ImageFileUtil.isImageFile(file.getAbsolutePath())){
				FileDoUtil.debugLog(key + ":存在图片");
			}else{
				continue;
			}
		}
	}
	/**
	 * 生成key
	 * @param path
	 * @param file
	 * @return
	 */
	private static String generateKey(String path, File file) {
		String key = file.getAbsolutePath();
		key = key.replace(path, "");
		key = key.replace(File.separator, "/");
		
		if (!key.startsWith(MusicConstants.SERVER_PATH_ROOT)) {
			key = MusicConstants.SERVER_PATH_ROOT + key;
		}
		return key;
	}
	private static String standardPath(String path) {
		path = path.replace("/", File.separator);
		if(path.endsWith("/")){
			path = new String(path.substring(0,path.length() - 1)) + File.separator;
		}else if(!path.endsWith(File.separator)){
			path = path + File.separator;
		}
		return path;
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

	/**
	 * 扫描文件
	 * @param file
	 * @param parentFloderName
	 * @param floders
	 */
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
	 * 扫描文件
	 * @param file
	 * @param parentFloderName
	 * @param floders
	 */
	public static void scanFile(File file) {
		if (file != null && file.exists()) {
			FileDoUtil.debugLog("扫描歌曲中...：" + file.getAbsolutePath());
			if (isFloder(file)) {
				File files[]=file.listFiles();
		        for(File file1:files){
		            if(file.isDirectory()){
		            	scanFile(file1);
		            }else{
		            	addFiles(file1);
		            }
		        }
				
			} else {
				addFiles(file);
			}
		}else if (!file.exists()) {
			System.out.println("没有这个文件或者文件夹："+file.getAbsolutePath());
		}{
			System.out.println("文件或者文件夹为空：");
		}
	}
	//风格列表
	public static final List<String> STYLES = new ArrayList<String>();
	public static final List<String> SINGERS = new ArrayList<String>();
	/**
	 * 扫描歌手
	 * @param file
	 * @param parentFloderName
	 * @param floders
	 */
	public static void scanSinger(File file) {
		if (file != null && file.exists()) {
			FileDoUtil.debugLog("扫描歌曲中...：" + file.getAbsolutePath());
			if (isFloder(file)) {
				File files[]=file.listFiles();
		        for(File file1:files){
		            if(file.isDirectory()){
		            	scanFile(file1);
		            }else{
		            	addFiles(file1);
		            }
		        }
				
			} else {
				addFiles(file);
			}
		}else if (!file.exists()) {
			System.out.println("没有这个文件或者文件夹："+file.getAbsolutePath());
		}{
			System.out.println("文件或者文件夹为空：");
		}
	}

	//level用于记录目录的级数。
    public static void listAll(File dir, int level) {
        String space=getSpace(level);
        System.out.println(space+dir.getAbsolutePath());
        File files[]=dir.listFiles();
        if(null==files){
            return;
        }
         
        for(File file:files){
            if(file.isDirectory()){
                listAll(file, ++level);
            }else{
                System.out.println(space+file.getAbsolutePath());
            }
        }
    }
 
    //根据目录级数产生空格。
    public static String getSpace(int level){
        StringBuilder sb=new StringBuilder(); 
        for(int i=0;i<level;i++){
            sb.append("  ");
        }
        return sb.toString();
    }
    
    /**
     * 扫描文件夹
     * @param folderPath
     * @param listFile
     * @param allFile
     * @param context
     */
    public static final List<File> fileList = new ArrayList<File>();
    public static final List<File> folderList = new ArrayList<File>();
    public static void scanFolderFile(String folderPath) { 
        File folder = new File(folderPath);  
        File[] files = folder.listFiles();// 列出所有文件  
        // 将所有文件存入list中  
        if (files != null) {  
            int count = files.length;// 文件个数  
            for (int i = 0; i < count; i++) {  
                File file = files[i];  
                // 如果不为文件夹就将文件添加到listFile中  
                if (!file.isDirectory()) {  
                	fileList.add(file);  
                } else {  
                    // 为文件夹就 递归扫描  
                	scanFolderFile(file.getPath());  
                }  
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
