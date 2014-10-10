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
import com.songone.www.lizhi.json.model.RadioAudio;
import com.songone.www.lizhi.json.runnable.SyncRadioAudioDataRunnable;
import com.songone.www.lizhi.json.service.RadioAudioService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class SyncRadioAudioDatasMain {
	private static final Logger logger = LogManager.getLogger(SyncRadioAudioDatasMain.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SystemUtil.init();
		syncAudioDatasStart();
	}
	
	public static void syncAudioDatasStart(){
		new Thread(new Runnable() {
			public void run() {
				long startTimes = 0L; // ms
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						syncAudioDatas();
					}
				}, startTimes, ThreadConstans.TIME_INTERVAL_TIMES);
			}
		}).start();
	}
	
	public static void syncAudioDatas(){
		RadioAudioService radioAudioService = new RadioAudioService();
		int start = 0;
		while(true){
			PageResultModel<RadioAudio> page = radioAudioService.listAudioByPageForSync(start, BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE) ;
			List<RadioAudio> radioAudios = page.getDatas();
			if(radioAudios == null){
				break;
			}
			ThreadPoolExecutor pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(ThreadConstans.THREAD_POOL_COUNT);
			for (RadioAudio audio : radioAudios) {
				Runnable runnable = new SyncRadioAudioDataRunnable(audio);
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
