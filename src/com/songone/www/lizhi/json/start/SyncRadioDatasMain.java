/**
 * 
 */
package com.songone.www.lizhi.json.start;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.SystemUtil;
import com.songone.www.lizhi.json.constans.ThreadConstans;
import com.songone.www.lizhi.json.model.Radio;
import com.songone.www.lizhi.json.runnable.SyncRadioRunnable;
import com.songone.www.lizhi.json.service.RadioService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class SyncRadioDatasMain {
	private static final Logger logger = LogManager.getLogger(SyncRadioDatasMain.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SystemUtil.init();
		syncRadioDatasStart();
	}
	
	public static void syncRadioDatasStart(){
		new Thread(new Runnable() {
			public void run() {
				long startTimes = 0L; // ms
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						syncRadioDatas();
					}
				}, startTimes, ThreadConstans.TIME_INTERVAL_TIMES);
			}
		}).start();
	}
	
	public static void syncRadioDatas(){
		RadioService radioService = new RadioService();
		while(true){
			logger.debug("开始查询需要同步电台数据..................");
			long times_start = System.currentTimeMillis();
			List<Radio> radios = radioService.listRadioForsync(BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE);
			long times_end = System.currentTimeMillis();
			logger.debug("本次查询0~"+ BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE +"电台数据花费时间(ms):::" + (times_end - times_start));
			if(radios == null){
				logger.debug("获取要同步的电台数据为空,结束同步操作............");
				break;
			}
			ThreadPoolExecutor pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(ThreadConstans.THREAD_POOL_COUNT);
			for (Radio radio : radios) {
				Runnable runnable = new SyncRadioRunnable(radio);
				pool.execute(runnable);
//				if(radio == null){
//					return;
//				}
//				boolean flag = radioService.syncRadioToServer(radio);
//				if(flag ){
//					radio.setSyncFlag(true);
//					radioService.updateFlagAndAudios(radio);
//				}
			}
			pool.shutdown();
			while (!pool.isTerminated()) {
				try {
					pool.awaitTermination(5, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
					logger.error(e.getMessage(),e);
				}
			}
		}
		logger.debug("结束查询需要同步电台数据..................");
	}
}
