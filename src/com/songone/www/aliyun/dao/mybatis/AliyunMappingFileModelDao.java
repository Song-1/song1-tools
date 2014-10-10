/**
 * 
 */
package com.songone.www.aliyun.dao.mybatis;

import java.util.List;

import com.songone.www.aliyun.model.AliyunMappingFileModel;

/**
 * 
 * @author Jelly.Liu
 *
 */
public interface AliyunMappingFileModelDao {

	public void add(AliyunMappingFileModel model) throws Exception;

	public void update(AliyunMappingFileModel model) throws Exception;
	
	public AliyunMappingFileModel queryModelByPath(String path)throws Exception ;
	
	public List<AliyunMappingFileModel> queryModelForUpload(int state)throws Exception ;
	
	public List<AliyunMappingFileModel> queryUploadFailForUpload(int pageSize)throws Exception ;

}
