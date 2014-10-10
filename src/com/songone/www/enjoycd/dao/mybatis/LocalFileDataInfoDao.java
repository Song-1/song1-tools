/**
 * 
 */
package com.songone.www.enjoycd.dao.mybatis;

import java.util.List;

import com.songone.www.enjoycd.models.LocalFileDataInfo;

/**
 * 
 * @author Jelly.Liu
 *
 */
public interface LocalFileDataInfoDao {

	public void add(LocalFileDataInfo model) throws Exception;

	public void update(LocalFileDataInfo model) throws Exception;

	public LocalFileDataInfo queryModelByFilePath(String filePath) throws Exception;

	public List<LocalFileDataInfo> queryModelForUpload(int pageSize) throws Exception;
	public List<LocalFileDataInfo> queryModelForUploadVali(int pageSize) throws Exception;

	public List<LocalFileDataInfo> queryModelForSyncEnjoyCD(int pageSize) throws Exception;
	
	public int queryModelCountsByParentId(int parentId) throws Exception;

}
