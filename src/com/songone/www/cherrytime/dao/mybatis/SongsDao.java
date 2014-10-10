/**
 * 
 */
package com.songone.www.cherrytime.dao.mybatis;

import com.songone.www.cherrytime.model.Songs;

/**
 * 
 * @author Jelly.Liu
 *
 */
public interface SongsDao {
	
	public void add(Songs model)throws Exception;
	
	public void update(Songs model)throws Exception;
	
	public Songs queryModelByNameIndex(Songs model) throws Exception;
	
	public Songs queryModelById(int id) throws Exception;
}
