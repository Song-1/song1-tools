/**
 * 
 */
package com.songone.www.enjoy.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.service.BaseService;
import com.songone.www.enjoy.dao.mybatis.AlbumDao;
import com.songone.www.enjoy.models.Album;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class AlbumService extends BaseService<Album> {
	private static final Logger logger = LogManager.getLogger(AlbumService.class);

	@SuppressWarnings("unchecked")
	@Override
	public Class<AlbumDao> getRealDaoClass() {
		return AlbumDao.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AlbumDao getDao(SqlSession session) {
		if (session == null) {
			return null;
		}
		return session.getMapper(AlbumDao.class);
	}

	/**
	 * 保存对象数据
	 * 
	 * @param model
	 * @return
	 */
	public Album save(Album model) {
		Album result = null;
		if (model == null) {
			return result;
		}
		result = getModelByNameIndex(model);
		if (result != null) {
			return result;
		}
		try {
			executeDao("add", model);
			result = model;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 更新对象数据
	 * 
	 * @param model
	 * @return
	 */
	public Album update(Album model) {
		Album result = null;
		if (model == null) {
			return result;
		} else if (model.getId() <= 0) {
			return result;
		}
		try {
			executeDao("update", model);
			result = model;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 根据唯一索引字段值获取专辑数据
	 * 
	 * @param model
	 * @return
	 */
	public Album getModelByNameIndex(Album model) {
		Album result = null;
		if (model == null) {
			return result;
		}
		try {
			result = (Album) executeDao("queryModelByNameIndex", model);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 根据主键查询专辑
	 * 
	 * @param id
	 * @return
	 */
	public Album getAlbumById(int id) {
		Album result = null;
		if (id <= 0) {
			return result;
		}
		try {
			result = (Album) executeDao("queryModelById", id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;

	}

	/**
	 * 遍历上传中的专辑数据
	 * 
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Album> listAlbumForCheckSync(String user) {
		List<Album> result = null;
		try {
			result = (List<Album>) executeDao("queryModelForCheckSync", user);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 遍历待同步的专辑数据
	 * 
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Album> listAlbumForSync(String user) {
		List<Album> result = null;
		try {
			result = (List<Album>) executeDao("queryModelForSync", user);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 当专辑的全部专辑歌曲已经上传完毕,更新专辑的状态
	 * 
	 * @param albumId
	 */
	public void updateAlbumState(int albumId) {
		Album album = getAlbumById(albumId);
		if (album == null) {
			return;
		}
		int albumSongs = album.getAlbumSongs();
		int songs = 0;
		try {
			Integer result = (Integer) executeDao("getCanSyncAlbumSongs", albumId);
			songs = result != null ? result.intValue() : songs;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (songs == albumSongs) {
			album.setState(2);
			this.update(album);
		}
	}

}
