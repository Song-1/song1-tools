/**
 * 
 */
package com.songone.www.enjoy.dao.mybatis;

import java.util.List;

import com.songone.www.enjoy.models.AlbumSong;

/**
 * 
 * @author Jelly.Liu
 *
 */
public interface AlbumSongDao {
	public void add(AlbumSong model) throws Exception;

	public void update(AlbumSong model) throws Exception;
	
	public AlbumSong queryModelByNameIndex(AlbumSong model) throws Exception;
	
	public AlbumSong queryModelById(int id)throws Exception;
	
	public List<AlbumSong> queryModelForSyncByParentId(int parentId)throws Exception;

}
