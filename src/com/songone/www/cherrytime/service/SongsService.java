package com.songone.www.cherrytime.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.service.BaseService;
import com.songone.www.cherrytime.dao.mybatis.SongsDao;
import com.songone.www.cherrytime.model.Songs;

/**
 * 
 * 
 * @author Jelly.Liu
 *
 */
public class SongsService extends BaseService<Songs> {
	private static final Logger logger = LogManager.getLogger(SongsService.class);
	@SuppressWarnings("unchecked")
	@Override
	public Class<SongsDao> getRealDaoClass() {
		return SongsDao.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SongsDao getDao(SqlSession session) {
		if(session == null){
			return null;
		}
		return session.getMapper(SongsDao.class);
	}
	/**
	 * 保存
	 * @param song
	 * @return
	 */
	public Songs save(Songs song){
		Songs result = null;
		if(song == null){
			return result;
		}
		result = getModelByNameIndex(song);
		if(result != null){
			return result;
		}
		try{
			executeDao("add", song);
			result = song;
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 更新
	 * @param song
	 * @return
	 */
	public Songs update(Songs song){
		Songs result = null;
		if(song == null){
			return result;
		}else if(song.getId() <= 0){
			return result;
		}
		try{
			executeDao("update", song);
			result = song;
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 根据唯一索引查询歌曲数据
	 * @param song
	 * @return
	 */
	public Songs getModelByNameIndex(Songs song){
		Songs result = null;
		if(song == null){
			return result;
		}
		try{
			result = (Songs) executeDao("queryModelByNameIndex", song);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	/**
	 * 根据主键查询歌曲数据
	 * @param song
	 * @return
	 */
	public Songs getModelById(int id){
		Songs result = null;
		if(id <= 0){
			return result;
		}
		try{
			result = (Songs) executeDao("queryModelById", id);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return result;
	}

}
