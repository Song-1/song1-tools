/**
 * 
 */
package com.songone.www.cherrytime.action;

import java.io.File;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.farng.mp3.util.MP3Info;
import org.farng.mp3.util.MP3Util;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;

import com.songone.www.base.action.BaseFileEachAction;
import com.songone.www.base.utils.ImageDoUtil;
import com.songone.www.base.utils.StringUtil;
import com.songone.www.cherrytime.constants.CherryTimeConstants;
import com.songone.www.cherrytime.model.SongList;
import com.songone.www.cherrytime.model.Songs;
import com.songone.www.cherrytime.service.SongListService;
import com.songone.www.cherrytime.service.SongsService;
import com.songone.www.enjoy.action.EachFileAction;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class CherryTimeFileEachAction extends BaseFileEachAction{
	public static final Logger logger = LogManager.getLogger(EachFileAction.class);
	private static final String SONG_NAME_REG = "^(.+)-(.+)[.]+";
	private static final int DATA_TYPE = 2;
	
	private SongListService songListService = new SongListService();
	private SongsService songsService = new SongsService();
	
	public void eachFiles() {
		String path = CherryTimeConstants.CHERRY_TIME_LOCAL_FILE_PATH;
		File file = new File(path);
		if (!file.exists()) {
			return;
		}else if(!file.isDirectory()){
			return;
		}
		File[] files = file.listFiles();
		if(files == null){
			return;
		}
		for (File f : files) {
			setBasePath(f.getAbsolutePath());
			setCreateInfo(f.getName());
			eachFiles(f, 0,null);
		}
	}
	private String songListName  = "";
	private String styleName= "";
	private String environmentName= "";	
	public int eachFiles(File f,int index,SongList songList) {
		if(f.isDirectory()){
			index += 1;
			String name = f.getName();
			if(index == 5){
				/// 歌单层
				songListName = name;
				songList = saveSongList();
			}else if(index == 4){
				//// 风格层
				styleName = name;
			}else if(index == 3){
				//// 场景层
				environmentName = name;
			}else if(index > 5){
				return 0;
			}
			File[] files = f.listFiles();
			if(files == null){
				return 0;
			}
			int songs = 0;
			for (File file : files) {
				songs += eachFiles(file, index,songList);
			}
			if(index == 5){
				songList.setSongs(songs);
				songListService.update(songList);
			}
			return 0;
		}else if(f.isFile()){
			int result = 0;
			if(index == 5){
				/// 歌单层
				int id = songList.getId();
				String path = f.getAbsolutePath();
				if(ImageDoUtil.isImageFile(path)){
					String key = getAliyunKey(path);
					if(isImage(f)){
						songList.setImg(key);
						String iconKey = createIcon(f, id,DATA_TYPE);
						songList.setIcon(iconKey);
					}else if(isIcon(f)){
						songList.setIcon(key);
					}
					aliyunMappingFileModelService.saveByFile(path, key, DATA_TYPE, id);
				}else if(path.toLowerCase().endsWith(".mp3")){
					result = 1;
					saveSongFile(f,id);
				}else if(path.toLowerCase().endsWith(".txt") && isDescText(f)){
					songList.setDescription(getDesc(f));
				}
			}
			return result;
		}else {
			return 0;
		}
	}
	
	private SongList saveSongList(){
		SongList songList = new SongList();
		songList.setName(songListName);
		songList.setStyleName(styleName);
		songList.setEnvironmentName(environmentName);
		songList.setCreateDate(createDate);
		songList.setCreateUser(createUser);
		songList.setState(1);
		songList = songListService.save(songList);
		return songList;
	}
	/**
	 * 歌单下面的歌曲文件
	 * @param f
	 */
	private void saveSongFile(File f,int parentId){
		String path = f.getAbsolutePath();
		String key = getAliyunKey(path);
		Songs song = new Songs();
		song.setUrl(key);
		String name = f.getName();
		Pattern p = Pattern.compile(SONG_NAME_REG);
		Matcher m = p.matcher(name);
		if (m.find()) {
			String singer = m.group(1).trim();
			String songname = m.group(2).trim();
			song.setSingerName(singer.trim());
			song.setName(songname.trim());
		}else{
			song.setName(name);
		}
		MP3Info mp3Info = MP3Util.getMP3Info(path);
		if (mp3Info != null) {
			song.setLyric(mp3Info.getSongLyric());
			song.setCoderate(StringUtil.getIntFormStr(mp3Info.getBitRate()));
			song.setTimestate(mp3Info.getTrackLength());
			String imgkey = getAndSaveImgFormMp3Id3(path,parentId);
			song.setImg(imgkey);
		}
		song.setParentId(parentId);
		song.setState(1);
		song = songsService.save(song);
		aliyunMappingFileModelService.saveByFile(path, key, DATA_TYPE, song.getId());
	}
	
	/**
	 * 获取mp3文件中的图片
	 * @param mp3filepath
	 * @return
	 */
	public String getAndSaveImgFormMp3Id3(String mp3filepath,int parentId) {
		String key = null;
		String path = mp3filepath.replace(".mp3", ".jpg");
		File file = new File(path);
		if(file.exists()){
			key = getAliyunKey(path);
			aliyunMappingFileModelService.saveByFile(path, key, DATA_TYPE, parentId);
			return key;
		}
		try {
			File sourceFile = new File(mp3filepath);
			org.jaudiotagger.audio.mp3.MP3File mp3file = new org.jaudiotagger.audio.mp3.MP3File(sourceFile);
			AbstractID3v2Tag tag = mp3file.getID3v2Tag();
			if (tag == null) {
				return key;
			}
			AbstractID3v2Frame frame = (AbstractID3v2Frame) tag.getFrame("APIC");
			if (frame == null) {
				return key;
			}
			FrameBodyAPIC body = (FrameBodyAPIC) frame.getBody();
			byte[] imageData = body.getImageData();
			if(imageData == null || imageData.length <= 0){
				return key;
			}
			FileOutputStream outputStream = new FileOutputStream(file);
			outputStream.write(imageData, 0, imageData.length);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		key = getAliyunKey(path);
		aliyunMappingFileModelService.saveByFile(path, key, DATA_TYPE, parentId);
		return key;

	}
	
	/**
	 * 获取阿里云的key值
	 * @param path
	 * @return
	 */
	public String getAliyunKey(String path) {
		return getAliyunKey(path, CherryTimeConstants.CHERRY_TIME_ALIYUN_BASE_KEY);
	}
	
}
