/**
 * 
 */
package com.songone.www.enjoycd.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.service.BaseService;
import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.StringUtil;
import com.songone.www.enjoycd.dao.mybatis.LocalFileDataInfoDao;
import com.songone.www.enjoycd.models.LocalFileDataInfo;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class LocalFileDataInfoService extends BaseService<LocalFileDataInfo> {
	private static final Logger logger = LogManager.getLogger(LocalFileDataInfoService.class);

	@SuppressWarnings("unchecked")
	@Override
	public Class<LocalFileDataInfoDao> getRealDaoClass() {
		return LocalFileDataInfoDao.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public LocalFileDataInfoDao getDao(SqlSession session) {
		if (session == null) {
			return null;
		}
		return session.getMapper(LocalFileDataInfoDao.class);
	}

	/**
	 * 保存文件信息对象
	 * 
	 * @param model
	 * @return
	 */
	public int save(LocalFileDataInfo model) {
		int result = 0;
		if (model == null) {
			return result;
		}
		LocalFileDataInfo m = ListModelByFilePath(model.getFilePath());
		if (m != null) {
			return m.getId();
		}
		try {
			executeDao("add", model);
			result = model.getId();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 更新文件信息对象
	 * 
	 * @param model
	 * @return
	 */
	public int update(LocalFileDataInfo model) {
		int result = 0;
		if (model == null) {
			return result;
		}
		LocalFileDataInfo m = ListModelByFilePath(model.getFilePath());
		if (m == null) {
			return result;
		}
		try {
			model.setId(m.getId());
			executeDao("update", model);
			result = model.getId();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 根据文件的地址来查询文件的信息数据
	 * 
	 * @param model
	 * @return
	 */
	public LocalFileDataInfo ListModelByFilePath(String filePath) {
		LocalFileDataInfo result = null;
		if (StringUtil.isEmptyString(filePath)) {
			return result;
		}
		try {
			result = (LocalFileDataInfo) executeDao("queryModelByFilePath", filePath);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 遍历0~pageSize的没有上传到阿里云的数据
	 * 
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LocalFileDataInfo> listModelForUpload(int pageSize) {
		List<LocalFileDataInfo> result = null;
		if (pageSize <= 0 || pageSize > 1000) {
			pageSize = BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE;
		}
		try {
			result = (List<LocalFileDataInfo>) executeDao("queryModelForUpload", pageSize);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	/**
	 * 遍历0~pageSize的没有上传到阿里云的数据
	 * 
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LocalFileDataInfo> valiDateUploadIsSuccess(int pageSize) {
		List<LocalFileDataInfo> result = null;
		if (pageSize <= 0 || pageSize > 1000) {
			pageSize = BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE;
		}
		try {
			result = (List<LocalFileDataInfo>) executeDao("queryModelForUploadVali", pageSize);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 遍历0~pageSize的没有同步的数据
	 * 
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LocalFileDataInfo> listModelForSyncEnjoyCD(int pageSize) {
		List<LocalFileDataInfo> result = null;
		if (pageSize <= 0 || pageSize > 1000) {
			pageSize = BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE;
		}
		try {
			result = (List<LocalFileDataInfo>) executeDao("queryModelForSyncEnjoyCD", pageSize);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 获取文件总数 
	 * @param parentId
	 * @return
	 */
	public int getFileCountsByParentId(int parentId){
		int result = 0;
		if (parentId <= 0 ) {
			return result;
		}
		try {
			Integer i = (Integer) executeDao("queryModelCountsByParentId", parentId);
			result = i != null ? i.intValue() : result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

}
