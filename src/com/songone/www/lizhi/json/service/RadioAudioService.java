/**
 * 
 */
package com.songone.www.lizhi.json.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.songone.www.base.db.mybatis.MybatisUtil;
import com.songone.www.base.db.mybatis.PageResultModel;
import com.songone.www.base.model.HttpResponseData;
import com.songone.www.base.service.BaseService;
import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.HttpClientUtil;
import com.songone.www.base.utils.StringUtil;
import com.songone.www.lizhi.json.constans.ThreadConstans;
import com.songone.www.lizhi.json.dao.mybatis.RadioAudioDao;
import com.songone.www.lizhi.json.exceptions.FormateNameException;
import com.songone.www.lizhi.json.model.RadioAudio;
import com.songone.www.makesound.constants.RadioConstants;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class RadioAudioService extends BaseService<RadioAudio> {
	private static final Logger logger = LogManager.getLogger(RadioAudioService.class);
	private UploadToAliyunDataModelService uploadToAliyunDataModelService = new UploadToAliyunDataModelService();

	@SuppressWarnings("unchecked")
	@Override
	public Class<RadioAudioDao> getRealDaoClass() {
		return RadioAudioDao.class;
	}

	@SuppressWarnings("unchecked")
	public RadioAudioDao getDao(SqlSession session) {
		if (session == null) {
			return null;
		}
		return session.getMapper(RadioAudioDao.class);
	}

	public void updateSyncFlag(RadioAudio audio) {
		try {
			executeDao("updateSyncFlag", audio);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取同步的电台节目数据(可分页)
	 * 
	 * @param start
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageResultModel<RadioAudio> listAudioByPageForSync(int start, int page) {
		PageResultModel<RadioAudio> result = new PageResultModel<RadioAudio>();
		if (start < 0) {
			return result;
		} else if (page < 0 || page > 1000) {
			page = BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE;
		}
		result.setStart(start);
		result.setPageSize(page);
		try {
			List<RadioAudio> lists = (List<RadioAudio>) executeDao("queryModelForSync", result);
			result.setDatas(lists);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 根据ID查询数据对象
	 * 
	 * @param id
	 * @return
	 */
	public RadioAudio getRadioById(long id) {
		RadioAudio result = null;
		if (id <= 0) {
			return result;
		}
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return result;
		}
		try {
			result = getDao(session).queryModelById(id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			if (session != null)
				session.close();
		}
		return result;
	}

	/**
	 * 根据电台的band查询节目总数
	 * 
	 * @param id
	 * @return
	 */
	public int listAudioCountsByBand(int band) {
		int result = 0;
		if (band <= 0) {
			return result;
		}
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return result;
		}
		try {
			result = getDao(session).queryModelByBandCounts(band);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			if (session != null)
				session.close();
		}
		return result;
	}

	/**
	 * 保存从荔枝FM获取电台数据到本地数据库
	 * 
	 * @param radio
	 * @return
	 */
	public boolean save(RadioAudio model) throws FormateNameException {
		boolean result = false;
		if (model == null) {
			return result;
		} else if (model.getId() <= 0) {
			return result;
		}
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return result;
		}
		try {
			RadioAudio r = getRadioById(model.getId());
			if (r == null) {
				getAudioKey(model.getUrl(), model.getName(), model.getRadioName());
				uploadToAliyunDataModelService.save(model.getCover(), model.getCoverKey());
				logger.debug("保存电台节目数据:::" + model);
				getDao(session).add(model);
				session.commit();
			}
			result = true;
		} catch (Exception e) {
			String dMessage = null;
			Throwable cause = e.getCause();
			if (cause != null) {
				dMessage = cause.getMessage();
			} else {
				dMessage = e.getMessage();
			}
			if (StringUtil.isEmptyString(dMessage)) {
				dMessage = "";
			}
			if (dMessage.indexOf(ThreadConstans.ERROR_NAME_VALUE_MUST_FORMATE) > 0) {
				throw new FormateNameException(e);
			} else {
				e.printStackTrace();
				logger.error(dMessage, e);
			}
		} finally {
			if (session != null)
				session.close();
		}
		return result;
	}

	/**
	 * 获取电台下面的音频数据
	 * 
	 * @param band
	 * @param cu_cnt
	 * @return List<RadioAudio>
	 */
	public List<RadioAudio> getRadioAudioDatas(String band, int cu_cnt) {
		if (StringUtil.isEmptyString(band)) {
			return null;
		} else if (cu_cnt <= 0) {
			return null;
		}
		try {
			String url = RadioConstants.RADIO_AUDIO_API;
			url = url.replace("#band#", band + "");
			url = url.replace("#au_cnt#", cu_cnt + "");
			String json = null;
			HttpResponseData responseData = HttpClientUtil.doGet(url);
			if(responseData != null && responseData.getCode() == HttpStatus.SC_OK){
				json = responseData.getData();
			}
			if (StringUtil.isEmptyString(json)) {
				return null;
			} else if (!json.startsWith("[{")) {
				return null;
			}
			List<RadioAudio> bean = new ArrayList<RadioAudio>();
			Type gsonType = new TypeToken<List<RadioAudio>>() {
			}.getType();
			Gson gson = new Gson();
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
	 * 同步造音社节目数据
	 * 
	 * @param audio
	 * @param radioBand
	 * @param radioName
	 */
	public boolean syncAudioData(RadioAudio audio) {
		boolean result = false;
		if (audio == null) {
			return result;
		}
		try {
			logger.debug("同步造音社节目数据[名称::" + audio.getName() + "] .............");
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", audio.getName());
			params.put("band", "" + audio.getBand());
			params.put("duration", "" + audio.getDuration());
			params.put("createtime", "" + audio.getCreate_time());
			params.put("cover", audio.getCoverKey());
			params.put("url", getAudioKey(audio.getUrl(), audio.getName(), audio.getRadioName()));
			uploadToAliyunDataModelService.save(audio.getCover(), audio.getCoverKey());
			String url = BaseConstants.PROTOCOL + BaseConstants.SONG_ONE_SERVER_HOST + RadioConstants.MAKE_SOUND_PROGRAM_API;
			String json = null;
			HttpResponseData responseData = HttpClientUtil.doPost(url, params);
			if(responseData != null && responseData.getCode() == HttpStatus.SC_OK){
				json = responseData.getData();
			}
			logger.debug(json);
			if (!StringUtil.isEmptyString(json) && json.indexOf(BaseConstants.SONG_ONE_API_RETUEN_STATUS_SUCCESS) > 0) {
				result = true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	private String getAudioKey(String url, String audioName, String radioName) {
		if (StringUtil.isEmptyString(url)) {
			return null;
		}
		String name = audioName;
		name = name.replace("/", "");
		String stuffix = new String(url.substring(url.lastIndexOf(".")));
		name = RadioConstants.ALIYUN_SAVE_RADIO_AUDIOS + radioName + "/" + name + stuffix;
		name = uploadToAliyunDataModelService.save(url, name);
		return name;
	}

}
