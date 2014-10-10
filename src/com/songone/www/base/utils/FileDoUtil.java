/**
 * 
 */
package com.songone.www.base.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.aliyun.AliyunUtil;
import com.songone.www.aliyun.exceptions.AliyunObjectKeyIllegalException;

/**
 * @author Administrator
 *
 */
public class FileDoUtil {
	private static final Logger logger = LogManager.getLogger(FileDoUtil.class);

	/**
	 * 判断文件夹下是否有文件夹
	 * 
	 * @param file
	 * @return
	 */
	public static boolean hasFloderInTheFloderFile(File file) {
		if (file == null) {
			return false;
		}
		if (file.exists() && file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File file2 : files) {
					if (file2 != null && file2.isDirectory()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 查找文件
	 * 
	 * @param fileRelativePath
	 * @return File
	 */
	public static File findFile(String fileRelativePath) {
		String path = getBasePath();
		path += fileRelativePath;
		// FileDoUtil.outLog("[FILE_PATH]:::" + path);
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

	/**
	 * 创建文件的目录路径并创建文件.<br>
	 * 
	 * @param file
	 */
	public static void mkDirs(File file) {
		if (file != null && !file.exists()) {
			String path = file.getAbsolutePath();
			int index = path.lastIndexOf(File.separator);
			String dirsPath = new String(path.substring(0, index));
			File filedirs = new File(dirsPath);
			if (!filedirs.exists()) {
				filedirs.mkdirs();
			}
		}
	}

	/**
	 * 
	 * @param path
	 * @param content
	 * @param isAdditionalWrite
	 */
	public static void outFile(String path, String content, boolean isAdditionalWrite) {
		if (StringUtil.isEmptyString(content)) {
			return;
		}
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
			logger.error(e.getMessage(), e);
		}
	}

	public static final String[] EXCEL_STFFIX = { ".xls", ".xlsx" };

	/**
	 * 判断是否是excel文件
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isExcel(String str) {
		if (StringUtil.isEmptyString(str)) {
			return false;
		} else {
			str = str.toLowerCase();
			for (String stffix : EXCEL_STFFIX) {
				if (str.endsWith(stffix)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 将网络文件保存到本地并上传到阿里云
	 * 
	 * @param bucket
	 * @param key
	 * @param url
	 * @return
	 */
	public static String uploadFileToAliyun(String bucket, String key, String url) {
		try {
			if (AliyunUtil.isExistObjectForTheKey(bucket, key)) {
				return key;
			}
		} catch (AliyunObjectKeyIllegalException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		File file = null;
		try {
			String tempFilePath = HttpClientUtil.saveFileByURL(url, null);
			if (StringUtil.isEmptyString(tempFilePath)) {
				return null;
			}
			file = new File(tempFilePath);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (file == null || !file.exists()) {
			return null;
		}
		boolean flag = aliyunUploadFile(bucket, key, file);
		file.delete();
		if (flag) {
			return key;
		}
		return null;
	}

	private static boolean aliyunUploadFile(String bucket, String key, File file) {
		boolean flag = false;
		try {
			flag = AliyunUtil.uploadBigFile(bucket, key, file);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return flag;
	}

	// //// test
	public static void main(String[] args) {
		File file = findFile("db.properties");
		if (file != null && file.exists()) {
			System.out.println(file.getAbsolutePath());
		}
	}

}
