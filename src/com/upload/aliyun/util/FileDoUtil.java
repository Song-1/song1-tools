/**
 * 
 */
package com.upload.aliyun.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Administrator
 *
 */
public class FileDoUtil {
	
	/**
	 * 文件夹（目录）选择对话框
	 */
	public static String folderDig(Shell parent) {
		// 新建文件夹（目录）对话框
		DirectoryDialog folderdlg = new DirectoryDialog(parent);
		// 设置文件对话框的标题
		folderdlg.setText("文件选择");
		// 设置初始路径
		folderdlg.setFilterPath("SystemDrive");
		// 设置对话框提示文本信息
		folderdlg.setMessage("请选择相应的文件夹");
		// 打开文件对话框，返回选中文件夹目录
		String selecteddir = folderdlg.open();
		if (selecteddir == null) {
			return null;
		} else {
			FileDoUtil.outLog("您选中的文件夹目录为：" + selecteddir);
			return selecteddir;
		}
	}
	
	public static boolean hasFloderInTheFloderFile(File file){
		if(file == null){
			return false;
		}
		if(file.exists() && file.isDirectory()){
			File[] files = file.listFiles();
			if(files != null){
				for (File file2 : files) {
					if(file2 != null && file2.isDirectory()){
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
	
	////// test
	public static void main(String[] args) {
		
	}

	
}
