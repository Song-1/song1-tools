/**
 * 
 */
package com.upload.aliyun;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.upload.aliyun.runnable.BooKListThread;
import com.upload.aliyun.util.JavascriptUtil;
import com.upload.aliyun.util.OSSUploadUtil;

/**
 * @author Administrator
 *
 */
public class StartMain {
	private static Map<String, List<File>> fileMaps = new TreeMap<String, List<File>>();

	public static void main(String[] args) {
		try {
			// // 初始化操作
			// /// 加载配置文件
			MusicConstants.loadConfig();
			OSSUploadUtil.init();
			JavascriptUtil.init();
			//
			eachFiles();
			doFile();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void eachFiles() {
		File file = new File(MusicConstants.BASE_FILE_PATH);
		System.out.println(MusicConstants.BASE_FILE_PATH);
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
			System.out.println(fileListName);
			long start = System.currentTimeMillis();
			new BooKListThread(fileListName, fileList).run();
			long end = System.currentTimeMillis();
			long times = (end - start) /1000;
			System.out.println(fileListName + " \t cost:::" + times);
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
