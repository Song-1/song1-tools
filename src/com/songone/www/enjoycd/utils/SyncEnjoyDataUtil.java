/**
 * 
 */
package com.songone.www.enjoycd.utils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.utils.ImageDoUtil;
import com.songone.www.base.utils.StringUtil;
import com.songone.www.base.utils.SystemUtil;
import com.songone.www.enjoycd.constants.EnjoyCDConstants;
import com.songone.www.enjoycd.models.SyncEnjoyCDModel;
import com.songone.www.enjoycd.service.SyncEnjoyCDModelService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class SyncEnjoyDataUtil {

	public static void main(String[] args) {
		SystemUtil.init();
		syncEnjoyDatas();
	}

	private static final Logger logger = LogManager.getLogger(EnjoyEachLocalFileUtil.class);
	private static SyncEnjoyCDModelService syncEnjoyCDModelService = new SyncEnjoyCDModelService();
	public static final String SONGFILEREG = "(\\d+)\\s+-{1}\\s+(.+[.])";

	public static void start() {
		long times = 10 * EnjoyCDConstants.TIME_INTERVAL_TIMES_UNIT;
		Timer timer = new Timer("TIMER-ENJOYCD_SYNC");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				syncEnjoyDatas();
			}
		}, EnjoyCDConstants.TIME_INTERVAL_TIMES_UNIT, times);
	}

	private static void syncEnjoyDatas() {
		logger.debug(" 开始享CD专辑遍历...........");
		while (true) {
			List<SyncEnjoyCDModel> datas = syncEnjoyCDModelService.listAlbumForSync(20);
			if (datas == null || datas.isEmpty()) {
				logger.debug("结束享CD专辑遍历...........");
				break;
			}
			for (SyncEnjoyCDModel model : datas) {
				if (model == null) {
					continue;
				}
				syncAlbunDatas(model);
			}
		}
	}

	private static void syncAlbunDatas(SyncEnjoyCDModel album) {
		if (album == null) {
			return;
		} else if (!album.isAlbum()) {
			return;
		}
		logger.debug(album);
		String str = album.getName();
		String[] strs = str.split(";");
		int size = strs.length;
		if (strs == null || size != 3) {
			return;
		}
		String createDate = album.getCreateDate();
		String createUser = album.getCreateUser();
		String albumName = "";
		String styleName = "";
		String singerName = "";
		String icon = null;
		String img = null;
		albumName = strs[2];
		singerName = strs[1];
		styleName = strs[0];
		List<SyncEnjoyCDModel> datas = syncEnjoyCDModelService.listAlbumDatas(album.getId());
		if (datas == null || datas.isEmpty()) {
			return;
		}
		StringBuilder strb = new StringBuilder(100);
		int counts = 0;
		for (SyncEnjoyCDModel model : datas) {
			if (model == null) {
				continue;
			}
			logger.debug(model);
			String key = model.getUrl();
			if (ImageDoUtil.isImageFile(key)) {
				if (key.endsWith("(300x300).jpg")) {
					icon = key;
				} else {
					img = key;
				}
			} else {
				boolean syncFlag = model.isSyncFlag();
				boolean syncFail = model.isSyncFail();
				boolean flag = !syncFlag || (syncFlag && syncFail);
				if(flag){
					String songId = syncEnjoyCDModelService.syncAlbumSong(model, createDate, createUser);
					if (StringUtil.isEmptyString(songId)) {
						model.setSyncFail(true);
					}
					strb.append(songId + ",");
				}
				counts += 1;
			}
			model.setSyncFlag(true);
			syncEnjoyCDModelService.update(model);
		}
		String adds = strb.toString();
		String singerId = syncEnjoyCDModelService.syncAlbumSinger(singerName,getSingerImg(singerName),getSingerIcon(singerName));
		String styleId = syncEnjoyCDModelService.syncAlbumStyle(styleName);
		boolean flag = syncEnjoyCDModelService.syncAlbum(albumName, img, icon, singerId, styleId, adds, createDate, createUser);
		if(counts == album.getSeat()){
			album.setSyncFlag(true);
		}
		if(!flag){
			album.setSyncFail(true);
		}else{
			album.setSyncFail(false);
		}
		syncEnjoyCDModelService.update(album);
	}
	
	private static String getSingerImg(String singerName){
		String imgName = singerName + ".jpg";
		SyncEnjoyCDModel model = syncEnjoyCDModelService.getModelByName(imgName,false);
		if(model != null){
			return model.getUrl();
		}
		return null;
	}
	
	private static String getSingerIcon(String singerName){
		String imgName = singerName + "(300x300).jpg";
		SyncEnjoyCDModel model = syncEnjoyCDModelService.getModelByName(imgName,false);
		if(model != null){
			return model.getUrl();
		}
		return null;
	}

}