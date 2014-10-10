/**
 * 
 */
package com.songone.www.enjoy.action;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.action.BaseFileEachAction;
import com.songone.www.base.utils.ImageDoUtil;
import com.songone.www.enjoy.models.Album;
import com.songone.www.enjoy.models.AlbumSong;
import com.songone.www.enjoy.service.AlbumService;
import com.songone.www.enjoy.service.AlbumSongService;
import com.songone.www.enjoycd.constants.EnjoyCDConstants;
import com.songone.www.enjoycd.utils.SaveEnjoyCDDataUtil;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class EachFileAction extends BaseFileEachAction{
	public static final Logger logger = LogManager.getLogger(EachFileAction.class);
	public static final String SONGFILEREG = "(\\d+)\\s+-{1}\\s+(.+[.])";
	private static final int DATA_TYPE = 1;

	private AlbumService albumService = new AlbumService();
	private AlbumSongService albumSongService = new AlbumSongService();
	private String styleName = "";
	private String singerName = "";
	private String singerImg = "";
	private String singerIcon = "";

	public void eachFilesBefore() {
		// File file = new File(EnjoyCDConstants.ENJOY_CD_LOCAL_FILE_PATH);
		File file = new File("E:\\享CD");
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		File[] files = file.listFiles();
		if (files == null) {
			return;
		}
		for (File f : files) {
			if (!valiDateFile(file)) {
				continue;
			}
			setBasePath(f.getAbsolutePath());
			setCreateInfo(f.getName());
			eachFiles(f, 0,null);
		}
	}

	public int eachFiles(File file, int index,Album album) {
		if(!valiDateFile(file)){
			return 0;
		}
		int albumId = 0;
		if(album != null){
			albumId = album.getId();
		}
		if (file.isDirectory()) {
			index += 1;
			String name = file.getName();
			if(index == 2){
				styleName = name;
			}else if(index == 3){
				singerName = name;
			}else if(index == 4){
				album = new Album();
				album.setAlbumName(name);
				album.setSingerName(singerName);
				album.setStyleName(styleName);
				album.setSingerImg(singerImg);
				album.setSingerIcon(singerIcon);
				album.setCreateUser(createUser);
				album.setCreateDate(createDate);
				System.out.println(album);
				//// save album
				album = albumService.save(album);
			}
			File[] files = file.listFiles();
			if (files == null) {
				return 0;
			}
			int counts = 0;
			for (File f : files) {
				counts += eachFiles(f, index,album);
			}
			if(index == 4){
				album.setAlbumSongs(counts);
				/// update album
				albumService.update(album);
			}
			return 0;
		} else if (file.isFile()) {
			int result = 0;
			String path = file.getAbsolutePath();
			String key = getAliyunKey(path);
			if(SaveEnjoyCDDataUtil.matchSttfix(path) && index == 4){
				saveAlbumSong(file.getName(), path, albumId);
				result = 1;
			}else if(ImageDoUtil.isImageFile(path)){
				if(index == 3 && isImage(file)){
					singerImg = key;
					singerIcon = createIcon(file, albumId);
				}else if(index == 3 && isIcon(file)){
					singerIcon = key;
				}else if(index == 4 && isImage(file)){
					album.setAlbumImg(key);
					String iconKey = createIcon(file, albumId);
					album.setAlbumIcon(iconKey);
				}else if(index == 4 && isIcon(file)){
					album.setAlbumIcon(key);
				}
				aliyunMappingFileModelService.saveByFile(path, key, DATA_TYPE, albumId);
			}
			return result;
		} else {
			return 0;
		}
	}
	
	private void saveAlbumSong(String name,String path,int albumId){
		AlbumSong song = new AlbumSong();
		Pattern p = Pattern.compile(SONGFILEREG);
		Matcher m = p.matcher(name);
		if (m.find()) {
			String seatStr = m.group(1).trim();
			String songname = m.group(2).trim();
			songname = new String(songname.substring(0, songname.lastIndexOf(".")));
			song.setName(songname);
			song.setSeat(Integer.parseInt(seatStr));
		}else{
			song.setName(name);
			song.setSeat(0);
		}
		String key = getAliyunKey(path);
		song.setUrl(key);
		song.setParentId(albumId);
		albumSongService.save(song);
		aliyunMappingFileModelService.saveByFile(path, key, DATA_TYPE, song.getId());
	}
	
	public String getAliyunKey(String path) {
		return getAliyunKey(path, EnjoyCDConstants.ENJOY_CD_ALIYUN_BASE_KEY);
	}

	private String createIcon(File file,int foreignKeyId) {
		return createIcon(file, foreignKeyId, DATA_TYPE);
	}

	@Override
	public String toString() {
		return "EachFileAction [basePath=" + basePath + ", createDate=" + createDate + ", createUser=" + createUser + "]";
	}

}
