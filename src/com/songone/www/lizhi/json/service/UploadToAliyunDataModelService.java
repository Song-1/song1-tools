/**
 * 
 */
package com.songone.www.lizhi.json.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.aliyun.AliyunUtil;
import com.songone.www.base.service.BaseService;
import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.StringUtil;
import com.songone.www.lizhi.json.dao.mybatis.UploadToAliyunDataModelDao;
import com.songone.www.lizhi.json.model.UploadToAliyunDataModel;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class UploadToAliyunDataModelService extends BaseService<UploadToAliyunDataModel>{
	private static final Logger logger = LogManager.getLogger(UploadToAliyunDataModelService.class);
	@SuppressWarnings("unchecked")
	@Override
	public Class<UploadToAliyunDataModelDao> getRealDaoClass() {
		return UploadToAliyunDataModelDao.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UploadToAliyunDataModelDao getDao(SqlSession session) {
		if(session == null){
			return null;
		}
		return session.getMapper(UploadToAliyunDataModelDao.class);
	}
	
	private boolean validateModel(UploadToAliyunDataModel model){
		boolean result = false;
		if(model == null){
			return result;
		}else if(StringUtil.isEmptyString(model.getUrl())){
			return result;
		}else if(StringUtil.isEmptyString(model.getAliyunKey())){
			return result;
		}else{
			result = true;
		}
		return result;
	}
	
	/**
	 * 保存数据
	 * @param url
	 * @param key
	 */
	public String save(String url,String key){
		if(StringUtil.isEmptyString(url)){
			return null;
		}else if(StringUtil.isEmptyString(key)){
			return null;
		}
		boolean falg =  AliyunUtil.isAliyunObjectKeyIllegal(BaseConstants.ALIYUN_BUCKET, key);
		if(falg){
			key = StringUtil.formateAliyunKey(key);
			falg =  AliyunUtil.isAliyunObjectKeyIllegal(BaseConstants.ALIYUN_BUCKET, key);
		}
		if(falg){
			return null;
		}
		UploadToAliyunDataModel model = new UploadToAliyunDataModel();
		model.setUrl(url);
		model.setAliyunKey(key);
		this.save(model);
		return key;
	}
	
	/**
	 * 保存数据
	 * @param model
	 * @return
	 */
	public synchronized boolean save(UploadToAliyunDataModel model){
		boolean result = false;
		if(!validateModel(model)){
			return result;
		}
		try{
			UploadToAliyunDataModel m = (UploadToAliyunDataModel)executeDao("queryModelByKey", model.getAliyunKey());
			if(m == null){
				executeDao("add", model);
			}
			result = true;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 更新上传数据标记
	 * @param model
	 * @return
	 */
	public boolean updateUploadFlag(UploadToAliyunDataModel model){
		boolean result = false;
		if(!validateModel(model)){
			return result;
		}
		try{
			UploadToAliyunDataModel m = (UploadToAliyunDataModel)executeDao("queryModelByKey", model.getAliyunKey());
			if(m != null){
				executeDao("updateUploadFlag", model);
				result = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 遍历0~pageSize 的数据上传
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UploadToAliyunDataModel> listDataForUpload(int pageSize){
		List<UploadToAliyunDataModel> result = null;
		if(pageSize <=0 || pageSize > 1000){
			pageSize = BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE;
		}
		try{
			result = (List<UploadToAliyunDataModel>)executeDao("queryModelForUpload", pageSize);
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 遍历0~pageSize 的上传失败的数据上传
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UploadToAliyunDataModel> listUploadFailDataForUpload(int pageSize){
		List<UploadToAliyunDataModel> result = null;
		if(pageSize <=0 || pageSize > 1000){
			pageSize = BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE;
		}
		try{
			result = (List<UploadToAliyunDataModel>)executeDao("queryModelFailForUpload", pageSize);
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return result;
	}

}
