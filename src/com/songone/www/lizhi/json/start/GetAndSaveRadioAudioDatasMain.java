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

import com.songone.www.base.db.mybatis.PageResultModel;
import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.SystemUtil;
import com.songone.www.lizhi.json.constans.ThreadConstans;
import com.songone.www.lizhi.json.model.Radio;
import com.songone.www.lizhi.json.runnable.GetAndSaveAudioRunnable;
import com.songone.www.lizhi.json.service.RadioService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class GetAndSaveRadioAudioDatasMain {
	private static final Logger logger = LogManager.getLogger(GetAndSaveRadioAudioDatasMain.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SystemUtil.init();
		getAndSaveRadioAudioDatasStart();
	}

	public static void getAndSaveRadioAudioDatasStart() {
		new Thread(new Runnable() {
			public void run() {
				long startTimes = 0L; // ms
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						getAndSaveRadioAudioDatas();
					}
				}, startTimes, ThreadConstans.TIME_INTERVAL_TIMES);
			}
		}).start();
	}

	public static void getAndSaveRadioAudioDatas(){
		RadioService radioService = new RadioService();
		while(true){
			PageResultModel<Radio> page = radioService.listRadioByPageForDoRadioAudio(0, BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE);
			List<Radio> radios = page.getDatas();
			if(radios == null){
				break;
			}
			ThreadPoolExecutor pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(ThreadConstans.THREAD_POOL_COUNT);
			for (Radio radio : radios) {
				Runnable runnable = new GetAndSaveAudioRunnable(radio);
				pool.execute(runnable);
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
	}
}
