/**
 * 
 */
package com.upload.aliyun.util;

import java.io.File;

/**
 * @author Administrator
 *
 */
public class FileDoUtil {

	/**
	 * 查找文件
	 * @param fileRelativePath
	 * @return File
	 */
	public static File findFile(String fileRelativePath) {
		String path = getBasePath();
		path += fileRelativePath;
		System.out.println("[FILE_PATH]:::"+path);
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

	public static void main(String[] args) {

	}

}
