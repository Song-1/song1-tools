/**
 * 
 */
package com.songone.www.makesound.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.utils.ConsoleInputUtil;
import com.songone.www.base.utils.SystemUtil;
import com.songone.www.makesound.model.Radio;
import com.songone.www.makesound.model.RadioAudio;
import com.songone.www.makesound.model.RadioTag;
import com.songone.www.makesound.service.RadioAudioService;
import com.songone.www.makesound.service.impl.RadioAudioServiceImpl;
import com.songone.www.makesound.util.RadioDataSyncUtil;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class SyncDataMian {
	private static final Logger logger = LogManager.getLogger(SyncDataMian.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SystemUtil.init();
		menu();
		// List<RadioTag> tags = RadioDataSyncUtil.getRadioTags();
		// printTags(tags);
		
	}

	public static void printTags(List<RadioTag> tags) {
		if (tags != null) {
			for (RadioTag tag : tags) {
				logger.info(tag);
				List<RadioTag> radioTags = tag.getTags();
				printTags(radioTags);
			}
		}
	}

	private static void menu() {
		Map<Integer, String> menu = new HashMap<Integer, String>();
		menu.put(1, "同步电台分类数据");
		menu.put(2, "同步电台数据");
		menu.put(3, "同步电台音频数据");
		menu.put(4, "保存电台音频数据到本地");
		menu.put(5, "同步本地电台音频数据到音乐1号");
		menu.put(0, "退出");
		System.out.println("===== 同步荔枝FM数据操作 ======");
		for (Map.Entry<Integer, String> entry : menu.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		int key = ConsoleInputUtil.getInt("请选择操作项 : ");
		switch (key) {
		case 1:
			syncRadioTags();
			break;
		case 2:
			syncRadios();
			break;
		case 3:
			RadioDataSyncUtil.syncAudioDatas();
			System.out.println("[INFO-OUT]同步电台音频数据结束");
			menu();
			break;
		case 4:
			RadioDataSyncUtil.saveRadioAudioDatas();
			System.out.println("[INFO-OUT]保存电台音频数据结束");
			menu();
			break;
		case 5:
			break;
		case 0:
			SystemUtil.exit(0);
		default:
			SystemUtil.exit(0);
		}
	}
	
	private static void syncRadioTags(){
		List<RadioTag> tags = RadioDataSyncUtil.getRadioTagsData();
		RadioDataSyncUtil.syncCategoryDatas(tags);
		System.out.println("[INFO-OUT]同步电台分类数据结束");
		menu();
	}
	
	private static void syncRadios(){
		List<RadioTag> tags = RadioDataSyncUtil.getRadioTags();
		if (tags != null) {
			for (RadioTag radioTag : tags) {
				if(radioTag != null){
					List<RadioTag> tagList = radioTag.getTags();
					if(tagList != null){
						for (RadioTag tag : tagList) {
							RadioDataSyncUtil.getRadioDatasByTag(tag);
						}
					}
				}
			}
		}else{
			System.out.println("[INFO-OUT]电台分类数据为空");	
		}
		System.out.println("[INFO-OUT]同步电台数据结束");
		menu();
	}
	
	private class SyncRadioAudioToSongOne implements Runnable{
		@Override
		public void run() {
			int start = 0;
			int pageSize = RadioDataSyncUtil.PAGE_SIZE_FOR_SELECT;
			RadioAudioService radioAudioService = new RadioAudioServiceImpl();
			while(true){
				List<RadioAudio> audios = radioAudioService.listSyncAudioDatas(start, pageSize);
				if(audios == null){
					break;
				}
				for (RadioAudio audio : audios) {
					
				}
				start+= pageSize;
			}
			
		}
		
		private void syncRadio(String band){
			Radio radio = RadioDataSyncUtil.getRadioData(band);
			if(radio == null){
				return;
			}
//			RadioDataSyncUtil.syncProgramListDatas(radio, categoryId, isReSend);
		}
		
	}
}
