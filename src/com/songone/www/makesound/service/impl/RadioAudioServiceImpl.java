/**
 * 
 */
package com.songone.www.makesound.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.db.mybatis.MybatisUtil;
import com.songone.www.base.utils.StringUtil;
import com.songone.www.makesound.dao.mybatis.RadioAudioDao;
import com.songone.www.makesound.model.RadioAudio;
import com.songone.www.makesound.service.RadioAudioService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class RadioAudioServiceImpl implements RadioAudioService{
	private static final Logger logger = LogManager.getLogger(SyncServiceImpl.class);

	private RadioAudioDao getDao(SqlSession session) {
		if (session == null) {
			return null;
		}
		return session.getMapper(RadioAudioDao.class);
	}
	@Override
	public boolean saveOrUpdate(RadioAudio model) {
		boolean result = false;
		if (model == null) {
			return result;
		}
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return result;
		}
		try {
			RadioAudioDao dao = getDao(session);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("name", model.getName());
			map.put("band", model.getBand());
			RadioAudio resultModel = dao.queryModel(map);
			if(resultModel != null && resultModel.getAudioId() > 0){
				if("Y".equals(resultModel.getSync_flag())){
					return result;
				}
				model.setAudioId(resultModel.getAudioId());
				dao.update(model);
			}else{
				dao.add(model);
			}
			session.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		} finally {
			if(session != null){
				session.close();
			}
		}
		return result;
		
	}

	@Override
	public RadioAudio listByNameAndBand(String name, String band) {
		RadioAudio model = null;
		if(StringUtil.isEmptyString(band)){
			return model;
		}else if(StringUtil.isEmptyString(name)){
			return model;
		}
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return model;
		}
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("band", band);
			model = getDao(session).queryModel(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		} finally {
			if(session != null){
				session.close();
			}
		}
		return model;
	}

	@Override
	public List<RadioAudio> listSyncAudioDatas(int start, int pageSize) {
		List<RadioAudio> reslut = null;
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return reslut;
		}
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("start", start);
			map.put("pageSize", pageSize);
			reslut = getDao(session).queryModelByPage(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		} finally {
			if(session != null){
				session.close();
			}
		}
		return reslut;
	}

}
