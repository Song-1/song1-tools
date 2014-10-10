/**
 * 
 */
package com.songone.www.enjoy.dao.mybatis;

import java.util.List;

import com.songone.www.enjoy.models.Album;

/**
 * 
 * @author Jelly.Liu
 *
 */
public interface AlbumDao {

	public void add(Album model) throws Exception;

	public void update(Album model) throws Exception;
	
	public Album queryModelByNameIndex(Album model) throws Exception;
	
	public void checkAlbumSongState() throws Exception;
	
	public int getCanSyncAlbumSongs(int albumId)throws Exception;
	
	public List<Album> queryModelForCheckSync(String user)throws Exception;
	
	public Album queryModelById(int id)throws Exception;
	
	

}
