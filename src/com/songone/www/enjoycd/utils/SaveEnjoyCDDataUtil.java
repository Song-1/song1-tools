/**
 * 
 */
package com.songone.www.enjoycd.utils;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.ImageDoUtil;
import com.songone.www.base.utils.StringUtil;
import com.songone.www.base.utils.SystemUtil;
import com.songone.www.enjoycd.constants.EnjoyCDConstants;
import com.songone.www.enjoycd.models.LocalFileDataInfo;
import com.songone.www.enjoycd.models.SyncEnjoyCDModel;
import com.songone.www.enjoycd.service.LocalFileDataInfoService;
import com.songone.www.enjoycd.service.SyncEnjoyCDModelService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class SaveEnjoyCDDataUtil {

	public static void main(String[] args) {
		SystemUtil.init();
		eachEnjopyDatasToSave();
	}

	private static final Logger logger = LogManager.getLogger(EnjoyEachLocalFileUtil.class);
	private static LocalFileDataInfoService localFileDataInfoService = new LocalFileDataInfoService();
	private static SyncEnjoyCDModelService syncEnjoyCDModelService = new SyncEnjoyCDModelService();
	public static final String SONGFILEREG = "(\\d+)\\s+-{1}\\s+(.+[.])";

	public static void start() {
		long times = 10 * EnjoyCDConstants.TIME_INTERVAL_TIMES_UNIT;
		Timer timer = new Timer("TIMER-ENJOYCD_SAVE");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				eachEnjopyDatasToSave();
			}
		}, EnjoyCDConstants.TIME_INTERVAL_TIMES_UNIT, times);
	}

	private static void eachEnjopyDatasToSave() {
		logger.debug(" 开始享CD数据遍历...........");
		while (true) {
			List<LocalFileDataInfo> datas = localFileDataInfoService.listModelForSyncEnjoyCD(BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE);
			if (datas == null || datas.isEmpty()) {
				logger.debug("结束享CD数据遍历...........");
				break;
			}
			for (LocalFileDataInfo model : datas) {
				if (model == null) {
					continue;
				}
				logger.debug(model);
				boolean saveFailFlag = true;
				String key = model.getAliyunKey();
				String name = getName(key);
				String path = model.getFilePath();
				int parentId = saveAlbumData(model.getInfoData(), model.getCreateDate(), model.getCreateUser());
				SyncEnjoyCDModel enjoy = new SyncEnjoyCDModel();
				enjoy.setParentId(parentId);
				if (parentId == -1) {
					enjoy.setName(name);
					enjoy.setUrl(key);
					enjoy.setParentId(0);
					syncEnjoyCDModelService.save(enjoy);
					model.setSyncFail(true);
					model.setSyncDataFlag(true);
					localFileDataInfoService.update(model);
					continue;
				}else if(parentId <= 0){
					model.setSyncFail(saveFailFlag);
					model.setSyncDataFlag(true);
					localFileDataInfoService.update(model);
					continue;
				}
				if (isIcon(path) || isCover(path)) {
					enjoy.setName(name);
					enjoy.setUrl(key);
					syncEnjoyCDModelService.save(enjoy);
					saveFailFlag = false;
				} else if (matchSttfix(path)) {
					Pattern p = Pattern.compile(SONGFILEREG);
					Matcher m = p.matcher(name);
					if (m.find()) {
						String seatStr = m.group(1).trim();
						String songname = m.group(2).trim();
						name = new String(name.substring(0, name.lastIndexOf(".")));
						enjoy.setName(songname);
						enjoy.setUrl(model.getAliyunKey());
						enjoy.setSeat(Integer.parseInt(seatStr));
						syncEnjoyCDModelService.save(enjoy);
						saveFailFlag = false;
					}
				}
				model.setSyncFail(saveFailFlag);
				model.setSyncDataFlag(true);
				localFileDataInfoService.update(model);
			}
		}
	}

	/**
	 * 判断是否是专辑的封面图片
	 * 
	 * @param path
	 * @return
	 */
	private static boolean isCover(String path) {
		if (!ImageDoUtil.isImageFile(path)) {
			return false;
		}
		File file = new File(path);
		if (!file.exists()) {
			return false;
		}
		String name = file.getName();
		String stffix = new String(name.substring(name.lastIndexOf(".")));
		String albumName = file.getParentFile().getName();
		String coverName = albumName + stffix;
		if (name.equals(coverName)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是专辑的小图片
	 * 
	 * @param path
	 * @return
	 */
	private static boolean isIcon(String path) {
		if (!ImageDoUtil.isImageFile(path)) {
			return false;
		}
		File file = new File(path);
		if (!file.exists()) {
			return false;
		}
		String name = file.getName();
		String stffix = new String(name.substring(name.lastIndexOf(".")));
		String albumName = file.getParentFile().getName();
		String iconName = albumName + "(300x300)" + stffix;
		if (name.equals(iconName)) {
			return true;
		}
		return false;
	}

	public static boolean matchSttfix(String name) {
		name = name.toLowerCase();
		for (String s : EnjoyCDConstants.ALBUM_SONG_STUFFIXS) {
			if (name.endsWith(s)) {
				return true;
			}
		}
		return false;
	}

	private static String getName(String name) {
		int index = name.lastIndexOf("/");
		if (index > 0) {
			return new String(name.substring(index + 1));
		}
		return name;
	}

	public static int saveAlbumData(String albumName, String createDate, String createUser) {
		if (StringUtil.isEmptyString(albumName)) {
			return 0;
		} else if (albumName.startsWith(";")) {
			albumName = new String(albumName.substring(1));
		}
		int parentId = 0;
		String[] strs = albumName.split(";");
		int size = strs.length;
		if (strs == null || size > 3 || size <= 1) {
			return parentId;
		}else if(size == 2){
			return -1;
		}
		SyncEnjoyCDModel model = syncEnjoyCDModelService.getModelByName(albumName,true);
		if (model == null) {
			model = new SyncEnjoyCDModel();
			model.setName(albumName);
			model.setAlbum(true);
			model.setCreateDate(createDate);
			model.setCreateUser(createUser);
			parentId = syncEnjoyCDModelService.save(model);
		} else {
			parentId = model.getId();
		}
		return parentId;
	}

}
