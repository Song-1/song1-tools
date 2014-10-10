/**
 * 
 */
package com.songone.www.aliyun.action;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.aliyun.AliyunUtil;
import com.songone.www.aliyun.exceptions.AliyunObjectKeyIllegalException;
import com.songone.www.aliyun.functions.UploadFiles;
import com.songone.www.aliyun.model.AliyunMappingFileModel;
import com.songone.www.aliyun.model.UploadServiceObserver;
import com.songone.www.aliyun.runnable.UpdateOtherDataRunnable;
import com.songone.www.aliyun.service.AliyunMappingFileModelService;
import com.songone.www.base.utils.BaseConstants;
import com.songone.www.enjoy.action.EachFileAction;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class UploadFileAction {
	public static final Logger logger = LogManager.getLogger(EachFileAction.class);
	private AliyunMappingFileModelService aliyunMappingFileModelService = new AliyunMappingFileModelService();
	
	public void uploadFile(){
		List<AliyunMappingFileModel> datas = null;
		while(true){
			datas = aliyunMappingFileModelService.listFileForUpload(0);
			if(datas == null || datas.isEmpty()){
				break;
			}
			UploadServiceObserver observer = new UploadServiceObserver();
			for (AliyunMappingFileModel model : datas) {
				if(model == null){
					continue;
				}
				logger.debug("处理:::" + model);
				String path = model.getPath();
				File file = new File(path);
				if(!file.exists()){
					model.setUploadState(3); // 上传失败
					model.setRemark("本地文件不存在");
					aliyunMappingFileModelService.update(model);
					continue;
				}
				String key = model.getAliyunKey();
				boolean isExistFlag = false;
				try {
					isExistFlag = AliyunUtil.isExistObjectForTheKey(BaseConstants.ALIYUN_BUCKET, key);
				} catch (AliyunObjectKeyIllegalException e) {
					model.setRemark("key值不合法");
					model.setUploadState(3); // 上传失败
					aliyunMappingFileModelService.update(model);
					logger.error(e.getMessage(),e);
					continue;
				}
				if(isExistFlag){
					model.setRemark("key值已经存在");
					model.setUploadState(2); // 上传成功
					aliyunMappingFileModelService.update(model);
					observer.attach(model);
					continue;
				}
				model.setUploadState(1);
				aliyunMappingFileModelService.update(model);
				UploadFiles uploadFile = new UploadFiles();
				isExistFlag = uploadFile.uploadFile(BaseConstants.ALIYUN_BUCKET, key, file);
				if(!isExistFlag){
					model.setRemark(uploadFile.getErrorMsg());
					model.setUploadState(3); // 上传失败
					aliyunMappingFileModelService.update(model);
				}else{
					model.setUploadState(2); // 上传成功
					aliyunMappingFileModelService.update(model);
					observer.attach(model);
				}
			}
			//// 更新相关数据的状态
			UpdateOtherDataRunnable runnable = new UpdateOtherDataRunnable(observer);
			new Thread(runnable).start();
		}
	}
	
	public void updateFileDataState(){
		List<AliyunMappingFileModel> datas = null;
		while(true){
			datas = aliyunMappingFileModelService.listFileForUpload(2);
			if(datas == null || datas.isEmpty()){
				break;
			}
			UploadServiceObserver observer = new UploadServiceObserver();
			observer.setDatas(datas);
			//// 更新相关数据的状态
			UpdateOtherDataRunnable runnable = new UpdateOtherDataRunnable(observer);
			new Thread(runnable).start();
		}
	}
}
