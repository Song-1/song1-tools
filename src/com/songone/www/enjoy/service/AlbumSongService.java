/**
 * 
 */
package com.songone.www.enjoy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.model.HttpResponseData;
import com.songone.www.base.service.BaseService;
import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.HttpClientUtil;
import com.songone.www.enjoy.dao.mybatis.AlbumSongDao;
import com.songone.www.enjoy.models.AlbumSong;
import com.songone.www.enjoycd.constants.EnjoyCDConstants;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class AlbumSongService extends BaseService<AlbumSong> {
	private static final Logger logger = LogManager.getLogger(AlbumSongService.class);

	@SuppressWarnings("unchecked")
	@Override
	public Class<AlbumSongDao> getRealDaoClass() {
		return AlbumSongDao.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AlbumSongDao getDao(SqlSession session) {
		if (session == null) {
			return null;
		}
		return session.getMapper(AlbumSongDao.class);
	}
	
	/**
	 * 保存对象数据
	 * @param model
	 * @return
	 */
	public AlbumSong save(AlbumSong model){
		AlbumSong result = null;
		if(model == null){
			return result;
		}
		result = getModelByNameIndex(model);
		if(result != null){
			return result;
		}
		try{
			executeDao( "add", model);
			result = model;
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	/**
	 * 根据唯一索引字段值获取专辑歌曲数据
	 * @param model
	 * @return
	 */
	public AlbumSong getModelByNameIndex(AlbumSong model){
		AlbumSong result = null;
		if(model == null){
			return result;
		}
		try{
			result = (AlbumSong)executeDao( "queryModelByNameIndex", model);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 更新对象数据
	 * @param model
	 * @return
	 */
	public AlbumSong update(AlbumSong model){
		AlbumSong result = null;
		if(model == null){
			return result;
		}else if(model.getId() <= 0){
			return result;
		}
		try{
			executeDao( "update", model);
			result = model;
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 根据ID获取专辑歌曲数据
	 * @param id
	 * @return
	 */
	public AlbumSong getModelById(int id){
		AlbumSong result = null;
		if(id <= 0){
			return result;
		}
		try{
			result = (AlbumSong)executeDao( "queryModelById", id);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<AlbumSong> listForSyncByParentId(int parentId){
		List<AlbumSong> result = null;
		if(parentId <= 0){
			return result;
		}
		try{
			result = (List<AlbumSong>)executeDao("queryModelForSyncByParentId", parentId);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	public void updateAlbumSongsState(int albumId){
		if(albumId <= 0 ){
			return;
		}
		String url = BaseConstants.PROTOCOL + BaseConstants.SONG_ONE_SERVER_HOST + EnjoyCDConstants.SONG_ONE_ENJOY_UPDATE_ALBUM_SONG_STATE + albumId;
		Map<String,String> params = new HashMap<String, String>();
		params.put("state", "valid"); // 上传成功则修改专辑歌曲状态为启用
		HttpClientUtil.doPut(url, params);
	}

}
