/**
 * 
 */
package com.songone.www.lizhi.json.dao.mybatis;

import java.util.List;

import com.songone.www.lizhi.json.model.UploadToAliyunDataModel;

/**
 * 
 * @author Jelly.Liu
 *
 */
public interface UploadToAliyunDataModelDao {

	public void add(UploadToAliyunDataModel model) throws Exception;

	public void updateUploadFlag(UploadToAliyunDataModel model) throws Exception;

	public UploadToAliyunDataModel queryModelByKey(String key) throws Exception;

	public List<UploadToAliyunDataModel> queryModelForUpload(int pageSize) throws Exception;

	public List<UploadToAliyunDataModel> queryModelFailForUpload(int pageSize) throws Exception;

}
