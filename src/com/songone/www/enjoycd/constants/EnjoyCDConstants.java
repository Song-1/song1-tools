/**
 * 
 */
package com.songone.www.enjoycd.constants;

import com.songone.www.base.utils.BaseConstants;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class EnjoyCDConstants {
	public static String SONG_ONE_API_VERSION = "v1";

	public static long TIME_INTERVAL_TIMES_UNIT = 60 * 1000L; // ms(1分钟)
	public static long TIME_INTERVAL_TIMES = 60 * TIME_INTERVAL_TIMES_UNIT; // ms(1小时)

	public static String ENJOY_CD_LOCAL_FILE_PATH = BaseConstants.getConstantsPropertyValue("enjoy.cd.local.file.path");
	public static String ENJOY_CD_ALIYUN_BASE_KEY = BaseConstants.getConstantsPropertyValue("enjoy.cd.aliyun.base.key");

	public static String SONG_ONE_ENJOY_API_ADD_SONG = "/api/enjoy/" + SONG_ONE_API_VERSION + "/addalbumsong";
	public static String SONG_ONE_ENJOY_API_ADD_SINGER = "/api/enjoy/" + SONG_ONE_API_VERSION + "/addsinger";
	public static String SONG_ONE_ENJOY_API_ADD_STYLE = "/api/enjoy/" + SONG_ONE_API_VERSION + "/addalbumstyle";
	public static String SONG_ONE_ENJOY_API_ADD_ALBUM = "/api/enjoy/" + SONG_ONE_API_VERSION + "/addalbum";
	
	/**
	 * 同步整个专辑数据的API接口
	 */
	public static String SONG_ONE_ENJOY_API_SYNC_ALBUM = "/api/enjoy/sync/" + SONG_ONE_API_VERSION + "/album";

	public static final String[] ALBUM_SONG_STUFFIXS = { ".ape", ".wav", ".flac", ".mp3" };

}
