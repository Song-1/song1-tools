/**
 * 
 */
package com.upload.aliyun.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

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
		System.out.println("[FILE_PATH]:::" + path);
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
			System.out.println("输出文件：" + path + "  发生异常..........");
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
		System.out.println(path);
		outFile(path, content, true);
	}

	public static void main(String[] args) {
		System.out.println(getBasePath());
	}

}
