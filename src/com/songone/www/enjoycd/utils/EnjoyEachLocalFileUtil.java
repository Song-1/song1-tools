/**
 * 
 */
package com.songone.www.enjoycd.utils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.StringHolder;

import com.songone.www.aliyun.AliyunUtil;
import com.songone.www.aliyun.exceptions.AliyunObjectKeyIllegalException;
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
public class EnjoyEachLocalFileUtil {
	private static final Logger logger = LogManager.getLogger(EnjoyEachLocalFileUtil.class);

	public static void main(String[] args) {
		SystemUtil.init();
//		start();
		long start = System.currentTimeMillis();
		eachFiles();
		long end = System.currentTimeMillis();
		System.out.println("cost times :::: " + (end - start));
//		String path = "G:\\上传盘\\享CD";
//		File file = new File(path);
//		int counts = eachFiles(file,0);
//		System.out.println(counts);

	}

	public static void start() {
		Timer timer = new Timer("TIMER-ENJOYCD_EACH");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				eachFiles();
			}
		}, 0L, EnjoyCDConstants.TIME_INTERVAL_TIMES);
	}

	private static LocalFileDataInfoService localFileDataInfoService = new LocalFileDataInfoService();

	public static void eachFiles() {
		File file = new File(EnjoyCDConstants.ENJOY_CD_LOCAL_FILE_PATH);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		File[] files = file.listFiles();
		if (files == null) {
			return;
		}
		for (File f : files) {
			if (f == null) {
				return;
			}
			String name = f.getName();
			String[] strs = name.split("-");
			String createDate = "";
			String createUser = "";
			if (strs != null && strs.length == 2) {
				createDate = strs[0];
				createUser = strs[1];
			}
			String basePath = f.getAbsolutePath();
			LocalFileDataInfo model = localFileDataInfoService.ListModelByFilePath(basePath);
			if (model != null) {
				int counts = localFileDataInfoService.getFileCountsByParentId(model.getId());
				int localFileCounts = eachFiles(f);
				if(counts == localFileCounts){
					logger.debug("[FilePath::" + basePath + "]下的文件已保存到数据库,跳过文件扫描..............");
					continue;
				}
			}else{
				model = saveModelData(basePath, createDate, createUser, null, null, 0, true, false);
			}
			StringHolder strh = new StringHolder("");
			printFiles(f, null, createDate, createUser, basePath, model.getId(), strh,0);
			String infoData = strh.value;
			if (!StringUtil.isEmptyString(infoData)) {
				model.setInfoData(infoData);
				localFileDataInfoService.update(model);
			}
		}
	}
	
	private static int eachFiles(File file,int index){
		if(file == null){
			return 0;
		}
		if(!file.exists()){
			return 0;
		}
		if(file.isDirectory()){
			index += 1;
			File[] files = file.listFiles();
			if(files == null){
				return 0;
			}
			int counts = 0;
			for (File f : files) {
				counts += eachFiles(f,index);
			}
			if(index == 5){
				System.out.println("AlbumName:::" + file.getName() + "  AlbumSongs:::" + counts);
			}
			return counts;
		}else if(file.isFile()){
			return 1;
		}else{
			return 0;
		}
	}
	
	private static int eachFiles(File file){
		if(file == null){
			return 0;
		}
		if(!file.exists()){
			return 0;
		}
		if(file.isDirectory()){
			File[] files = file.listFiles();
			if(files == null){
				return 0;
			}
			int counts = 0;
			for (File f : files) {
				counts += eachFiles(f);
			}
			return counts;
		}else if(file.isFile()){
			return 1;
		}else{
			return 0;
		}
	}

	private static int printFiles(File file, String name, String createDate, String createUser, String basePath, int parentId, StringHolder strh,int index) {
		if (file == null) {
			return 0;
		}else if(!file.exists()){
			return 0;
		}
		if (StringUtil.isEmptyString(name)) {
			name = "";
		}
		if (file.isDirectory()) {
			index += 1;
			File[] files = file.listFiles();
			if (files == null) {
				return 0;
			}
			if (!basePath.endsWith(file.getName())) {
				name += ";" + file.getName();
			}
			int counts = 0;
			for (File f : files) {
				counts += printFiles(f, name, createDate, createUser, basePath, parentId, strh,index);
			}
			if(index == 4){
				if (name.startsWith(";")) {
					name = new String(name.substring(1));
				}
				saveAlbumData(name, createDate, createUser, counts);
			}
			return counts;
		} else if(file.isFile()) {
			int result = 0;
			String path = file.getAbsolutePath();
			if(SaveEnjoyCDDataUtil.matchSttfix(path)){
				result = 1;
			}
			if (name.startsWith(";")) {
				name = new String(name.substring(1));
			}
			createIcon(file, basePath, createDate, createUser, name, parentId, strh);
			String aliyunKey = formateAliyunKey(path, basePath);
			logger.debug("FilePath::" + path + "|||Key:::" + aliyunKey);
			saveData(path, aliyunKey, createDate, createUser, name, parentId, strh);
			return result;
		}else{
			return 0;
		}

	}
	private static SyncEnjoyCDModelService syncEnjoyCDModelService = new SyncEnjoyCDModelService();
	/**
	 * 保存专辑数据
	 * @param albumName
	 * @param createDate
	 * @param createUser
	 * @param counts
	 */
	private static void saveAlbumData(String albumName,String createDate, String createUser,int counts){
		SyncEnjoyCDModel model = syncEnjoyCDModelService.getModelByName(albumName,true);
		if (model == null) {
			model = new SyncEnjoyCDModel();
			model.setName(albumName);
			model.setAlbum(true);
			model.setSeat(counts);
			model.setCreateDate(createDate);
			model.setCreateUser(createUser);
			syncEnjoyCDModelService.save(model);
		} 
	}

	private static void saveData(String path, String aliyunKey, String createDate, String createUser, String indoData, int parentId, StringHolder strh) {
		LocalFileDataInfo model = localFileDataInfoService.ListModelByFilePath(path);
		if(model != null && model.getId() > 0){
			return ;
		}
		boolean falg = false;
		try {
			falg = AliyunUtil.isExistObjectForTheKey(BaseConstants.ALIYUN_BUCKET, aliyunKey);
		} catch (AliyunObjectKeyIllegalException e) {
			strh.value += "FilePath::" + path + "|||Key:::" + aliyunKey + "(key不合法);";
			return;
		}
		saveModelData(path, createDate, createUser, aliyunKey, indoData, parentId, false, falg);
	}

	private static void createIcon(File file, String basePath, String createDate, String createUser, String infoData, int parentId, StringHolder strh) {
		String path = file.getAbsolutePath();
		if (!ImageDoUtil.isImageFile(path)) {
			return;
		}
		String name = file.getName();
		String stffix = new String(name.substring(name.lastIndexOf(".")));
		String parentName = file.getParentFile().getName() + stffix;
		if (!name.equals(parentName)) {
			return;
		}
		String iconName = file.getParentFile().getName() + "(300x300)" + stffix;
		path = path.replace(name, iconName);
		File f = new File(path);
		if (f.exists()) {
			return;
		}
		logger.debug(file.getAbsolutePath());
		String iconPath = ImageDoUtil.cutImage(file);
		if(StringUtil.isEmptyString(iconPath)){
			return;
		}
		String aliyunKey = formateAliyunKey(iconPath, basePath);
		logger.debug("FilePath::" + iconPath + "|||Key:::" + aliyunKey);
		saveData(iconPath, aliyunKey, createDate, createUser, infoData, parentId, strh);
	}

	private static LocalFileDataInfo saveModelData(String path, String createDate, String createUser, String aliyunKey, String indoData, int parentId, boolean isBasePath, boolean isUpload) {
		LocalFileDataInfo model = new LocalFileDataInfo();
		model.setBasePath(isBasePath);
		model.setFilePath(path);
		model.setFileType("enjoycd");
		model.setInfoData(indoData);
		model.setCreateDate(createDate);
		model.setCreateUser(createUser);
		model.setAliyunKey(aliyunKey);
		model.setParentId(parentId);
		model.setUpload(isUpload);
		localFileDataInfoService.save(model);
		return model;
	}

	private static String formateAliyunKey(String path, String basePath) {
		String key = path.replace(basePath, "");
		String separator = File.separator;
		if (key.startsWith(separator)) {
			key = new String(key.substring(separator.length()));
		} else if (key.startsWith("/")) {
			key = new String(key.substring(1));
		}
		key = key.replace(separator, "/");
		key = EnjoyCDConstants.ENJOY_CD_ALIYUN_BASE_KEY + key;
		return key;
	}

}
