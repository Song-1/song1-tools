/**
 * 
 */
package com.songone.www.lizhi.json.start;

import java.util.Timer;
import java.util.TimerTask;

import com.songone.www.base.utils.SystemUtil;
import com.songone.www.lizhi.json.constans.ThreadConstans;
import com.songone.www.lizhi.json.runnable.GetAndSaveRadioRunnable;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class GetAndSaveRadioDatasMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SystemUtil.init();
		getAndSaveRadioDatasStart();
	}

	public static void getAndSaveRadioDatasStart() {
		new Thread(new Runnable() {
			public void run() {
				long startTimes = 0L; // ms
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						getAndSaveRadioDatas();
					}
				}, startTimes, ThreadConstans.TIME_INTERVAL_TIMES);
			}
		}).start();
	}

	public static void getAndSaveRadioDatas() {
		// 获取荔枝FM的电台数据并保存
		for (int i = 0; i < 10; i++) {
			GetAndSaveRadioRunnable runnable = new GetAndSaveRadioRunnable();
			new Thread(runnable).start();
		}
	}
}
