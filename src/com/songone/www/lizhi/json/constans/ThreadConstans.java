/**
 * 
 */
package com.songone.www.lizhi.json.constans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.songone.www.base.utils.CacheUtil;
import com.songone.www.lizhi.json.service.RadioService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class ThreadConstans {
	public static final int RADIO_BAND_END = 1000000;
	public static int LIZHI_RADIO_BAND = 10000;
	public static int SAVE_RADIOAUDIO_DATAS_START = 0;
	// // 获取或保存电台节目数据失败的电台信息记录集合
	private static final int GETORSAVEAUDIO_FAIL_RADIO_LIST_SIZE = 1000;
	private static final String GETORSAVEAUDIO_FAIL_RADIO_LIST_MAP_CACHE_KEY = "GETORSAVEAUDIO_FAIL_RADIO_LIST_MAP";
	private static List<Integer> GETORSAVEAUDIO_FAIL_RADIO_LIST = new ArrayList<Integer>(GETORSAVEAUDIO_FAIL_RADIO_LIST_SIZE);
	private static int GETORSAVEAUDIO_FAIL_RADIO_LIST_MAP_INDEX = 1;

	public static final String ERROR_USER_NAME_VALUE_MUST_FORMATE = "for column 'user_name'";
	public static final String ERROR_DESC_VALUE_MUST_FORMATE = "for column 'desc_text'";
	public static final String ERROR_NAME_VALUE_MUST_FORMATE = "for column 'name'";

	public static boolean test_flag = true;
	
	public static final long TIME_INTERVAL_TIMES = 60 * 60 * 1000L; // ms

	public static void init() {
		RadioService radioService = new RadioService();
		int currentBand = radioService.getCurrentMaxBand();
		if (LIZHI_RADIO_BAND < currentBand) {
			LIZHI_RADIO_BAND = currentBand + 1;
		}
	}

	/**
	 * 新增获取或保存电台节目数据失败的电台band
	 * 
	 * @param band
	 */
	@SuppressWarnings("unchecked")
	public static synchronized void addDoFailRadioBandForAudio(int band) {
		if (band <= 0) {
			return;
		}
		int size = GETORSAVEAUDIO_FAIL_RADIO_LIST.size();
		if (size >= GETORSAVEAUDIO_FAIL_RADIO_LIST_SIZE) {
			List<Integer> list = GETORSAVEAUDIO_FAIL_RADIO_LIST;
			Map<Integer, List<Integer>> map = (Map<Integer, List<Integer>>) CacheUtil.getElement(GETORSAVEAUDIO_FAIL_RADIO_LIST_MAP_CACHE_KEY);
			if (map == null) {
				map = new TreeMap<Integer, List<Integer>>();
			}
			map.put(GETORSAVEAUDIO_FAIL_RADIO_LIST_MAP_INDEX, list);
			CacheUtil.addElement(GETORSAVEAUDIO_FAIL_RADIO_LIST_MAP_CACHE_KEY, map);
			GETORSAVEAUDIO_FAIL_RADIO_LIST_MAP_INDEX += 1;
			GETORSAVEAUDIO_FAIL_RADIO_LIST = new ArrayList<Integer>(GETORSAVEAUDIO_FAIL_RADIO_LIST_SIZE);
		}
		GETORSAVEAUDIO_FAIL_RADIO_LIST.add(band);
	}

	/**
	 * 电台band自加
	 */
	public static synchronized void radioBandPlus() {
		LIZHI_RADIO_BAND += 1;
	}

	/**
	 * 获取电台band
	 * 
	 * @return
	 */
	public static synchronized int getRadioBand() {
		int band = LIZHI_RADIO_BAND;
		if (band >= RADIO_BAND_END) {
			test_flag = false;
			LIZHI_RADIO_BAND = 10000;
			return -1;
		}
		radioBandPlus();
		return band;
	}


	public static int THREAD_POOL_COUNT = 20;
	public static ThreadPoolExecutor pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(THREAD_POOL_COUNT);

	public static synchronized void putJob(Runnable runnable) {
		if (runnable == null) {
			return;
		}
//		int activeCounts = pool.getActiveCount();
//		do{
//			activeCounts = pool.getActiveCount();
//		}while(activeCounts == THREAD_POOL_COUNT);
		pool.execute(runnable);
	}

	public static void closeThreadPool() {
		pool.shutdown();
		while (!pool.isTerminated()) {
			try {
				pool.awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
