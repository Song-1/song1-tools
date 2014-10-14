/**
 * 
 */
package com.songone.www.cherrytime.constants;

import com.songone.www.base.utils.BaseConstants;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class CherryTimeConstants {
	public static String SONG_ONE_API_VERSION = "v1";
	public static String CHERRY_TIME_LOCAL_FILE_PATH = BaseConstants.getConstantsPropertyValue("cherry.time.local.file.path");
	public static String CHERRY_TIME_ALIYUN_BASE_KEY = BaseConstants.getConstantsPropertyValue("cherry.time.aliyun.base.key");
	/**
	 * 同步整个歌单数据的API接口
	 */
	public static String SONG_ONE_CHERRYTIME_API_SYNC_SONGLIST = "/api/song/" + SONG_ONE_API_VERSION + "/sync/songlist";
}
