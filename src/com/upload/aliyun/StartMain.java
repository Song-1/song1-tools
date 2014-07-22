/**
 * 
 */
package com.upload.aliyun;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.tools.song1.aliyun.OSSUploadUtil;
import com.upload.aliyun.runnable.enjoy.EnjoyFileEachUtil;
import com.upload.aliyun.runnable.music.GetMusicTypeFromExcel;
import com.upload.aliyun.util.FileDoUtil;
import com.upload.aliyun.util.JavascriptUtil;
import com.upload.aliyun.util.POIUtil;
import com.upload.aliyun.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class StartMain {
	private static Map<String, List<File>> fileMaps = new TreeMap<String, List<File>>();
	private static final Map<Integer, String> GLOBAL_MENU_MAP = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 3832481997454745090L;
		{
			put(1, "樱桃时光");
			put(2, "状元听书");
			put(3, "享CD");
			put(4, "阿里云文件操作");
			put(5, "退出");
		}
	};
	private static final Map<Integer, String> ALIYUN_MENU_MAP = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 3832481997454745090L;
		{
			put(1, "复制文件");
			put(2, "移动文件");
			put(3, "删除文件");
			put(4, "查看文件");
			put(5, "返回");
		}
	};

	public static void main(String[] args) throws Exception {
		System.out.println(".............加载配置文件并初始化...................");
		// /// 加载配置文件
		MusicConstants.loadConfig();
		OSSUploadUtil.init();
		JavascriptUtil.init();
		System.out.println("................初始化 完成...................");
		//
		while (true) {
			System.out.println("=============== 请选择功能操作=================");
			for(Map.Entry<Integer, String> entry : GLOBAL_MENU_MAP.entrySet()){
				System.out.println(" 【"+entry.getKey().intValue()+"】 "+ entry.getValue());
			}
			int doIndex = convertInputStr("请输入你要选择的操作的编号:::");
			if(doIndex == 5){
				System.out.println("系统退出..................");
				break;
			}
			doStart(doIndex);
		}
	}

	@SuppressWarnings("resource")
	public static String inputStr(String str) {
		System.out.print(str);
		Scanner scanner = new Scanner(System.in);
		String value = scanner.nextLine();
		if(StringUtil.isEmptyString(value)){
			value = "";
		}
		value = value.trim();
		return value;
	}
	public static int convertInputStr(String str) {
		String intput = inputStr(str);
		while(StringUtil.isEmptyString(intput)){
			System.out.println("输入不能为空!");
			intput = inputStr(str);
		}
		boolean flag = true;
		int doIndex = 0;
		while(flag){
			try{
			 doIndex = Integer.parseInt(intput.trim());
			}catch(Exception e){
				System.out.println("输入的操作编号不对!");
			}
			if(doIndex > 0){
				break;
			}
			intput = inputStr(str);
		}
		return doIndex;
	}
	
	public static void doStart(int index){
		switch (index) {
		case 1:
			System.out.println("开始上传樱桃时光的数据...........");
			break;
		case 2:
			System.out.println("开始上传状元听书的数据...........");
			break;
		case 3:
			System.out.println("开始上传享CD的数据...........");
			EnjoyFileEachUtil.doEnjoy();
			break;
		case 4:
			while(true){
				System.out.println("=============== 请选择阿里云文件操作=================");
				for(Map.Entry<Integer, String> entry : ALIYUN_MENU_MAP.entrySet()){
					System.out.println(" 【"+entry.getKey().intValue()+"】 "+ entry.getValue());
				}
				int doIndex = convertInputStr("请输入你要选择的操作的编号:::");
				if(5 == doIndex){
					break;
				}else {
					aliyunDoStart(doIndex);
				}
			}
			break;

		default:
			System.out.println("没有此功能操作,请输入正确的功能操作编码...........");
			break;
		}
		
	}
	
	public static void aliyunDoStart(int doIndex){
		switch (doIndex) {
		case 1:
			System.out.println(" 开始阿里云文件复制,请进行操作配置 :::");
			String oldBucket = inputStr("请输入复制文件所属BUCKET::");
			String oldKey = inputStr("请输入复制文件路径::");
			String newBucket = inputStr("请输入复制到文件所属BUCKET::");
			String newKey = inputStr("请输入复制到文件路径::");
			OSSUploadUtil.modifyAliyunFloderName(oldBucket, oldKey, newBucket, newKey);
			break;
		case 2:
			System.out.println(" 开始阿里云文件移动,请进行操作配置 :::");
			String oldBucket2 = inputStr("请输入移动文件所属BUCKET::");
			String oldKey2 = inputStr("请输入移动文件路径::");
			String newBucket2 = inputStr("请输入移动到文件所属BUCKET::");
			String newKey2 = inputStr("请输入移动到文件路径::");
			OSSUploadUtil.modifyAliyunFloderName(oldBucket2, oldKey2, newBucket2, newKey2);
			///// delete
			break;
		case 3:
			System.out.println(" 开始阿里云文件删除,请进行操作配置 :::");
			String bucket = inputStr("请输入删除文件所属BUCKET::");
			String key = inputStr("请输入删除文件路径::");
			OSSUploadUtil.deleteKey(bucket, key);
			break;
		case 4:
			System.out.println(" 开始阿里云文件查看,请进行操作配置 :::");
			String bucket4 = inputStr("请输入查看文件所属BUCKET::");
			String key4 = inputStr("请输入查看文件路径::");
			key4 = key4.trim();
			if(!key4.endsWith("/")){
				key4 += "/";
			}
			String index = "-";
			while(true){
				List<String> list = new ArrayList<String>();
				List<OSSObjectSummary> fileList = new ArrayList<OSSObjectSummary>();
				OSSUploadUtil.listAliyunFloder(bucket4, key4, list, fileList);
				System.out.println(key4);
				System.out.println(index+"【功能】 ..");
				for (String string : list) {
					if(string.endsWith("/")){
						string = new String(string.substring(0,string.length()-1));
					}
					int i  = string.lastIndexOf("/");
					if(i > 0){
						string = new String(string.substring(i + 1));
					}
					System.out.println(index+"【文件夹】 "+string);
				}
				for (OSSObjectSummary os : fileList) {
					String osName = os.getKey();
					int i  = osName.lastIndexOf("/");
					if(i > 0){
						osName = new String(osName.substring(i + 1));
					}
					System.out.println(index+"【文件】 "+osName);
				}
				System.out.println(index+"【功能】 exit");
				String inputStr = inputStr("请输入要查看文件夹路径(exit:退出;..:返回上层目录)::");
				inputStr = inputStr.trim();
				if("exit".equalsIgnoreCase(inputStr)){
					break;
				}
				if("..".equals(inputStr)){
					if(key4.endsWith("/")){
						key4 = new String(key4.substring(0,key4.length()-1));
					}
					int i  = key4.lastIndexOf("/");
					if(i > 0){
						key4 = new String(key4.substring(0,i + 1));
					}
					index = new String(index.substring(0, index.length() - 1));
				}else{
					key4 += inputStr + "/";
					index += "-";
				}
				if(!key4.endsWith("/")){
					key4 += "/";
				}
			}
			break;

		default:
			System.out.println("没有此功能操作,请输入正确的功能操作编码...........");
			break;
		}
		
	}

	public static void start() {
		try {

			if ("enjoy".equals(MusicConstants.DO_TYPE)) {
				EnjoyFileEachUtil.doEnjoy();
				return;
			} else if ("rename".equals(MusicConstants.DO_TYPE)) {
				List<OSSObjectSummary> listObject = OSSUploadUtil.listObject("cherrytime", MusicConstants.SERVER_PATH_ROOT);
				for (OSSObjectSummary ossObjectSummary : listObject) {
					System.out.println(ossObjectSummary.getKey());
				}
				return;
			} else if ("music".equals(MusicConstants.DO_TYPE)) {
				File file = new File(MusicConstants.MUSIC_TIME_TYPE_MAPPING_FILE_PATH);
				if (file.exists()) {
					String name = file.getName();
					if (name.endsWith(".xls") || name.endsWith(".xlsx")) {
						new GetMusicTypeFromExcel().doExcel(file);
					}
				} else {
					System.out.println("歌单分类文件不存在");
					return;
				}
			}
			// /
			eachFiles();
			doFile();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void eachFiles() {
		File file = new File(MusicConstants.BASE_FILE_PATH);
		FileDoUtil.outLog("本地根目录：" + MusicConstants.BASE_FILE_PATH);
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
				if (name.endsWith(".xls") || name.endsWith(".xlsx")) {
					POIUtil poiutil = DoExcelFactory.getBean();
					if (poiutil != null) {
						poiutil.doExcel(file);
					}
				} else {
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
		// ExecutorService pool = Executors.newFixedThreadPool(10);
		for (Map.Entry<String, List<File>> entry : fileMaps.entrySet()) {
			String fileListName = entry.getKey();
			List<File> fileList = entry.getValue();
			if (fileList == null) {
				continue;
			}
			FileDoUtil.outLog(fileListName);
			long start = System.currentTimeMillis();
			Runnable runnable = DoSaveFactory.getBean(fileListName, fileList);
			if (runnable != null) {
				runnable.run();
			}
			long end = System.currentTimeMillis();
			long times = (end - start) / 1000;
			FileDoUtil.outLog(fileListName + " \t cost:::" + times + "秒\t\n");
			// pool.execute(new BooKListThread(fileListName, fileList));
		}
		// pool.shutdown();
		// while (!pool.isTerminated()) {
		// try {
		// pool.awaitTermination(5, TimeUnit.SECONDS);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
	}

}
