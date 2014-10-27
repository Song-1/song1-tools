/**
 * 
 */
package com.songone.www.makesound.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.songone.www.aliyun.AliyunUtil;
import com.songone.www.base.model.BaseResultBean;
import com.songone.www.base.model.HttpResponseData;
import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.CacheUtil;
import com.songone.www.base.utils.HttpClientUtil;
import com.songone.www.base.utils.StringUtil;
import com.songone.www.makesound.constants.RadioConstants;
import com.songone.www.makesound.model.MakesoundSyncRadioModel;
import com.songone.www.makesound.model.Radio;
import com.songone.www.makesound.model.RadioAudio;
import com.songone.www.makesound.model.RadioDataListBtTag;
import com.songone.www.makesound.model.RadioTag;
import com.songone.www.makesound.service.RadioAudioService;
import com.songone.www.makesound.service.SyncService;
import com.songone.www.makesound.service.impl.RadioAudioServiceImpl;
import com.songone.www.makesound.service.impl.SyncServiceImpl;

/**
 * 同步荔枝FM电台相关数据
 * 
 * @author Jelly.Liu
 *
 */
public class RadioDataSyncUtil {
	private static final Logger logger = LogManager.getLogger(RadioDataSyncUtil.class);

	private static SyncService syncService = new SyncServiceImpl();
	private static RadioAudioService radioAudioService = new RadioAudioServiceImpl();

