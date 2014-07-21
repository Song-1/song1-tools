/**
 * 
 */
package com.upload.aliyun;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.upload.aliyun.runnable.enjoy.EnjoyFileEachUtil;
import com.upload.aliyun.runnable.music.GetMusicTypeFromExcel;
import com.upload.aliyun.util.FileDoUtil;
import com.upload.aliyun.util.JavascriptUtil;
import com.upload.aliyun.util.OSSUploadUtil;
import com.upload.aliyun.util.POIUtil;

/**
 * @author Administrator
 *
 */
public class StartMain {
	private static Map<String, List<File>> fileMaps = new TreeMap<String, List<File>>();

	public static void main(String[] args) {
		try {
			// /// 加载配置文件
			MusicConstants.loadConfig();
			OSSUploadUtil.init();
			JavascriptUtil.init();
			//
			if("enjoy".equals(MusicConstants.DO_TYPE)){
				EnjoyFileEachUtil.doEnjoy();
				return;
			}else if ("rename".equals(MusicConstants.DO_TYPE)) {
				List<OSSObjectSummary> listObject = OSSUploadUtil.listObject("cherrytime", MusicConstants.SERVER_PATH_ROOT);
				for (OSSObjectSummary ossObjectSummary : listObject) {
					System.out.println(ossObjectSummary.getKey());
				}
				return;
			}else if("music".equals(MusicConstants.DO_TYPE)){
				File file = new File(MusicConstants.MUSIC_TIME_TYPE_MAPPING_FILE_PATH);
				if(file.exists()){
					String name = file.getName();
					if(name.endsWith(".xls") ||name.endsWith(".xlsx")){
						new GetMusicTypeFromExcel().doExcel(file);
					}
				}else{
					System.out.println("歌单分类文件不存在");
					return ;
				}
			}
			///
			eachFiles();
			doFile();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void eachFiles() {
		File file = new File(MusicConstants.BASE_FILE_PATH);
		FileDoUtil.outLog("本地根目录："+MusicConstants.BASE_FILE_PATH);
		getFile(file);

	}

	private static File getFile(File file) {
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null) {
					for (File f : files) {
						getFile(f);
					}
				}
			} else if (file.isFile()) {
				String name = file.getName();
				if(name.endsWith(".xls") ||name.endsWith(".xlsx")){
					POIUtil poiutil = DoExcelFactory.getBean();
					if(poiutil != null){
						poiutil.doExcel(file);
					}
				}else{
					String filePath = file.getParent();
					filePath = filePath.replace(MusicConstants.BASE_FILE_PATH + File.separator, "");
					if (fileMaps.get(filePath) == null) {
						List<File> fileList = new ArrayList<File>();
						fileList.add(file);
						fileMaps.put(filePath, fileList);
					} else {
						List<File> fileList = fileMaps.get(filePath);
						fileList.add(file);
					}
				}
			}
		}
		return null;
	}

	public static void doFile() {
		//ExecutorService pool = Executors.newFixedThreadPool(10);
		for (Map.Entry<String, List<File>> entry : fileMaps.entrySet()) {
			String fileListName = entry.getKey();
			List<File> fileList = entry.getValue();
			if (fileList == null) {
				continue;
			}
			FileDoUtil.outLog(fileListName);
			long start = System.currentTimeMillis();
			Runnable runnable = DoSaveFactory.getBean(fileListName, fileList);
			if(runnable != null){
				runnable.run();
			}
			long end = System.currentTimeMillis();
			long times = (end - start) /1000;
			FileDoUtil.outLog(fileListName + " \t cost:::" + times +"秒\t\n");
			//pool.execute(new BooKListThread(fileListName, fileList));			
		}
//		pool.shutdown();
//		while (!pool.isTerminated()) {
//			try {
//				pool.awaitTermination(5, TimeUnit.SECONDS);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	


}
