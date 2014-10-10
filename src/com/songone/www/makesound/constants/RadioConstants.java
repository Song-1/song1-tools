/**
 * 
 */
package com.songone.www.makesound.constants;

/**
 * 电台相关常量
 * @author Jelly.Liu
 *
 */
public class RadioConstants {
	
	//// 电台小图的URL前缀
	public static final String RADIO_U_THUMB_PREFIX = "http://cdn.lizhi.fm/portrait/";
	//// 电台封面的URL前缀
	public static final String RADIO_COVER_PREFIX = "http://cdn.lizhi.fm/radio_cover/";
	//// 电台下面节目封面的URL前缀
	public static final String AUDIO_COVER_PREFIX = "http://cdn.lizhi.fm/audio_cover/";
	////
	public static final String AUDIO_URL_PREFIX = "http://cdn.lizhi.fm/audio/";
	
	//// 请求电台分类的URL
	public static final String RADIO_TAGS_API = "http://www.lizhi.fm/api/tags";
	//// 请求电台分类下面的电台的URL
	public static final String LIST_TAGS_RADIO_API = "http://www.lizhi.fm/api/tag/#typeid#/#page#";
	//// 请求电台数据的URL
	public static final String RADIO_API = "http://www.lizhi.fm/api/radio?flag=15&band=#band#";
	//// 请求电台下面节目的数据的URL
	public static final String RADIO_AUDIO_API = "http://www.lizhi.fm/api/radio_audios?flag=15&band=#band#&l=#au_cnt#";
	
	//// 保存造音社节目单分类的API接口
	public static final String MAKE_SOUND_CATEGORY_SYNC_API = "/api/makesound/v1/save/category";
	//// 保存造音社节目单的API接口
	public static final String MAKE_SOUND_PROGRAM_LIST_SYNC_API = "/api/makesound/v1/save/programlist";
	//// 保存造音社节目的API接口
	public static final String MAKE_SOUND_PROGRAM_API = "/api/makesound/v1/save/program";
	
	//// 阿里云保存电台封面
	public static final String ALIYUN_SAVE_RADIO_COVER = "makesound/radio_covers/";
	//// 阿里云保存电台音频封面
	public static final String ALIYUN_SAVE_AUDIO_COVER = "makesound/audio_covers/";
	//// 阿里云保存电台小图
	public static final String ALIYUN_SAVE_RADIO_UTHUMB = "makesound/u_thumbs/";
	//// 阿里云保存电台音频文件
	public static final String ALIYUN_SAVE_RADIO_AUDIOS = "makesound/audios/";
	

}