	public static int PAGE_SIZE_FOR_SELECT = 100;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	/**
	 * 获取荔枝FM的所有电台分类
	 * 
	 * @return List<RadioTag>
	 */
	public static List<RadioTag> getRadioTagsData() {
		try {
			Map<String, List<RadioTag>> bean = new HashMap<String, List<RadioTag>>();
			Type gsonType = new TypeToken<Map<String, List<RadioTag>>>() {
			}.getType();
			Gson gson = new Gson();
			String json = null;
			HttpResponseData responseData = HttpClientUtil.doGet(RadioConstants.RADIO_TAGS_API);
			if(responseData != null && responseData.getCode() == HttpStatus.SC_OK){
				json = responseData.getData();
			}
			bean = gson.fromJson(json, gsonType);
			List<RadioTag> tags = bean.get("tagClz");
			return tags;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 根据荔枝FM电台分类遍历电台数据
	 * 
	 * @param tag
	 */
	public static void getRadioDatasByTag(RadioTag tag) {
		if (tag == null) {
			return;
		}
		logger.debug(" 获取荔枝FM[" + tag.getName() + "]下的电台数据并同步到音乐一号服务器   [开始] ");
		try {
			int page = 1;
			int tagId = tag.getId();
			while (true) {
				boolean flag = true;
				List<Radio> radios = gettRadioDatasByTag(tagId, page);
				if (radios != null) {
					for (Radio radio : radios) {
						radio = getRadioData(radio);
						if (radio != null) {
							syncRadioData(radio, tag.getMakeSoundCategoryId());
						}
					}
					flag = false;
				}
				page += 1;
				if (flag) {
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.debug(" 获取荔枝FM[" + tag.getName() + "]下的电台数据并同步到音乐一号服务器   [结束] ");
	}

	public static List<Radio> gettRadioDatasByTag(int tagId, int page) {
		List<Radio> radios = null;
		try {
			String url = RadioConstants.LIST_TAGS_RADIO_API;
			url = url.replace("#typeid#", tagId + "");
			url = url.replace("#page#", page + "");
			RadioDataListBtTag bean = new RadioDataListBtTag();
			Type gsonType = new TypeToken<RadioDataListBtTag>() {
			}.getType();
			Gson gson = new Gson();
			String json = null;
			HttpResponseData responseData = HttpClientUtil.doGet(url);
			if(responseData != null && responseData.getCode() == HttpStatus.SC_OK){
				json = responseData.getData();
			}
			bean = gson.fromJson(json, gsonType);
			if (bean != null) {
				radios = bean.getRadios();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return radios;
	}

	/**
	 * 获取电台的详细数据
	 * 
	 * @param radio
	 * @return
	 */
	public static Radio getRadioData(Radio radio) {
		if (radio == null) {
			return null;
		}
		try {
			int band = radio.getBand();
			String url = RadioConstants.RADIO_API;
			url = url.replace("#band#", band + "");
			Radio r = new Radio();
			Type gsonType = new TypeToken<Radio>() {
			}.getType();
			Gson gson = new Gson();
			String json = null;
			HttpResponseData responseData = HttpClientUtil.doGet(url);
			if(responseData != null && responseData.getCode() == HttpStatus.SC_OK){
				json = responseData.getData();
			}
			r = gson.fromJson(json, gsonType);
			if (r != null) {
				radio = r;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return radio;
	}
	/**
	 * 获取电台的详细数据
	 * 
	 * @param radio
	 * @return
	 */
	public static Radio getRadioData(String band) {
		if (StringUtil.isEmptyString(band)) {
			return null;
		}
		try {
			String url = RadioConstants.RADIO_API;
			url = url.replace("#band#", band );
			Radio r = new Radio();
			Type gsonType = new TypeToken<Radio>() {
			}.getType();
			Gson gson = new Gson();
			String json = null;
			HttpResponseData responseData = HttpClientUtil.doGet(url);
			if(responseData != null && responseData.getCode() == HttpStatus.SC_OK){
				json = responseData.getData();
			}
			r = gson.fromJson(json, gsonType);
			if (r != null) {
				return r;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获取电台下面的音频数据
	 * 
	 * @param band
	 * @param cu_cnt
	 * @return List<RadioAudio>
	 */
	public static List<RadioAudio> getRadioAudioDatas(String band, int cu_cnt) {
		if (StringUtil.isEmptyString(band)) {
			return null;
		} else if (cu_cnt <= 0) {
			return null;
		}
		try {
			String url = RadioConstants.RADIO_AUDIO_API;
			url = url.replace("#band#", band + "");
			url = url.replace("#au_cnt#", cu_cnt + "");
			List<RadioAudio> bean = new ArrayList<RadioAudio>();
			Type gsonType = new TypeToken<List<RadioAudio>>() {
			}.getType();
			Gson gson = new Gson();
			String json = null;
			HttpResponseData responseData = HttpClientUtil.doGet(url);
			if(responseData != null && responseData.getCode() == HttpStatus.SC_OK){
				json = responseData.getData();
			}
			bean = gson.fromJson(json, gsonType);
			if (bean != null && !bean.isEmpty()) {
				return bean;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 批量同步造音社节目单分类数据
	 * 
	 * @param tags
	 */
	public static void syncCategoryDatas(List<RadioTag> tags) {
		if (tags == null) {
			return;
		}
		for (RadioTag tag : tags) {
			if (tag == null) {
				continue;
			}
			syncCategoryDatas(tag, null);
			List<RadioTag> radioTags = tag.getTags();
			if(radioTags == null){
				continue;
			}
			for (RadioTag radioTag : radioTags) {
				syncCategoryDatas(radioTag, tag.getMakeSoundCategoryCode());
			}
		}
		CacheUtil.addElement("LIST_RADIOTAG", tags);
	}

	/**
	 * 同步造音社节目单分类数据
	 * 
	 * @param tag
	 * @param parentCode
	 */
	public static void syncCategoryDatas(RadioTag tag, String parentCode) {
		if (tag == null) {
			return;
		}
		try {
			logger.debug("同步电台分类数据"+tag);
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", tag.getName());
			params.put("parentCode", parentCode);
			String url = BaseConstants.PROTOCOL + BaseConstants.SONG_ONE_SERVER_HOST + RadioConstants.MAKE_SOUND_CATEGORY_SYNC_API;
			BaseResultBean<Map<String, String>> bean = new BaseResultBean<Map<String, String>>();
			Type gsonType = new TypeToken<BaseResultBean<Map<String, String>>>() {
			}.getType();
			Gson gson = new Gson();
			String json = null;
			HttpResponseData responseData = HttpClientUtil.doPost(url, params);
			if(responseData != null && responseData.getCode() == HttpStatus.SC_OK){
				json = responseData.getData();
			}
			bean = gson.fromJson(json, gsonType);
			if ("1000".equals(bean.getStatus())) {
				Map<String, String> map = bean.getData();
				if (map != null) {
					tag.setMakeSoundCategoryId(map.get("id"));
					tag.setMakeSoundCategoryCode(map.get("code"));
				}
			} else {
				logger.debug("同步造音社节目单分类数据失败[分类名称::" + tag.getName() + "]");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 同步造音社节目单数据
	 * 
	 * @param radio
	 * @param categoryId
	 */
	public static void syncProgramListDatas(Radio radio, String categoryId, boolean isReSend) {
		if (radio == null) {
			return;
		}
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", radio.getName());
			params.put("_add_", categoryId);
			params.put("band", "" + radio.getBand());
			params.put("userName", radio.getUser_name());
			params.put("description", radio.getDesc());
			params.put("cover", uploadFileToAliyun(BaseConstants.ALIYUN_BUCKET, radio.getCoverKey(), radio.getCover()));
			params.put("uthumb", uploadFileToAliyun(BaseConstants.ALIYUN_BUCKET, radio.getU_thumbKey(), radio.getU_thumb()));
			String url = BaseConstants.PROTOCOL + BaseConstants.SONG_ONE_SERVER_HOST + RadioConstants.MAKE_SOUND_PROGRAM_LIST_SYNC_API;
			BaseResultBean<Map<String, Integer>> bean = new BaseResultBean<Map<String, Integer>>();
			Type gsonType = new TypeToken<BaseResultBean<Map<String, Integer>>>() {
			}.getType();
			Gson gson = new Gson();
			String json = null;
			HttpResponseData responseData = HttpClientUtil.doPost(url, params);
			if(responseData != null && responseData.getCode() == HttpStatus.SC_OK){
				json = responseData.getData();
			}
			logger.debug("返回的json数据:::" + json);
			bean = gson.fromJson(json, gsonType);
			if ("1000".equals(bean.getStatus())) {
				Map<String, Integer> map = bean.getData();
				if (map != null) {
					int id = map.get("id") == null ? 0 : map.get("id").intValue();
					int programCounts = map.get("programCounts") == null ? 0 : map.get("programCounts").intValue();
					radio.setProgramListId(id);
					radio.setProgramCounts(programCounts);
				}
				logger.debug("电台[名称::" + radio.getName() + ",band::" + radio.getBand() + "]数据 同步到音乐一号服务器  成功");
				return;
			} else {
				logger.error("同步造音社节目单数据失败[名称::" + radio.getName() + "]");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 同步造音社节目数据
	 * 
	 * @param audio
	 * @param radioBand
	 * @param radioName
	 */
	public static boolean syncProgramDatas(RadioAudio audio, int programListId, String radioName) {
		if (audio == null) {
			return false;
		}
		try {
			logger.debug("同步造音社节目数据[名称::" + audio.getName() + "] .............");
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", audio.getName());
			params.put("listId", "" + programListId);
			params.put("duration", "" + audio.getDuration());
			params.put("createtime", "" + audio.getCreate_time());
			params.put("cover", uploadFileToAliyun(BaseConstants.ALIYUN_BUCKET, audio.getCoverKey(), audio.getCover()));
			if (!StringUtil.isEmptyString(audio.getUrl())) {
				String url = audio.getUrl();
				String name = audio.getName();
				name = name.replace("/", "");
				String stuffix = new String(url.substring(url.lastIndexOf(".")));
				name = RadioConstants.ALIYUN_SAVE_RADIO_AUDIOS + radioName + "/" + name + stuffix;
				String key = uploadFileToAliyun(BaseConstants.ALIYUN_BUCKET, name, audio.getUrl());
				params.put("url", key);
			}
			String url = BaseConstants.PROTOCOL + BaseConstants.SONG_ONE_SERVER_HOST + RadioConstants.MAKE_SOUND_PROGRAM_API;
			BaseResultBean<String> bean = new BaseResultBean<String>();
			Type gsonType = new TypeToken<BaseResultBean<String>>() {
			}.getType();
			Gson gson = new Gson();
			String json = null;
			HttpResponseData responseData = HttpClientUtil.doPost(url, params);
			if(responseData != null && responseData.getCode() == HttpStatus.SC_OK){
				json = responseData.getData();
			}
			bean = gson.fromJson(json, gsonType);
			if ("1000".equals(bean.getStatus())) {
				return true;
			} else {
				logger.debug("同步造音社节目数据失败[名称::" + audio.getName() + "]");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 将网络文件保存到本地并上传到阿里云
	 * 
	 * @param bucket
	 * @param key
	 * @param url
	 * @return
	 */
	public static String uploadFileToAliyun(String bucket, String key, String url) {
		try {
			if (AliyunUtil.isExistObjectForTheKey(bucket, key)) {
				return key;
			}
			String tempFilePath = HttpClientUtil.saveFileByURL(url, null);
			if (!StringUtil.isEmptyString(tempFilePath)) {
				File file = new File(tempFilePath);
				if (file.exists()) {
					AliyunUtil.uploadBigFile(bucket, key, file);
					file.delete();
					return key;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获取已处理的电台分类数据集合
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<RadioTag> getRadioTags() {
		List<RadioTag> list = (List<RadioTag>) CacheUtil.getElement("LIST_RADIOTAG");
		if (list == null) {
			list = getRadioTagsData();
			syncCategoryDatas(list);
			CacheUtil.addElement("LIST_RADIOTAG", list);
		}
		return list;
	}

	/**
	 * 保存并同步电台数据到服务器
	 * 
	 * @param radio
	 * @param categoryId
	 */
	public static void syncRadioData(Radio radio, String categoryId) {
		MakesoundSyncRadioModel model = syncService.listByBand(radio.getBand() + "");
		if (model != null && model.getProgramListId() > 0) {
			logger.debug("电台[" + radio.getBand() + "]数据 音乐一号服务器已存在");
			return;
		}
		syncProgramListDatas(radio, categoryId, false);
		model = new MakesoundSyncRadioModel();
		model.setBand(radio.getBand() + "");
		model.setAudios(radio.getAu_cnt());
		model.setProgramListId(radio.getProgramListId());
		model.setProgramCounts(radio.getProgramCounts());
		model.setProgramListName(radio.getName());
		syncService.save(model);
	}

	/**
	 * 同步电台音频数据
	 */
	public static void syncAudioDatas() {
		int start = 0;
		while (true) {
			List<MakesoundSyncRadioModel> radios = syncService.listSyncAudioDatas(start, PAGE_SIZE_FOR_SELECT);
			if (radios == null) {
				break;
			}
			start += PAGE_SIZE_FOR_SELECT;
			for (MakesoundSyncRadioModel model : radios) {
				if (model == null) {
					continue;
				}
				List<RadioAudio> audios = getRadioAudioDatas(model.getBand(), model.getAudios());
				if (audios == null) {
					continue;
				}
				int programListId = model.getProgramListId();
				String radioName = model.getProgramListName();
				int programCounts = 0;
				logger.debug(" 同步电台[" + radioName + "]的音频数据到音乐一号服务器  开始  ");
				for (RadioAudio audio : audios) {
					if (syncProgramDatas(audio, programListId, radioName)) {
						programCounts += 1;
					}
				}
				model.setProgramCounts(programCounts);
				syncService.save(model);
				logger.debug(" 同步电台[" + radioName + "]的音频数据到音乐一号服务器  结束  ");
			}
		}
	}

	public static void saveRadioAudioDatas() {
		List<RadioTag> tags = getRadioTagsData();
		if (tags == null) {
			return;
		}
		for (RadioTag tag : tags) {
			List<RadioTag> radioTags = tag.getTags();
			if (radioTags == null) {
				continue;
			}
			for (RadioTag radioTag : radioTags) {
				if (radioTag == null) {
					continue;
				}
				eachRadioDatasByTag(radioTag.getId());
			}
		}
	}

	public static void eachRadioDatasByTag(int tagId) {
		try {
			int page = 1;
			while (true) {
				List<Radio> radios = gettRadioDatasByTag(tagId, page);
				if (radios == null) {
					break;
				}
				for (Radio radio : radios) {
					radio = getRadioData(radio);
					if (radio == null) {
						continue;
					}
					String band = radio.getBand() + "";
					String radioName = radio.getName();
					List<RadioAudio> audios = getRadioAudioDatas(band, radio.getAu_cnt());
					if (audios == null) {
						continue;
					}
					for (RadioAudio audio : audios) {
						if (audio == null) {
							continue;
						}
						audio.setBand(band);
						audio.setRadioName(radioName);
						logger.debug("保存电台[" + radioName + "]音频文件[" + audio.getName() + "]");
						boolean flag = radioAudioService.saveOrUpdate(audio);
						if (flag) {
							continue;
						}
						logger.debug("保存电台[" + radioName + "]音频文件[" + audio.getName() + "]......失败");
						logger.debug("保存电台[" + radioName + "]音频文件[" + audio.getName() + "]......格式化电台音频文件名称保存");
						audio.setName(formateStr(audio.getName()));
						flag = radioAudioService.saveOrUpdate(audio);
						if (flag) {
							continue;
						}
						logger.debug("保存电台[" + radioName + "]音频文件[" + audio.getName() + "]......格式化电台音频文件名称保存......失败");
						logger.debug("保存电台[" + radioName + "]音频文件[" + audio.getName() + "]......格式化电台名称保存");
						radioName = formateStr(radioName);
						audio.setRadioName(radioName);
						flag = radioAudioService.saveOrUpdate(audio);
						if(!flag){
							logger.debug("保存电台[" + radioName + "]音频文件[" + audio.getName() + "]......格式化电台名称保存......失败");
						}
					}

				}
				page += 1;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 格式化字符串编码
	 * 
	 * @param text
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String formateStr(String text) throws UnsupportedEncodingException {
		byte[] bytes = text.getBytes("UTF-8");
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		int i = 0;
		int lastI = 0;
		while (i < bytes.length) {
			lastI = i;
			short b = bytes[i];
			if (b > 0) {
				buffer.put(bytes[i++]);
				continue;
			}
			b += 256;
			if ((b ^ 0xC0) >> 4 == 0) {
				buffer.put(bytes, i, 2);
				i += 2;
			} else if ((b ^ 0xE0) >> 4 == 0) {
				buffer.put(bytes, i, 3);
				i += 3;
			} else if ((b ^ 0xF0) >> 4 == 0) {
				i += 4;
			}
			if (i == lastI) {
				return "";
			}
		}
		buffer.flip();
		return new String(buffer.array(), "utf-8");
	}

}
