/**
 * 
 */
package com.songone.www.aliyun.main;

import java.util.Timer;
import java.util.TimerTask;

import com.songone.www.aliyun.action.UploadFileAction;
import com.songone.www.base.utils.SystemUtil;
import com.songone.www.enjoycd.constants.EnjoyCDConstants;

/**
 * @author Administrator
 *
 */
public class AliyunMain {

	public static void main(String[] args) {
		SystemUtil.init();
		////// 
		Timer timer = new Timer("TIMER-UPLOAD-FILE");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				UploadFileAction action = new UploadFileAction();
				action.start();
			}
		}, 0L, EnjoyCDConstants.TIME_INTERVAL_TIMES);
	}

}
