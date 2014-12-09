/**
 * 
 */
package com.tools.song1.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

/**
 * @author Administrator
 *
 */
public class FileDoUtil {

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
		System.out.println("findFile getBasePath:"+path);
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

		File directory = new File("");// 设定为当前文件夹
		try {
			System.out.println("获取标准的路径:"+directory.getCanonicalPath());// 获取标准的路径
			System.out.println(directory.getAbsolutePath());// 获取绝对路径
		} catch (Exception e) {
		}
		URL resource = FileDoUtil.class.getResource("/");
		String path = null;
		if (resource == null) {
			try {
				path = directory.getCanonicalPath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			path = resource.toString();
		}
		System.out.println("path:" + path);
		if (path.contains("\\")) {
			path.replace("/", "\\");
		}
		if (!path.endsWith("/")) {
			path = path + "/";
		}
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
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param path
	 * @param content
	 * @param isAdditionalWrite
	 */
	public static void outFile(String path, String content,
			boolean isAdditionalWrite) {
		if (StringUtil.isEmptyString(content)) {
			return;
		}
		File file = new File(path);
		System.out.println("输出文件路径:" + file.getAbsolutePath());
		try {
			if (!file.exists()) {
				int index = path.lastIndexOf("/");
				String dirsPath = new String(path.substring(0, index));
				File filedirs = new File(dirsPath);
				if (!filedirs.exists()) {
					filedirs.mkdirs();
				}
				file.createNewFile();
			} else {
				System.out.println("file exists:" + file.getAbsolutePath());
				if (isAdditionalWrite) {
					content = "\r\n" + content;
					// ///在指定文件后追加输出
					FileOutputStream fos = FileUtils.openOutputStream(file,
							true);
					fos.write(content.getBytes("UTF-8"));
					fos.close();
				} else {
					FileUtils.writeStringToFile(file, content, "UTF-8");
				}

			}
		} catch (IOException e) {
			// FileDoUtil.outLog("输出文件：" + path + "  发生异常..........");
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
		// System.out.println("日志路径："+path);
		content = StringUtil.getFormateDate("yyyy-MM-dd hh:mm:ss:SSS")
				+ " ::[INFO]:: " + content;
		System.out.println("日志内容：" + content);
		outFile(path, content, true);
	}

	public static void errorLog(String content) {
		String date = StringUtil.getFormateDate("yyyy-MM-dd");
		String path = getBasePath() + "log/error.log." + date + ".txt";
		content = StringUtil.getFormateDate("yyyy-MM-dd hh:mm:ss:SSS")
				+ " ::[ERROR]:: " + content;
		System.out.println("日志内容：" + content);
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
		// FileDoUtil.outLog(path);
		content = StringUtil.getFormateDate("yyyy-MM-dd hh:mm:ss:SSS")
				+ " ::[DEBUG]:: " + content;
		System.out.println("日志内容：" + content);
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
		System.out.println("日志内容：" + content);
		outFile(path, content, true);
	}

	// //// test
	public static void main(String[] args) {

	}

}
