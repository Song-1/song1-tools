/**
 * 
 */
package com.upload.aliyun.runnable.music;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.farng.mp3.util.MP3Info;
import org.farng.mp3.util.MP3Util;

import com.upload.aliyun.MusicConstants;
import com.upload.aliyun.util.FileDoUtil;
import com.upload.aliyun.util.JavascriptUtil;
import com.upload.aliyun.util.NetWorkUtil;
import com.upload.aliyun.util.OSSUploadUtil;
import com.upload.aliyun.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class MusicListThread implements Runnable {
	private String threadName;
	private String bookName;
	private List<File> bookFiles;
	private String _add_;
	private String imgUrl= "";
	private MP3Info mp3Info = null;

	public MusicListThread(String bookName, List<File> bookFiles) {
		if(bookName != null){
			int index = bookName.lastIndexOf(File.separator) + 1;
			this.bookName = new String(bookName.substring(index));
		}
		this.bookFiles = bookFiles;
		threadName = "BooKListThread_" + bookName;
		_add_ ="";
	}

	@Override
	public void run() {
		saveBook();
		if(saveTheBook()){
			saveBook();
		}
	}

	public void saveBook() {
		if (bookFiles == null) {
			return;
		}
		for (File file : bookFiles) {
			String key = file.getAbsolutePath();
			key = key.replace(MusicConstants.BASE_FILE_PATH + File.separator, "");
			key = key.replace(File.separator, "/");
			if (file.exists()) {
				String fileName = file.getName();
				saveData(fileName,key, file);
			} else {
				System.out.println(key + "\t此文件已被移动......");
			}
		}
	}

	public void saveData(String name,String key,File file) {
		key = MusicConstants.SERVER_PATH_ROOT + key;
		boolean flag = OSSUploadUtil.isObjectExist(MusicConstants.BUKET_NAME, key);
		String url = MusicConstants.getUrl(key);
		if (!flag) {
			FileDoUtil.outLog("[ " + StringUtil.getFormateDate() + " ]"  + file.getAbsolutePath() + "   服务器此文件不存在");
			return;
		}
		if(key.endsWith(".jpg")){
			imgUrl = url;
			return;
		}else if(key.endsWith(".mp3")){
			mp3Info = MP3Util.getMP3Info(file.getAbsolutePath());
		}
		System.out.println(name + "\t" + url + "\t" +key);
		String rus = saveBookFile(name, url);
		String bookId = JavascriptUtil.getSaveBookResponse(rus);
		addBookId(bookId);
	}
	
	private void addBookId(String bookId){
		_add_+= bookId+ ",";
	}
	
	private String saveBookFile(String name,String url){
		Map<String, String> m = new HashMap<String, String>();
		
		if(mp3Info != null && mp3Info.getSongTitle() != null && !"".equals(mp3Info.getSongTitle().trim())){
			m.put("name",mp3Info.getSongTitle());
			m.put("singer", mp3Info.getArtist());
			m.put("lyric", mp3Info.getSongLyric());
			m.put("timestate", "");
			m.put("coderate", "");
		}else{
			int index = name.lastIndexOf(".");
			name = new String(name.substring(0,index));
			m.put("name",name);
		}
		m.put("url", url);
		return NetWorkUtil.doPost(MusicConstants.URL_SAVE_DATA_SONG, m, NetWorkUtil.ENCODE);
	}
	
	private boolean saveTheBook(){
		Map<String, String> m = new HashMap<String, String>();
		MusicDataInfo song = MusicDataGetFromExcel.getObjFormList(bookName);
		m.put("name",bookName);
		m.put("_add_", _add_);
		m.put("img", imgUrl);
		m.put("desc", song.getDesc());
		m.put("enverionment", song.getEnverionment());//环境
		m.put("category", song.getCategory());//心情
		m.put("times", GetMusicTypeFromExcel.getTimes(song.getEnverionment()));//心情
		String res = NetWorkUtil.doPost(MusicConstants.URL_SAVE_DATA_SONG_LIST, m, NetWorkUtil.ENCODE);
		String bookId = JavascriptUtil.getSaveBookResponse(res);
		if("success".equalsIgnoreCase(bookId)){
			return true;
		}
		return false;
	}

}
