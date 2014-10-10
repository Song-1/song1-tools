/**
 * 
 */
package com.songone.www.aliyun.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.aliyun.dao.mybatis.AliyunMappingFileModelDao;
import com.songone.www.aliyun.model.AliyunMappingFileModel;
import com.songone.www.base.service.BaseService;
import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.StringUtil;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class AliyunMappingFileModelService extends BaseService<AliyunMappingFileModel> {
	private static final Logger logger = LogManager.getLogger(AliyunMappingFileModelService.class);
	@SuppressWarnings("unchecked")
	@Override
	public  Class<AliyunMappingFileModelDao> getRealDaoClass() {
		return AliyunMappingFileModelDao.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AliyunMappingFileModelDao getDao(SqlSession session) {
		if(session == null){
			return null;
		}
		return session.getMapper(AliyunMappingFileModelDao.class);
	}
	
	/**
	 * 保存对象
	 * @param model
	 * @return
	 */
	public AliyunMappingFileModel save(AliyunMappingFileModel model){
		AliyunMappingFileModel result = null;
		if(model == null){
			return result;
		}else if(StringUtil.isEmptyString(model.getPath())){
			return result;
		}
		result = getModelByUniqueKey(model.getPath());
		if(result != null){
			return result;
		}
		try{
			executeDao("add", model);
			result = model;
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 更新对象
	 * @param model
	 * @return
	 */
	public AliyunMappingFileModel update(AliyunMappingFileModel model){
		AliyunMappingFileModel result = model;
		if(model == null){
			return result;
		}else if(model.getId() <= 0){
			return result;
		}else if(StringUtil.isEmptyString(model.getPath())){
			return result;
		}
		try{
			executeDao("update", model);
			result = model;
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 根据唯一键获取对象
	 * @param uniqueKey
	 * @return
	 */
	public AliyunMappingFileModel getModelByUniqueKey(String uniqueKey){
		AliyunMappingFileModel result = null;
		if(StringUtil.isEmptyString(uniqueKey)){
			return result;
		}
		try{
			result = (AliyunMappingFileModel)executeDao("queryModelByPath", uniqueKey);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 遍历要上传的文件
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AliyunMappingFileModel> listFileForUpload(int state){
		List<AliyunMappingFileModel> result = null;
		try{
			result = (List<AliyunMappingFileModel>)executeDao("queryModelForUpload", state);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 遍历上传失败的文件
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AliyunMappingFileModel> listUploadFailFileForUpload(int pageSize){
		List<AliyunMappingFileModel> result = null;
		if(pageSize <= 0 || pageSize > 1000){
			pageSize = BaseConstants.QUERY_DATAS_PAGESIZE;
		}
		try{
			result = (List<AliyunMappingFileModel>)executeDao("queryUploadFailForUpload", pageSize);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 保存数据
	 * @param path 文件的本地路径
	 * @param key 文件的阿里云路径
	 * @param type 类型
	 * @param foreignKeyId 对应其他数据的外键ID
	 * @return
	 */
	public AliyunMappingFileModel saveByFile(String path,String key,int type,int foreignKeyId){
		AliyunMappingFileModel result = null;
		if(StringUtil.isEmptyString(path)){
			return result;
		}else if(StringUtil.isEmptyString(key)){
			return result;
		}
		String suffix = null;
		int index = key.lastIndexOf(".");
		if(index > 0){
			suffix = new String(key.substring(index + 1));
			suffix = suffix.toLowerCase();
		}
		result = new AliyunMappingFileModel();
//		boolean falg = false;
//		try {
//			falg = AliyunUtil.isExistObjectForTheKey(BaseConstants.ALIYUN_BUCKET, key);
//		} catch (AliyunObjectKeyIllegalException e) {
//			result.setRemark(e.getMessage());
//		}
//		if(falg){
//			result.setUploadState(2);
//		}else{
//			result.setUploadState(0);
//		}
		result.setAliyunKey(key);
		result.setPath(path);
		result.setDataType(type);
		result.setForeignKeyId(foreignKeyId);
		result.setSuffix(suffix);
		result = save(result);
		return result;
	}

}
