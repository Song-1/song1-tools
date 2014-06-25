/**
 * 
 */
package com.upload.aliyun.util;

/**
 * @author Administrator
 *
 */
public class ImageFileUtil {

	private static String[] imageSuffix = { ".jpg", ".JPG", ".png", ".PNG" };

	/**
	 * 判断是否是图片文件
	 * @param filePath
	 * @return
	 */
	public static boolean isImageFile(String filePath) {
		if (filePath == null || "".equals(filePath.trim())) {
			return false;
		}
		for (String suffix : imageSuffix) {
			if (filePath.endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}

	// // test
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
