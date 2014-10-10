/**
 * 
 */
package com.songone.www.enjoycd.utils;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.aliyun.AliyunUtil;
import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.SystemUtil;
import com.songone.www.enjoycd.constants.EnjoyCDConstants;
import com.songone.www.enjoycd.models.LocalFileDataInfo;
import com.songone.www.enjoycd.service.LocalFileDataInfoService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class UploadFileUtil {

	public static void main(String[] args) {
		logger.debug("start init ....................");
		SystemUtil.init();
		logger.debug("end init ....................");
		start();
	}

	private static final Logger logger = LogManager.getLogger(EnjoyEachLocalFileUtil.class);
	private static LocalFileDataInfoService localFileDataInfoService = new LocalFileDataInfoService();

	public static void start() {
		Timer timer = new Timer("TIMER-UPLOADFILE");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				eachUploadDatasToUpload();
			}
		}, 0L, EnjoyCDConstants.TIME_INTERVAL_TIMES);
		///// 
//		Timer timerValiDate = new Timer("TIMER-UPLOADFILE_VALIDATE");
//		timerValiDate.schedule(new ValiDateUploadIsSuccess(), 0L, EnjoyCDConstants.TIME_INTERVAL_TIMES);
	}
	

	private static void eachUploadDatasToUpload() {
		logger.debug("遍历要上传文件开始");
		while (true) {
			List<LocalFileDataInfo> datas = localFileDataInfoService.listModelForUpload(BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE);
			if (datas == null || datas.isEmpty()) {
				logger.debug("遍历要上传文件结束");
				break;
			}
			for (LocalFileDataInfo model : datas) {
				if (model == null) {
					continue;
				}
				String path = model.getFilePath();
				File file = new File(path);
				if (!file.exists()) {
					logger.error("FilePath::" + path + "|||Key:::" + model.getAliyunKey() + "(文件不存在)");
					continue;
				}
				boolean flag = false;
				try {
					flag = AliyunUtil.uploadBigFile(BaseConstants.ALIYUN_BUCKET, model.getAliyunKey(), file);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				if (!flag) {
					model.setUploadFail(true);
				}
				model.setUpload(true);
				localFileDataInfoService.update(model);
			}
		}
	}
	
	private static class ValiDateUploadIsSuccess extends TimerTask{
		@Override
		public void run() {
			logger.debug("校验上传文件开始");
			while(true){
				List<LocalFileDataInfo> datas = localFileDataInfoService.valiDateUploadIsSuccess(BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE);
				if (datas == null || datas.isEmpty()) {
					logger.debug("校验上传文件结束");
					break;
				}
				for (LocalFileDataInfo model : datas) {
					if (model == null) {
						continue;
					}
					boolean flag = false;
					try {
						logger.debug("校验文件是否存在 [FilePath::" + model.getFilePath() + "|||Key:::" + model.getAliyunKey() + "]");
						flag = AliyunUtil.isExistObjectForTheKey(BaseConstants.ALIYUN_BUCKET, model.getAliyunKey());
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					if (!flag) {
						logger.error("校验文件是否存在 [FilePath::" + model.getFilePath() + "|||Key:::" + model.getAliyunKey() + "] (不存在)");
						model.setUploadFail(true);
					}
					model.setUpload(true);
					localFileDataInfoService.update(model);
				}
				
			}
			
		}
		
	}

}
