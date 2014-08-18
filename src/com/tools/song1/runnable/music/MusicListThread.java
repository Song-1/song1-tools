/**
 * 
 */
package com.tools.song1.runnable.music;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.farng.mp3.util.MP3Info;
import org.farng.mp3.util.MP3Util;

import com.tools.song1.util.FileDoUtil;
import com.tools.song1.util.ImageFileUtil;
import com.tools.song1.util.JavascriptUtil;
import com.tools.song1.util.NetWorkUtil;
import com.tools.song1.util.StringUtil;
import com.upload.aliyun.MusicConstants;

/**
 * @author Administrator
 *
 */
public class MusicListThread implements Runnable {
	// private String threadName;
	private String bookName;
	private List<File> bookFiles;
	private String _add_;
	private String imgUrl = "";
	private MP3Info mp3Info = null;

	public MusicListThread(String bookName, List<File> bookFiles) {
		if (bookName != null) {
			int index = bookName.lastIndexOf(File.separator) + 1;
			this.bookName = new String(bookName.substring(index));
		}
		this.bookFiles = bookFiles;
		// threadName = "BooKListThread_" + bookName;
		_add_ = "";
	}

	@Override
	public void run() {
		saveBook();
		if (saveSongList()) {
			saveBook();
		}
	}

	public void saveBook() {
		if (bookFiles == null) {
			return;
		}
		for (File file : bookFiles) {
			String key = file.getAbsolutePath();
			String basePath = "";
			if(MusicConstants.BASE_FILE_PATH.endsWith("/")){
				basePath = MusicConstants.BASE_FILE_PATH;
				basePath = new String(basePath.substring(0,basePath.length() - 1)) + File.separator;
			}else if(MusicConstants.BASE_FILE_PATH.endsWith(File.separator)){
				basePath = MusicConstants.BASE_FILE_PATH;
			}else{
				basePath = MusicConstants.BASE_FILE_PATH + File.separator;
			}
			key = key.replace(basePath, "");
			key = key.replace(File.separator, "/");
			if (file.exists()) {
				String fileName = file.getName();
				saveData(fileName, key, file);
			} else {
				FileDoUtil.outLog(key + "\t此文件已被移动......");
			}
		}
	}

	/**
	 * 
	 * @Title: saveData 保存数据
	 * @param name
	 * @param key
	 * @param file void
	 * @throws
	 */
	public void saveData(String name, String key, File file) {
		key = MusicConstants.SERVER_PATH_ROOT + key;
		//boolean flag = OSSUploadUtil.isObjectExist(MusicConstants.BUKET_NAME, key);
		String url = MusicConstants.getUrl(key);
		String img = "";
//		if (!flag) {
//			FileDoUtil.outLog("[ " + StringUtil.getFormateDate() + " ]" + file.getAbsolutePath() + "   服务器此文件不存在");
//			return;
//		}
		if (ImageFileUtil.isImageFile(key)) {
			imgUrl = url;
			return;
		} else if (key.endsWith(".mp3")) {

			mp3Info = MP3Util.getMP3Info(file.getAbsolutePath());
			if (mp3Info != null) {
				img = ImageFileUtil.uploadImage(file.getAbsolutePath());
			}
		}
		FileDoUtil.outLog(name + "\t" + url + "\t" + key);
		String rus = saveSongFile(name, url, img);
		String bookId = JavascriptUtil.getSaveBookResponse(rus);
		addBookId(bookId);
	}

	private void addBookId(String bookId) {
		_add_ += bookId + ",";
	}

	private String saveSongFile(String name, String url, String img) {
		Map<String, String> m = new HashMap<String, String>();
		if (mp3Info != null && mp3Info.getSongTitle() != null && !"".equals(mp3Info.getSongTitle().trim())) {
			//m.put("name", mp3Info.getSongTitle());
			//m.put("singer", mp3Info.getArtist());
			m.put("lyric", mp3Info.getSongLyric());
			m.put("coderate", mp3Info.getBitRate());
			m.put("timestate", mp3Info.getTrackLength());
		} 
		int index = name.lastIndexOf(".");
		int spIndex = name.indexOf("-");
		if(spIndex > 0){
			m.put("singer", new String(name.substring(0, spIndex)));
			m.put("name", new String(name.substring(spIndex+1, index)));
		}else {
			name = new String(name.substring(0, index));
			m.put("name", name);
		}
		m.put("url", url);
		m.put("img", img);
		return NetWorkUtil.doPost(MusicConstants.URL_SAVE_DATA_SONG, m, NetWorkUtil.ENCODE);
	}

	private boolean saveSongList() {
		Map<String, String> m = new HashMap<String, String>();
		MusicDataInfo song = MusicDataGetFromExcel.getObjFormList(bookName);
		m.put("name", bookName);
		m.put("_add_", _add_);
		m.put("img", imgUrl);
		if(song != null){
			m.put("desc", song.getDesc());
			String enverionment = song.getEnverionment();
			if(StringUtil.isEmptyString(enverionment)){
				FileDoUtil.outLog("请确认 歌单 :["+bookName+"]对应的Excel文件中的环境列数据是否存在");
			}else{
				m.put("times", GetMusicTypeFromExcel.getTimes(enverionment));// 心情
				m.put("enverionment", enverionment);// 环境
			}
			m.put("category", song.getCategory());// 心情
		}
		String res = NetWorkUtil.doPost(MusicConstants.URL_SAVE_DATA_SONG_LIST, m, NetWorkUtil.ENCODE);
		String bookId = JavascriptUtil.getSaveBookResponse(res);
		if ("success".equalsIgnoreCase(bookId)) {
			return true;
		}
		return false;
	}

}
