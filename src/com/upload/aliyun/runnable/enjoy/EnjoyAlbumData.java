/**
 * 
 */
package com.upload.aliyun.runnable.enjoy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tools.song1.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class EnjoyAlbumData {
	
	private String styleName;
	private String singerName;
	private String albumName;
	private String albumImage;
	private String albumIcon;
	private Map<String,File> albumSongs = new HashMap<String,File>();
	private List<File> albumImages = new ArrayList<File>();
	
	
	public void addImages(File file){
		if(file == null){
			return;
		}
		albumImages.add(file);
	}
	
	public void addSongs(String key, File file){
		if(file == null || StringUtil.isEmptyString(key)){
			return;
		}else if(StringUtil.isEmptyString(key)){
			return;
		}
		albumSongs.put(key,file);
	}
	
	public void addNameStr(String str){
		if(StringUtil.isEmptyString(str)){
			return;
		}
		String[] strs = str.split("/");
		if(strs.length >= 3){
			int size = strs.length;
			this.setAlbumName(strs[size -1]);
			this.setSingerName(strs[size - 2]);
			String styleStr = "";
			for(int i = 0; i <(size - 2);i++){
				if(StringUtil.isEmptyString(strs[i])){
					continue;
				}
				styleStr += strs[i]+"/";
			}
			if(styleStr.endsWith("/")){
				styleStr = new String(styleStr.substring(0,styleStr.length() - 1));
			}
			this.setStyleName(styleStr);
		}
	}
	
	public String getStyleName() {
		return styleName;
	}
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	public String getSingerName() {
		return singerName;
	}
	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public String getAlbumImage() {
		return albumImage;
	}
	public void setAlbumImage(String albumImage) {
		this.albumImage = albumImage;
	}
	public String getAlbumIcon() {
		return albumIcon;
	}
	public void setAlbumIcon(String albumIcon) {
		this.albumIcon = albumIcon;
	}

	public Map<String,File> getAlbumSongs() {
		return albumSongs;
	}

	public List<File> getAlbumImages() {
		return albumImages;
	}
	
	
	
}
