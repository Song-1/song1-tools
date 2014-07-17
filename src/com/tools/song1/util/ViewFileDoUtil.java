/**
 * 
 */
package com.tools.song1.util;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Administrator
 *
 */
public class ViewFileDoUtil {
	
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

	// // test
	public static void main(String[] args) {

	}

}
