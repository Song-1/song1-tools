/**
 * 
 */
package com.songone.www.lizhi.json.service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.songone.www.base.db.mybatis.MybatisUtil;
import com.songone.www.base.db.mybatis.PageResultModel;
import com.songone.www.base.service.BaseService;
import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.HttpClientUtil;
import com.songone.www.base.utils.StringUtil;
import com.songone.www.lizhi.json.constans.ThreadConstans;
import com.songone.www.lizhi.json.dao.mybatis.RadioDao;
import com.songone.www.lizhi.json.exceptions.FormateDescException;
import com.songone.www.lizhi.json.exceptions.FormateNameException;
import com.songone.www.lizhi.json.exceptions.FormateUserNameException;
import com.songone.www.lizhi.json.model.Radio;
import com.songone.www.makesound.constants.RadioConstants;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class RadioService extends BaseService<Radio> {
	private static final Logger logger = LogManager.getLogger(RadioService.class);
	
	private UploadToAliyunDataModelService uploadToAliyunDataModelService = new UploadToAliyunDataModelService();

	@SuppressWarnings("unchecked")
	@Override
	public Class<RadioDao> getRealDaoClass() {
		return RadioDao.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RadioDao getDao(SqlSession session) {
		if (session != null) {
			return session.getMapper(RadioDao.class);
		}
		return null;
	}

	/**
	 * 获取没有同步到音乐一号服务器的电台数据的总数
	 * 
	 * @return
	 */
	public int queryModelByPageForSyncCounts(boolean syncFlag) {
		int result = 0;
		try {
			Integer obj = (Integer) executeDao("queryModelByPageForSyncCounts", syncFlag);
			result = obj == null ? 0 : obj.intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 获取没有同步到音乐一号服务器的电台数据的总数
	 * 
	 * @return
	 */
	public int getRadioCounts() {
		int result = 0;
		try {
			Integer obj = (Integer) executeDao("queryModelCounts");
			result = obj == null ? 0 : obj.intValue();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return result;
	}

	/**
	 * 获取没有同步到音乐一号服务器的电台数据的总数
	 * 
	 * @return
	 */
	public PageResultModel<Radio> queryModelByPageForSync(int start, int pageSize) {
		PageResultModel<Radio> result = listByPage("queryModelByPageForSync", start, pageSize);
		int counts = queryModelByPageForSyncCounts(false);
		result.setCounts(counts);
		return result;
	}
	/**
	 * 获取已经同步到音乐一号服务器的电台数据的总数
	 * 
	 * @return
	 */
	public PageResultModel<Radio> queryModelByPageForSyncAudio(int start, int pageSize) {
		PageResultModel<Radio> result = listByPage("queryModelByPageForSyncAudio", start, pageSize);
		int counts = queryModelByPageForSyncCounts(true);
		result.setCounts(counts);
		return result;
	}
	
	/**
	 * 获取电台数据的主键
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public List<Radio> listRadioIds(int start, int pageSize){
		PageResultModel<Radio> result = listByPage("queryModelIDByPage", start, pageSize);
		return result.getDatas();
	}
	

	/**
	 * 电台分页数据查询
	 * 
	 * @param methodName
	 * @param start
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private PageResultModel<Radio> listByPage(String methodName, int start, int pageSize) {
		PageResultModel<Radio> result = new PageResultModel<Radio>();
		if (StringUtil.isEmptyString(methodName)) {
			return result;
		} else if (start < 0) {
			return result;
		} else if (pageSize <= 0) {
			pageSize = result.getPageSize();
		}
		try {
			result.setStart(start);
			List<Radio> radios = (List<Radio>) executeDao(methodName, result);
			result.setDatas(radios);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 查询当前数据库最大的band
	 * 
	 * @return
	 */
	public int getCurrentMaxBand() {
		int result = 0;
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return result;
		}
		try {
			Integer re = getDao(session).queryCurrentMaxBand();
			if (re != null) {
				result = re.intValue();
			}
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
	 * 根据ID查询数据对象
	 * 
	 * @param id
	 * @return
	 */
	public Radio getRadioById(long id) {
		Radio result = null;
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
	 * 分页查询数据
	 * 
	 * @param id
	 * @return
	 */
	public PageResultModel<Radio> listRadioForPage(int start, int pageSize) {
		PageResultModel<Radio> result = new PageResultModel<Radio>();
		if (start < 0) {
			return result;
		} else if (pageSize <= 0) {
			pageSize = result.getPageSize();
		}
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return result;
		}
		try {
			RadioDao dao = getDao(session);
			int counts = dao.queryModelCounts();
			if (counts <= 0) {
				return result;
			} else if (start >= counts) {
				start = 0;
			}
			result.setStart(start);
			result.setCounts(counts);
			List<Radio> radios = dao.queryModelByPage(result);
			result.setDatas(radios);
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
	 * 分页查询数据
	 * 
	 * @param id
	 * @return
	 */
	public PageResultModel<Radio> listRadioByPageForDoAudio(int start, int pageSize) {
		PageResultModel<Radio> result = new PageResultModel<Radio>();
		if (start < 0) {
			return result;
		} else if (pageSize <= 0) {
			pageSize = result.getPageSize();
		}
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return result;
		}
		try {
			RadioDao dao = getDao(session);
			int counts = dao.queryModelByPageForAudioCounts();
			result.setCounts(counts);
			if (counts <= 0) {
				return result;
			} else if (start >= counts) {
				return result;
			} else {
				result.setStart(start);
				List<Radio> radios = dao.queryModelByPageForAudio(result);
				result.setDatas(radios);
			}
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
	 * 分页查询数据
	 * 
	 * @param id
	 * @return
	 */
	public PageResultModel<Radio> listRadioByPageForDoRadioAudio(int start, int pageSize) {
		PageResultModel<Radio> result = new PageResultModel<Radio>();
		if (start < 0) {
			return result;
		} else if (pageSize <= 0) {
			pageSize = result.getPageSize();
		}
		result.setPageSize(pageSize);
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return result;
		}
		try {
			RadioDao dao = getDao(session);
			int counts = dao.queryModelByPageForRadioAudioCounts();
			result.setCounts(counts);
			if (counts <= 0) {
				return result;
			} else if (start >= counts) {
				return result;
			} else {
				result.setStart(start);
				List<Radio> radios = dao.queryModelByPageForRadioAudio(result);
				result.setDatas(radios);
			}
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
	 * 分页查询数据
	 * 
	 * @param id
	 * @return
	 */
	public PageResultModel<Radio> listRadioByPageForDoSync(int start, int pageSize) {
		PageResultModel<Radio> result = new PageResultModel<Radio>();
		if (start < 0) {
			return result;
		} else if (pageSize <= 0) {
			pageSize = result.getPageSize();
		}
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return result;
		}
		try {
			RadioDao dao = getDao(session);
			int counts = dao.queryModelByPageForRadioAudioCounts();
			result.setCounts(counts);
			if (counts <= 0) {
				return result;
			} else if (start >= counts) {
				return result;
			} else {
				result.setStart(start);
				List<Radio> radios = dao.queryModelByPageForRadioAudio(result);
				result.setDatas(radios);
			}
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
	public boolean save(Radio radio) throws FormateUserNameException, FormateNameException, FormateDescException {
		boolean result = false;
		if (radio == null) {
			return result;
		} else if (radio.getId() <= 0) {
			return result;
		}
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return result;
		}
		try {
			uploadToAliyunDataModelService.save(radio.getCover(), radio.getCoverKey());
			uploadToAliyunDataModelService.save(radio.getU_thumb(), radio.getU_thumbKey());
			RadioDao dao = getDao(session);
			Radio r = dao.queryModelById(radio.getId());
			if (r == null) {
				logger.debug("保存电台数据:::" + radio);
				dao.add(radio);
			} else {
				radio.setGetSaveAudioFlag(false);
				radio.setSyncFlag(false);
				logger.debug("更新电台数据:::" + radio);
				dao.updateFlagAndAudios(radio);
			}
			session.commit();
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
			if (dMessage.indexOf(ThreadConstans.ERROR_USER_NAME_VALUE_MUST_FORMATE) > 0) {
				throw new FormateUserNameException(e);
			} else if (dMessage.indexOf(ThreadConstans.ERROR_NAME_VALUE_MUST_FORMATE) > 0) {
				throw new FormateNameException(e);
			} else if (dMessage.indexOf(ThreadConstans.ERROR_DESC_VALUE_MUST_FORMATE) > 0) {
				throw new FormateDescException(e);
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
	 * 保存从荔枝FM获取电台数据到本地数据库
	 * 
	 * @param radio
	 * @return
	 */
	public boolean updateGetSaveAudioFlag(Radio radio) {
		boolean result = false;
		if (radio == null) {
			return result;
		} else if (radio.getId() <= 0) {
			return result;
		}
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return result;
		}
		try {
			getDao(session).updateGetSaveAudioFlag(radio);
			session.commit();
			result = true;
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
	 * 更新电台的节目数和同步标记,更新节目数据标记
	 * 
	 * @param radio
	 * @return
	 */
	public boolean updateFlagAndAudios(Radio radio) {
		boolean result = false;
		if (radio == null) {
			return result;
		} else if (radio.getId() <= 0) {
			return result;
		}
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return result;
		}
		try {
			getDao(session).updateFlagAndAudios(radio);
			session.commit();
			result = true;
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
	 * 根据电台编号从荔枝FM获取电台数据
	 * 
	 * @param band
	 * @return
	 */
	public Radio getRadioData(String band) {
		if (StringUtil.isEmptyString(band)) {
			return null;
		}
		try {
			String url = RadioConstants.RADIO_API;
			url = url.replace("#band#", band);
			logger.debug("请求电台数据:::" + url);
			String json = HttpClientUtil.doGet(url);
			if (StringUtil.isEmptyString(json)) {
				return null;
			} else if (!json.startsWith("{")) {
				return null;
			}
			Radio r = new Radio();
			Type gsonType = new TypeToken<Radio>() {
			}.getType();
			Gson gson = new Gson();
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
	 * 从0开始遍历要同步的电台数据
	 * @param maxPagesize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Radio> listRadioForsync(int maxPagesize){
		List<Radio> radios = null;
		if(maxPagesize <= 0 || maxPagesize > 1000){
			maxPagesize = BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE;
		}
		try {
			radios = (List<Radio>) executeDao("queryModelForSync", maxPagesize);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}	
		return radios;
	}

	/**
	 * 同步电台数据到音乐一号服务器
	 * 
	 * @param radio
	 * @return
	 */
	public boolean syncRadioToServer(Radio radio) {
		boolean result = false;
		if (radio == null) {
			return result;
		}
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", radio.getName());
			params.put("band", "" + radio.getBand());
			params.put("userName", radio.getUser_name());
			params.put("description", radio.getDesc());
			params.put("tags", radio.getTagName());
			params.put("cover",  radio.getCoverKey());
			params.put("uthumb",  radio.getU_thumbKey());
			uploadToAliyunDataModelService.save(radio.getCover(), radio.getCoverKey());
			uploadToAliyunDataModelService.save(radio.getU_thumb(), radio.getU_thumbKey());
			String url = BaseConstants.PROTOCOL + BaseConstants.SONG_ONE_SERVER_HOST + RadioConstants.MAKE_SOUND_PROGRAM_LIST_SYNC_API;
			logger.debug("send datas:::" + params);
			String json = HttpClientUtil.doPost(url, params);
			logger.debug(json);
			if (!StringUtil.isEmptyString(json) && json.indexOf(BaseConstants.SONG_ONE_API_RETUEN_STATUS_SUCCESS) > 0) {
				result = true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

}
