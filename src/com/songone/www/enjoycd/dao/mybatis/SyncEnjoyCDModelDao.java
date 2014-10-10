/**
 * 
 */
package com.songone.www.enjoycd.dao.mybatis;

import java.util.List;

import com.songone.www.enjoycd.models.SyncEnjoyCDModel;

/**
 * 
 * @author Jelly.Liu
 *
 */
public interface SyncEnjoyCDModelDao {

	public void add(SyncEnjoyCDModel model) throws Exception;

	public void update(SyncEnjoyCDModel model) throws Exception;

	public List<SyncEnjoyCDModel> queryModelByNameAndAlbumFlag(SyncEnjoyCDModel model) throws Exception;

	public List<SyncEnjoyCDModel> queryModelForSyncAlbum(int pageSize) throws Exception;

	public List<SyncEnjoyCDModel> queryModelByAlbum(int id) throws Exception;

}
