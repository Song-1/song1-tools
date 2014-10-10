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
import com.songone.www.makesound.dao.mybatis.MakesoundSyncRadioModelDao;
import com.songone.www.makesound.model.MakesoundSyncRadioModel;
import com.songone.www.makesound.service.SyncService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class SyncServiceImpl implements SyncService {
	private static final Logger logger = LogManager.getLogger(SyncServiceImpl.class);

	private MakesoundSyncRadioModelDao getDao(SqlSession session) {
		if (session == null) {
			return null;
		}
		return session.getMapper(MakesoundSyncRadioModelDao.class);
	}

	@Override
	public void save(MakesoundSyncRadioModel model) {
		if (model == null) {
			return;
		}
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return;
		}
		try {
			MakesoundSyncRadioModelDao dao = getDao(session);
			MakesoundSyncRadioModel resultModel = dao.selectByBand(model.getBand());
			if(resultModel != null && resultModel.getId() > 0){
				model.setId(resultModel.getId());
				dao.update(model);
			}else{
				dao.add(model);
			}
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		} finally {
			if(session != null){
				session.close();
			}
		}
	}

	@Override
	public List<MakesoundSyncRadioModel> list(String band) {
		if(StringUtil.isEmptyString(band)){
			return null;
		}
		return null;
	}

	@Override
	public MakesoundSyncRadioModel listByBand(String band) {
		MakesoundSyncRadioModel model = null;
		if(StringUtil.isEmptyString(band)){
			return model;
		}
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return model;
		}
		try {
			model = getDao(session).selectByBand(band);
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
	public List<MakesoundSyncRadioModel> listSyncAudioDatas(int start, int pageSize) {
		List<MakesoundSyncRadioModel> reslut = null;
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			return reslut;
		}
		try {
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("start", start);
			map.put("pageSize", pageSize);
			reslut = getDao(session).selectForSyncAudio(map);
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
