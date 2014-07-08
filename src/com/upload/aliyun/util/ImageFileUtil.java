/**
 * 
 */
package com.upload.aliyun.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.ImageIcon;

import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;

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
	
	/**
	 * 
	 * @Title: getImageByte 获取id3信息中的图片
	 * @param mp3filepath
	 * @return    
	 * byte[]    
	 * @throws
	 */
	public static byte[] getImageByte(String mp3filepath){
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

	// // test
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
			String dir = "E:\\tmp\\13 电子\\";
			String filename = "01 - 2 Brothers on the 4th Floor - Dreams.mp3";
			String suffix = filename.substring(filename.lastIndexOf(".")+1);
			String filename1 = filename.substring(0, filename.lastIndexOf("."));
			FileDoUtil.outLog(filename1);
			FileDoUtil.outLog(suffix);
			String url =dir +  filename;  
			File sourceFile = new File(url);  
			org.jaudiotagger.audio.mp3.MP3File mp3file = new org.jaudiotagger.audio.mp3.MP3File(sourceFile); 
			AbstractID3v2Tag tag = mp3file.getID3v2Tag();  
			AbstractID3v2Frame frame = (AbstractID3v2Frame) tag.getFrame("APIC");  
			FrameBodyAPIC body = (FrameBodyAPIC) frame.getBody();  
			byte[] imageData = body.getImageData();  
			Image img=Toolkit.getDefaultToolkit().createImage(imageData, 0,imageData.length);  
			FileDoUtil.outLog("img----" + imageData);  
			ImageIcon icon = new ImageIcon(img);              
			FileOutputStream fos = new FileOutputStream(dir + filename1 + ".jpg");  
			fos.write(imageData);  
			fos.close();  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
