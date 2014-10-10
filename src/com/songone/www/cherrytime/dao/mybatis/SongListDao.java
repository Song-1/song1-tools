/**
 * 
 */
package com.songone.www.cherrytime.dao.mybatis;

import com.songone.www.cherrytime.model.SongList;

/**
 * 
 * @author Jelly.Liu
 *
 */
public interface SongListDao {
	
	public void add(SongList model)throws Exception;
	
	public void update(SongList model)throws Exception;
	
	public SongList queryModelByNameIndex(SongList model) throws Exception;
	
	public SongList queryModelById(int id) throws Exception;
	
	public int getCanSyncSongs(int id)throws Exception;
}
