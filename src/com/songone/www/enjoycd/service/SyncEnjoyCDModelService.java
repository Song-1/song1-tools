/**
 * 
 */
package com.songone.www.enjoycd.service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.songone.www.base.model.BaseResultBean;
import com.songone.www.base.service.BaseService;
import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.HttpClientUtil;
import com.songone.www.base.utils.StringUtil;
import com.songone.www.enjoycd.constants.EnjoyCDConstants;
import com.songone.www.enjoycd.dao.mybatis.SyncEnjoyCDModelDao;
import com.songone.www.enjoycd.models.SyncEnjoyCDModel;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class SyncEnjoyCDModelService extends BaseService<SyncEnjoyCDModel> {
	private static final Logger logger = LogManager.getLogger(SyncEnjoyCDModelService.class);

	@SuppressWarnings("unchecked")
	@Override
	public Class<SyncEnjoyCDModelDao> getRealDaoClass() {
		return SyncEnjoyCDModelDao.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SyncEnjoyCDModelDao getDao(SqlSession session) {
		if (session == null) {
			return null;
		}
		return session.getMapper(SyncEnjoyCDModelDao.class);
	}

	public int save(SyncEnjoyCDModel model) {
		int result = 0;
		if (model == null) {
			return result;
		}
		try {
			executeDao("add", model);
			result = model.getId();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	public boolean update(SyncEnjoyCDModel model) {
		boolean result = false;
		if (model == null) {
			return result;
		} else if (model.getId() <= 0) {
			return result;
		}
		try {
			executeDao("update", model);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 根据名字查询专辑数据
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SyncEnjoyCDModel getModelByName(String name,boolean isAlbum) {
		SyncEnjoyCDModel result = null;
		if (StringUtil.isEmptyString(name)) {
			return result;
		}
		result = new SyncEnjoyCDModel();
		result.setName(name);
		result.setAlbum(isAlbum);
		try {
			List<SyncEnjoyCDModel> datas = (List<SyncEnjoyCDModel>) executeDao("queryModelByNameAndAlbumFlag", result);
			if(datas != null && !datas.isEmpty()){
				result = datas.get(0);
			}else{
				result = null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 遍历0~pageSize的专辑数据
	 * 
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SyncEnjoyCDModel> listAlbumForSync(int pageSize) {
		List<SyncEnjoyCDModel> result = null;
		if (pageSize <= 0 || pageSize > 1000) {
			pageSize = BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE;
		}
		try {
			result = (List<SyncEnjoyCDModel>) executeDao("queryModelForSyncAlbum", pageSize);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 根据专辑的ID遍历专辑的数据
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SyncEnjoyCDModel> listAlbumDatas(int id) {
		List<SyncEnjoyCDModel> result = null;
		if (id <= 0) {
			return result;
		}
		try {
			result = (List<SyncEnjoyCDModel>) executeDao("queryModelByAlbum", id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 同步专辑歌曲
	 * 
	 * @param model
	 * @return
	 */
	public String syncAlbumSong(SyncEnjoyCDModel model, String createDate, String createuser) {
		if (model == null) {
			return null;
		} else if (model.isAlbum()) {
			return null;
		}
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", model.getName());
			params.put("url", model.getUrl());
			params.put("seat", model.getSeat() + "");
			params.put("createDate", createDate);
			params.put("createUser", createuser);
			String url = BaseConstants.PROTOCOL + BaseConstants.SONG_ONE_SERVER_HOST + EnjoyCDConstants.SONG_ONE_ENJOY_API_ADD_SONG;
			logger.debug("send datas:::" + params);
			String json = HttpClientUtil.doPost(url, params);
			logger.debug(json);
			if (!StringUtil.isEmptyString(json) && json.indexOf(BaseConstants.SONG_ONE_API_RETUEN_STATUS_SUCCESS) > 0) {
				BaseResultBean<String> bean = new BaseResultBean<String>();
				Gson gson = new Gson();
				Type gsonType = new TypeToken<BaseResultBean<String>>() {
				}.getType();
				bean = gson.fromJson(json, gsonType);
				return bean.getData();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 同步专辑歌手
	 * 
	 * @param model
	 * @return
	 */
	public String syncAlbumSinger(String name,String img,String icon) {
		if (StringUtil.isEmptyString(name)) {
			return null;
		}
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", name);
			params.put("img", img);
			params.put("icon", icon);
			String url = BaseConstants.PROTOCOL + BaseConstants.SONG_ONE_SERVER_HOST + EnjoyCDConstants.SONG_ONE_ENJOY_API_ADD_SINGER;
			logger.debug("send datas:::" + params);
			String json = HttpClientUtil.doPost(url, params);
			logger.debug(json);
			if (!StringUtil.isEmptyString(json) && json.indexOf(BaseConstants.SONG_ONE_API_RETUEN_STATUS_SUCCESS) > 0) {
				BaseResultBean<String> bean = new BaseResultBean<String>();
				Gson gson = new Gson();
				Type gsonType = new TypeToken<BaseResultBean<String>>() {
				}.getType();
				bean = gson.fromJson(json, gsonType);
				return bean.getData();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 同步专辑风格
	 * 
	 * @param model
	 * @return
	 */
	public String syncAlbumStyle(String name) {
		if (StringUtil.isEmptyString(name)) {
			return null;
		}
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", name);
			String url = BaseConstants.PROTOCOL + BaseConstants.SONG_ONE_SERVER_HOST + EnjoyCDConstants.SONG_ONE_ENJOY_API_ADD_STYLE;
			logger.debug("send datas:::" + params);
			String json = HttpClientUtil.doPost(url, params);
			logger.debug(json);
			if (!StringUtil.isEmptyString(json) && json.indexOf(BaseConstants.SONG_ONE_API_RETUEN_STATUS_SUCCESS) > 0) {
				BaseResultBean<String> bean = new BaseResultBean<String>();
				Gson gson = new Gson();
				Type gsonType = new TypeToken<BaseResultBean<String>>() {
				}.getType();
				bean = gson.fromJson(json, gsonType);
				return bean.getData();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 同步专辑
	 * 
	 * @param model
	 * @return
	 */
	public boolean syncAlbum(String name, String img, String icon, String singerId, String styleId, String songIds, String createDate, String createUser) {
		boolean result = false;
		if (StringUtil.isEmptyString(name)) {
			return result;
		}
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", name);
			params.put("image", img);
			params.put("icon", icon);
			params.put("_add_", songIds);
			params.put("styleId", styleId);
			params.put("singerId", singerId);
			params.put("createDate", createDate);
			params.put("createUser", createUser);
			String url = BaseConstants.PROTOCOL + BaseConstants.SONG_ONE_SERVER_HOST + EnjoyCDConstants.SONG_ONE_ENJOY_API_ADD_ALBUM;
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
