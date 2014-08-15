/**
 * 
 */
package com.tools.song1.runnable.enjoy;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

import com.tools.song1.util.FileDoUtil;
import com.tools.song1.util.ImageFileUtil;
import com.tools.song1.util.JavascriptUtil;
import com.tools.song1.util.NetWorkUtil;
import com.tools.song1.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class EnjoyThread {
	public static String BASEPATH = "http://localhost:8080/song1/api/enjoy/v1/";
	public static final String SONGFILEREG = "(\\d+)\\s+-{1}\\s+(.+[.])";
	public static String LOCAL_FILE_BASE_PATH = "";
	public static String ALIYUN_SERVER_PATH_ROOT = "";
	public static String ALIYUN_BUKET_NAME = "";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String songName = "00  -  Noelle's Theme....ape";
		Pattern p = Pattern.compile(SONGFILEREG);
		Matcher m = p.matcher(songName);
		if (m.find()) {
			String seatStr = m.group(1).trim();
			String name = m.group(2).trim();
			name = new String(name.substring(0, name.lastIndexOf(".")));
			System.out.println(seatStr);
			System.out.println(name);
		}

	}

	// // 专辑歌曲
	private static String saveAlbumSongs(Map<String, File> songFiles) {
		FileDoUtil.outLog(" [保存] 专辑歌曲");
		String addId = "";
		String funcName = "addalbumsong";
		if (songFiles != null) {
			for (Map.Entry<String, File> entry : songFiles.entrySet()) {
				File file = entry.getValue();
				if (file == null) {
					continue;
				} else if (!file.exists()) {
					FileDoUtil.outLog("文件[ " + file.getAbsolutePath() + " ] 被移动,请检查文件");
				}
				Map<String, String> map = new HashMap<String, String>();
				String songName = file.getName();
				Pattern p = Pattern.compile(SONGFILEREG);
				Matcher m = p.matcher(songName);
				if (m.find()) {
					String seatStr = m.group(1).trim();
					String name = m.group(2).trim();
					name = new String(name.substring(0, name.lastIndexOf(".")));
					map.put("name", name);
					// map.put("timeState", "04:39");
					// map.put("lyric", "test");
					 map.put("url", entry.getKey());
					// map.put("codeRate", "128");
					map.put("seat", Integer.parseInt(seatStr) + "");
					String content = NetWorkUtil.doPost(BASEPATH + funcName, map, NetWorkUtil.ENCODE);
					String ids = JavascriptUtil.getSaveEnjoyResponse(content);
					addId += ids + ",";
				}
			}
		}
		return addId;
	}

	// // 专辑所属歌手的所属地域
	private static String saveSingerType(String singerTypeName) {
		if (StringUtil.isEmptyString(singerTypeName)) {
			return null;
		}
		FileDoUtil.outLog(" [保存] 专辑所属歌手的所属地域");
		String id = null;
		String funcName = "addsingertype";
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", singerTypeName);
		String content = NetWorkUtil.doPost(BASEPATH + funcName, map, NetWorkUtil.ENCODE);
		id = JavascriptUtil.getSaveEnjoyResponse(content);
		return id;
	}

	// // 专辑所属歌手
	private static String saveSinger(String typeId, String singerName) {
		if (StringUtil.isEmptyString(singerName)) {
			return null;
		}
		FileDoUtil.outLog(" [保存] 专辑所属歌手");
		String id = null;
		String funcName = "addsinger";
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", singerName);
		// map.put("img", "华语男歌手");
		// map.put("description", "华语男歌手");
		map.put("singerTypeCode", typeId);
		String content = NetWorkUtil.doPost(BASEPATH + funcName, map, NetWorkUtil.ENCODE);
		id = JavascriptUtil.getSaveEnjoyResponse(content);
		return id;
	}

	// // 专辑所属风格
	private static String saveAlbumStyle(String style) {
		if (StringUtil.isEmptyString(style)) {
			return null;
		}
		FileDoUtil.outLog(" [保存] 专辑所属风格");
		String id = null;
		String funcName = "addalbumstyle";
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", style);
		String content = NetWorkUtil.doPost(BASEPATH + funcName, map, NetWorkUtil.ENCODE);
		id = JavascriptUtil.getSaveEnjoyResponse(content);
		return id;
	}

	// // 专辑
	public static String saveAlbum(EnjoyAlbumData data) {
		if (data == null) {
			return null;
		} else if (StringUtil.isEmptyString(data.getAlbumName())) {
			return null;
		}
		String name = data.getAlbumName();
		FileDoUtil.outLog(" [保存] 专辑 :::" + name + " ==========  开始   =============   ");
		String singerTypeId = saveSingerType(GetSingerInfoDataFromExcel.getMappingValue(data.getSingerName()));
		String singerId = saveSinger(singerTypeId, data.getSingerName());
		String addIds = saveAlbumSongs(data.getAlbumSongs());
		String albumStyleId = saveAlbumStyle(data.getStyleName());
		List<File> images = data.getAlbumImages();
		String imageURL = null;
		String iconURL = null;
		if (images != null) {
			File image = null;
			for (File file : images) {
				String filename = file.getName();
				filename = new String(filename.substring(0, filename.lastIndexOf(".")));
				if (name.equals(filename)) {
					image = file;
					break;
				}
			}
			if (image == null && !images.isEmpty()) {
				image = images.get(0);
			}
			if (image != null) {
				imageURL = ImageFileUtil.cutImageAndUpload(ALIYUN_BUKET_NAME,ALIYUN_SERVER_PATH_ROOT, LOCAL_FILE_BASE_PATH, image, false);
				iconURL = ImageFileUtil.cutImageAndUpload(ALIYUN_BUKET_NAME,ALIYUN_SERVER_PATH_ROOT, LOCAL_FILE_BASE_PATH, image, true);
			}
		}
		String id = null;
		String funcName = "addalbum";
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		// map.put("description", "test albums");
		map.put("image", imageURL);
		map.put("icon", iconURL);
		map.put("_add_", addIds);
		// map.put("releaseTime", "2013年");
		map.put("styleId", albumStyleId);
		map.put("singerId", singerId);
		NetWorkUtil.doPost(BASEPATH + funcName, map, NetWorkUtil.ENCODE);
		FileDoUtil.outLog(" [保存] 专辑 :::" + name + " ==========  结束   =============");
		// String content = NetWorkUtil.doPost(BASEPATH + funcName, map,
		// NetWorkUtil.ENCODE);
		// id = JavascriptUtil.getSaveEnjoyResponse(content);
		return id;
	}
}
