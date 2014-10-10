package com.songone.www.cherrytime.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.service.BaseService;
import com.songone.www.cherrytime.dao.mybatis.SongListDao;
import com.songone.www.cherrytime.model.SongList;

/**
 * 
 * 
 * @author Jelly.Liu
 *
 */
public class SongListService extends BaseService<SongList> {
	private static final Logger logger = LogManager.getLogger(SongListService.class);
	@SuppressWarnings("unchecked")
	@Override
	public Class<SongListDao> getRealDaoClass() {
		return SongListDao.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SongListDao getDao(SqlSession session) {
		if(session == null){
			return null;
		}
		return session.getMapper(SongListDao.class);
	}
	/**
	 * 保存
	 * @param song
	 * @return
	 */
	public SongList save(SongList song){
		SongList result = null;
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
	public SongList update(SongList song){
		SongList result = null;
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
	public SongList getModelByNameIndex(SongList song){
		SongList result = null;
		if(song == null){
			return result;
		}
		try{
			result = (SongList) executeDao("queryModelByNameIndex", song);
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
	public SongList getModelById(int id){
		SongList result = null;
		if(id <= 0){
			return result;
		}
		try{
			result = (SongList) executeDao("queryModelById", id);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 更新歌单的状态为待同步
	 * @param albumId
	 */
	public void updateListState(int id) {
		if(id <= 0){
			return ;
		}
		SongList songList = getModelById(id);
		if (songList == null) {
			return;
		}
		int albumSongs = songList.getSongs();
		int songs = 0;
		try {
			Integer result = (Integer) executeDao("getCanSyncSongs", id);
			songs = result != null ? result.intValue() : songs;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (songs == albumSongs) {
			songList.setState(2);
			this.update(songList);
		}
	}


}
