/**
 * 
 */
package com.songone.www.lizhi.json.start;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.FileDoUtil;
import com.songone.www.base.utils.StringUtil;
import com.songone.www.base.utils.SystemUtil;
import com.songone.www.lizhi.json.model.UploadToAliyunDataModel;
import com.songone.www.lizhi.json.service.UploadToAliyunDataModelService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class UploadToAliyunDataModelMain {
	private static final Logger logger = LogManager.getLogger(FileDoUtil.class);

	public static void main(String[] args) {
		logger.debug("初始化  开始");
		SystemUtil.init();
		logger.debug("初始化 完成");
		toDoUploadFailDatas();
	}
	
	public static void uploadDatas() {
		UploadToAliyunDataModelService service = new UploadToAliyunDataModelService();
		while (true) {
			List<UploadToAliyunDataModel> datas = service.listDataForUpload(BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE);
			if (datas == null) {
				break;
			}
			for (UploadToAliyunDataModel model : datas) {
				if (model == null) {
					continue;
				}
				String key = FileDoUtil.uploadFileToAliyun(BaseConstants.ALIYUN_BUCKET, model.getAliyunKey(), model.getUrl());
				if (StringUtil.isEmptyString(key)) {
					model.setUploadFail(true);
					logger.debug("同步数据到阿里云失败:::" + model);
				}
				model.setUpload(true);
				service.updateUploadFlag(model);
			}
		}
	}

	public static void toDoUploadFailDatas() {
		UploadToAliyunDataModelService service = new UploadToAliyunDataModelService();
		while (true) {
			List<UploadToAliyunDataModel> datas = service.listUploadFailDataForUpload(BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE);
			if (datas == null) {
				break;
			}
			for (UploadToAliyunDataModel model : datas) {
				if (model == null) {
					continue;
				}
				String key = model.getAliyunKey();
				if(!key.endsWith(".mp3") && !key.endsWith(".jpg")){
					continue;
				}
				key = FileDoUtil.uploadFileToAliyun(BaseConstants.ALIYUN_BUCKET, key, model.getUrl());
				if(StringUtil.isEmptyString(key)){
					model.setUploadFail(true);
				}else{
					model.setUploadFail(false);
				}
				model.setUpload(true);
				service.updateUploadFlag(model);
			}
		}
	}

}
