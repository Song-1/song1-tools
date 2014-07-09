/**
 * 
 */
package com.upload.aliyun.util;

import java.io.File;
import java.io.FileNotFoundException;

import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;

import com.upload.aliyun.MusicConstants;

/**
 * @author Administrator
 *
 */
public class ImageFileUtil {

	private static String[] imageSuffix = { ".jpg", ".JPG", ".png", ".PNG" };

	/**
	 * 判断是否是图片文件
	 * 
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

	/**
	 * 
	 * @Title: getImageByte 获取id3信息中的图片
	 * @param mp3filepath
	 * @return byte[]
	 * @throws
	 */
	public static byte[] getImageByte(String mp3filepath) {
		byte[] imageData = null;
		try {
			File sourceFile = new File(mp3filepath);
			org.jaudiotagger.audio.mp3.MP3File mp3file = new org.jaudiotagger.audio.mp3.MP3File(sourceFile);
			AbstractID3v2Tag tag = mp3file.getID3v2Tag();
			if (tag == null) {
				return null;
			}
			AbstractID3v2Frame frame = (AbstractID3v2Frame) tag.getFrame("APIC");
			if (frame == null) {
				return null;
			}
			FrameBodyAPIC body = (FrameBodyAPIC) frame.getBody();
			imageData = body.getImageData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageData;

	}

	/**
	 * 上传歌曲的图片
	 * @param mp3filepath
	 * @return
	 */
	public static String uploadImage(String mp3filepath) {
		String imageURL = null;
		if (StringUtil.isEmptyString(mp3filepath)) {
			return imageURL;
		} else if (!mp3filepath.endsWith(".mp3")) {
			return imageURL;
		}
		byte[] imageByte = ImageFileUtil.getImageByte(mp3filepath);
		if (imageByte != null) {
			try {
				String filename = mp3filepath.replace(".mp3", ".jpg");
				filename = filename.replace(MusicConstants.BASE_FILE_PATH + File.separator, "");
				imageURL = filename.replace(File.separator, "/");
				if (!OSSUploadUtil.isObjectExist(MusicConstants.BUKET_NAME, imageURL)) {
					FileDoUtil.debugLog(MusicConstants.BUKET_NAME + " bucket 下面的 " + imageURL + "不存在，正在上传中...");
					OSSUploadUtil.uploadImage(MusicConstants.BUKET_NAME, imageURL, imageByte);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		FileDoUtil.debugLog("歌曲图片:"+imageURL);
		return imageURL;
	}

	// // test
	public static void main(String[] args) {
		
	}

}
