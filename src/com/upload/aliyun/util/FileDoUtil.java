/**
 * 
 */
package com.upload.aliyun.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.farng.mp3.util.MP3Info;
import org.farng.mp3.util.MP3Util;

import com.upload.aliyun.MusicConstants;

/**
 * @author Administrator
 *
 */
public class FileDoUtil {

	/**
	 * 查找文件
	 * 
	 * @param fileRelativePath
	 * @return File
	 */
	public static File findFile(String fileRelativePath) {
		String path = getBasePath();
		path += fileRelativePath;
		FileDoUtil.outLog("[FILE_PATH]:::" + path);
		// /
		File file = new File(path);
		if (file.exists()) {
			return file;
		} else {
			return null;
		}
	}

	/**
	 * 获取文件的基路径
	 * 
	 * @return
	 */
	public static String getBasePath() {
		String path = FileDoUtil.class.getResource("/").toString();
		path = path.replace("file:/", "");
		return path;
	}

	public static void outFile(String path, String content, boolean isAdditionalWrite) {
		File file = new File(path);
		try {
			if (!file.exists()) {
				int index = path.lastIndexOf("/");
				String dirsPath = new String(path.substring(0, index));
				File filedirs = new File(dirsPath);
				if (!filedirs.exists()) {
					filedirs.mkdirs();
				}
				file.createNewFile();
			}
			if (file.exists()) {
				if (isAdditionalWrite) {
					content = "\r\n" + content;
					// ///在指定文件后追加输出
					FileOutputStream fos = FileUtils.openOutputStream(file, true);
					fos.write(content.getBytes("UTF-8"));
					fos.close();
				} else {
					FileUtils.writeStringToFile(file, content, "UTF-8");
				}
			}
		} catch (IOException e) {
			FileDoUtil.outLog("输出文件：" + path + "  发生异常..........");
			e.printStackTrace();
		}
	}

	/**
	 * 输出日志
	 * 
	 * @param content
	 */
	public static void outLog(String content) {
		String date = StringUtil.getFormateDate("yyyy-MM-dd");
		String path = getBasePath() + "log/log_" + date + ".txt";
//		System.out.println("日志路径："+path);
		System.out.println("日志内容："+content);
		outFile(path, content, true);
	}
	
	/**
	 * 输出日志
	 * 
	 * @param content
	 */
	public static void debugLog(String content) {
		String date = StringUtil.getFormateDate("yyyy-MM-dd");
		String path = getBasePath() + "log/debugLog_" + date + ".txt";
//		FileDoUtil.outLog(path);
		System.out.println("日志内容："+content);
		outFile(path, content, true);
	}
	
	/**
	 * 输出日志
	 * 
	 * @param content
	 */
	public static void sqlLog(String content) {
		String date = StringUtil.getFormateDate("yyyy-MM-dd");
		String path = getBasePath() + "log/debugLog_" + date + ".sql";
		System.out.println("日志内容："+content);
		outFile(path, content, true);
	}
	public String pathname = "F:\\ct\\场景：音乐文件+表格";
	List<String> fileList = new ArrayList<String>();
	List<String> filedir = new ArrayList<String>();
	public static void main(String[] args) {
		
		FileDoUtil.outLog(getBasePath());
		try {
			MusicConstants.loadConfig();
			OSSUploadUtil.init();
			FileDoUtil fileDoUtil = new FileDoUtil();
			String img;
			fileDoUtil.pathname = MusicConstants.BASE_FILE_PATH;
			fileDoUtil.showAllFiles(new File(fileDoUtil.pathname));
//			FileDoUtil.outLog("文件：");
//			printlist(fileDoUtil.fileList);
			for (String mp3filepath : fileDoUtil.fileList) {
				FileDoUtil.outLog(mp3filepath);
				String key = mp3filepath.replace(MusicConstants.BASE_FILE_PATH + File.separator, "");
				key = key.substring(key.indexOf("\\")+1);
//				replace = string.replace("/", "\\");
				key = key.replace(File.separator, "/");
				key = MusicConstants.SERVER_PATH_ROOT + key;
				boolean objectExist = OSSUploadUtil.isObjectExist(MusicConstants.BUKET_NAME, key);
				debugLog(objectExist + ":"+key);
				boolean endsWith = key.endsWith(".mp3");
				if (objectExist && endsWith) {
					byte[] imageByte = ImageFileUtil.getImageByte(mp3filepath);
					String filename = key.substring(0, key.lastIndexOf("."));
					if (imageByte != null) {
						try {
							filename = filename.replace(MusicConstants.BASE_FILE_PATH + File.separator, "");
							img = filename + ".jpg";
							img = img.replace(File.separator, "/");
//							img = MusicConstants.SERVER_PATH_ROOT + img;
							if (!OSSUploadUtil.isObjectExist(MusicConstants.BUKET_NAME, img)) {
								debugLog(MusicConstants.BUKET_NAME +" bucket 下面的 " + img + "不存在，正在上传中...");
								OSSUploadUtil.uploadImage(MusicConstants.BUKET_NAME, img, imageByte);
							}
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
					MP3Info mp3Info = MP3Util.getMP3Info(mp3filepath);
				}
			}
			
			String sql = "";
//			FileDoUtil.outLog("目录：");
//			printlist(fileDoUtil.filedir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public  void showAllFiles(File dir) throws Exception {
//		FileDoUtil.outLog(dir.getAbsolutePath());
		if (dir.isDirectory()) {
			filedir.add(dir.getAbsolutePath());
			File[] fs = dir.listFiles();
			for (int i = 0; i < fs.length; i++) {
//				FileDoUtil.outLog(fs[i].getAbsolutePath());
				if (fs[i].isDirectory()) {
					try {
						showAllFiles(fs[i]);
					} catch (Exception e) {
					}
				}else{
					fileList.add(fs[i].getAbsolutePath());
				}
			}
		}
	}
	
	public  static void printlist(List<String> sl) {
		for (String string : sl) {
			debugLog(string);
		}
	}
}
