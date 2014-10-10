package com.songone.www.aliyun;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aliyun.openservices.ClientConfiguration;
import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.Bucket;
import com.aliyun.openservices.oss.model.CopyObjectResult;
import com.aliyun.openservices.oss.model.GetObjectRequest;
import com.aliyun.openservices.oss.model.ListObjectsRequest;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PutObjectResult;
import com.songone.www.aliyun.exceptions.AliyunObjectKeyIllegalException;
import com.songone.www.aliyun.functions.UploadFileFunction;
import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.FileDoUtil;
import com.songone.www.base.utils.StringUtil;

public class AliyunUtil {
	private static final Logger logger = LogManager.getLogger(AliyunUtil.class);
	public static OSSClient client1 = null;
	public static final long PART_SIZE = 1 * 512 * 1024L; // 每个Part的大小 521kb
	public static int THREAD_POOL_COUNT = 50;
	/**
	 * 获取阿里云连接对象
	 * 
	 * @return
	 */
	public static OSSClient createOSSClient() {
		ClientConfiguration config = new ClientConfiguration();
		String urlPath = BaseConstants.PROTOCOL + BaseConstants.ALIYUN_IMAGE_HOST;
		OSSClient client = new OSSClient(urlPath, BaseConstants.ALIYUN_ACCESSKEYID, BaseConstants.ALIYUN_ACCESSKEYSECRET, config);
		return client;
	}

	/**
	 * 校验bucket可key值是否符合要求,默认返回true,否则返回false
	 * 
	 * @param bucket
	 * @param key
	 * @param isFile 是否是文件
	 * @return
	 */
	public static boolean valiDateParams(String bucket, String key, boolean isFile) {
		boolean result = true;
		if (StringUtil.isEmptyString(bucket)) {
			logger.error("参数:阿里云bucket为空");
			result = false;
		} else {
			if (isFile) {
				if (StringUtil.isEmptyString(key)) {
					logger.error("参数:阿里云key为空");
					result = false;
				} else if (key.endsWith("/")) {
					logger.error("参数:阿里云key对应的不是文件");
					result = false;
				}
			} else if (key == null) {
				logger.error("参数:阿里云key为空");
				result = false;
			}
		}
		return result;
	}
	
	public static void putObject(String bucketName, String key, File file) {
		if (StringUtil.isEmptyString(bucketName)) {
			logger.debug("[putObject 失败] bucket为空");
			return;
		} else if (key == null) {
			logger.debug("[putObject 失败] key=null");
			return;
		}
		bucketName = bucketName.trim();
		FileInputStream in = null;
		try {
			if (file == null) {
				String path = AliyunUtil.class.getResource("/com/songone/www/aliyun/uploadfile.txt").getFile();
				file = new File(path);
			}
			if (file.exists()) {
				in = new FileInputStream(file);
				// 初始化OSSClient
				OSSClient client = createOSSClient();
				// 创建上传Object的Metadata
				ObjectMetadata meta = new ObjectMetadata();
				// 必须设置ContentLength
				meta.setContentLength(file.length());
				// 上传Object.
				PutObjectResult result = client.putObject(bucketName, key, in, meta);
				// 打印ETag
				System.out.println(result.getETag());
				in.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally{
			in = null;
		}
	}

	/**
	 * 获取所有的bucket
	 * 
	 * @return
	 */
	public static List<String> listAllBucket() {
		List<Bucket> buckets = createOSSClient().listBuckets();
		if (buckets != null) {
			List<String> names = new ArrayList<String>(buckets.size());
			for (Bucket bucket : buckets) {
				names.add(bucket.getName());
			}
			return names;
		}
		return null;
	}

	/**
	 * 获取阿里云的签名URL
	 * 
	 * @param bucket
	 * @param key
	 * @param expirationTimes 超时时间(ms)
	 * @return 阿里云签名URL
	 */
	public static String generatePresignedUrl(String bucket, String key, long expirationTimes) {
		String result = null;
		if (valiDateParams(bucket, key, true)) {
			long times = new Date().getTime();
			Date expiration = new Date(times + expirationTimes);
			URL url = createOSSClient().generatePresignedUrl(bucket, key, expiration);
			result = url.toString();
		}
		return result;
	}


	public static boolean uploadBigFile(String bucket, String key, File file) throws Exception {
		return new UploadFileFunction(bucket, key, file).uploadFile();
	}

	// 根据文件的大小和每个Part的大小计算需要划分的Part个数。
	public static int calPartCount(long size) {
		int partCount = (int) (size / PART_SIZE);
		if (size % PART_SIZE != 0) {
			partCount++;
		}
		return partCount;
	}

	/**
	 * 判断资源文件是否存在服务器
	 * 
	 * @param client
	 * @param bucket
	 * @param filePath
	 * @param name
	 * @return
	 */
	public static boolean isObjectExist(String bucket, String filePath, String name) {
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucket);
		// 设置参数
		listObjectsRequest.setDelimiter("/");
		listObjectsRequest.setPrefix(filePath);
		ObjectListing objectListing = createOSSClient().listObjects(listObjectsRequest);
		// 遍历所有Object
		for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			String fileName = objectSummary.getKey();
			fileName = fileName.replace(filePath, "");
			if (name.equals(fileName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断阿里云服务器指定bucket下面是否存在此key值的文件 .<br>
	 * 
	 * @param bucket
	 * @param key
	 * @return
	 */
	public static boolean isObjectExist(String bucket, String key) {
		if (key == null || "".equals(key.trim())) {
			return false;
		}
		int index = key.lastIndexOf("/") + 1;
		String filePath = new String(key.substring(0, index - 1));
		String name = new String(key.substring(index));
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucket);
		// 设置参数
		listObjectsRequest.setDelimiter("/");
		if (!filePath.endsWith("/")) {
			filePath = filePath + "/";
		}
		listObjectsRequest.setPrefix(filePath);
		listObjectsRequest.setMaxKeys(500);
		ObjectListing objectListing = null;
		// int rrrindex = 0;
		String NextMarker = "";
		while (true) {
			listObjectsRequest.setMarker(NextMarker);
			objectListing = aliyunConnect(listObjectsRequest);
			NextMarker = objectListing.getNextMarker();
			// 遍历所有Object
			for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
				String fileName = objectSummary.getKey();
				// FileDoUtil.outLog(fileName + "  index :::: " + rrrindex);
				String fileName_ = fileName.replace(filePath, "");
				if (name.equals(fileName_)) {
					return true;
				}
				// rrrindex++;
			}
			if (!objectListing.isTruncated()) {
				break;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: aliyunConnect 当阿里云断线的时候重连,重连100次放弃
	 * @param listObjectsRequest
	 * @param a
	 * @return ObjectListing
	 * @throws
	 */

	private static ObjectListing aliyunConnect(ListObjectsRequest listObjectsRequest) {
		int CONNECT_COUNT = 0;
		ObjectListing objectListing = null;
		try {
			logger.debug("阿里云第" + (++CONNECT_COUNT) + "次连接");
			objectListing = createOSSClient().listObjects(listObjectsRequest);
		} catch (Exception e) {
			logger.debug("阿里云第" + (++CONNECT_COUNT) + "次连接错误：" + e.getMessage());
			e.printStackTrace();
		}
		while (objectListing == null && CONNECT_COUNT < 100) {
			try {
				Thread.sleep(10000); // 线程沉睡10秒
				logger.debug("阿里云第" + (++CONNECT_COUNT) + "次连接");
				objectListing = createOSSClient().listObjects(listObjectsRequest);
				if (objectListing != null) {
					break;
				}
			} catch (Exception e) {
				logger.debug("阿里云第" + (++CONNECT_COUNT) + "次连接错误：" + e.getMessage());
			}
		}
		return objectListing;
	}

	/**
	 * 阿里云服务器资源复制
	 * 
	 * @param sourceBucketName
	 * @param sourceKey
	 * @param destinationBucketName
	 * @param destinationKey
	 */
	public static void copyObject(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey) {
		boolean falg = isObjectExist(destinationBucketName, destinationKey);
		if (!falg) {
			logger.debug(sourceBucketName);
			logger.debug(sourceKey);
			logger.debug(destinationBucketName);
			logger.debug(destinationKey);
			CopyObjectResult result = createOSSClient().copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
			logger.debug("[File Copy]Etag:::" + result.getETag());
		} else {
			logger.debug("[File Copy]目标文件已存在");
		}
	}

	/**
	 * 阿里云服务器资源复制
	 * 
	 * @param sourceBucketName
	 * @param sourceKey
	 * @param destinationBucketName
	 * @param destinationKey
	 */
	public static void deleteObject(String sourceBucketName, String sourceKey) {
		if (StringUtil.isEmptyString(sourceBucketName)) {
			return;
		} else if (sourceKey == null) {
			return;
		}
		System.out.println("delete...........");
		sourceBucketName = sourceBucketName.trim();
		createOSSClient().deleteObject(sourceBucketName, sourceKey);
	}

	public static void deleteKey(String sourceBucketName, String sourceKey) {
		if (StringUtil.isEmptyString(sourceBucketName)) {
			return;
		} else if (StringUtil.isEmptyString(sourceKey)) {
			return;
		}
		List<String> list = new ArrayList<String>();
		List<OSSObjectSummary> fileList = new ArrayList<OSSObjectSummary>();
		listAliyunFloder(sourceBucketName, sourceKey, list, fileList);
		for (OSSObjectSummary os : fileList) {
			String osName = os.getKey();
			deleteObject(sourceBucketName, osName);
		}
		fileList.clear();
		for (String string : list) {
			deleteKey(sourceBucketName, string);
		}
	}

	/**
	 * 阿里云服务器资源移动
	 * 
	 * @param sourceBucketName
	 * @param sourceKey
	 * @param destinationBucketName
	 * @param destinationKey
	 */
	public static CopyObjectResult moveObject(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey) {
		CopyObjectResult result = createOSSClient().copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
		createOSSClient().deleteObject(sourceBucketName, sourceKey);
		return result;
	}

	/**
	 * 阿里云服务器显示文件
	 * 
	 * @param sourceBucketName
	 * @param sourceKey
	 * @param destinationBucketName
	 * @param destinationKey
	 */
	public static List<OSSObjectSummary> listObject(String bucketName, String prefix) {

		List<OSSObjectSummary> ossslist = new ArrayList<OSSObjectSummary>();
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
		List<String> listPrefixeslist = listPrefixeslist(bucketName, prefix);
		ObjectListing objectListing = null;
		Iterator<String> iterator = listPrefixeslist.iterator();

		for (; iterator.hasNext();) {
			String commonPrefixe = iterator.next();
			if (!isLastPrefixes(bucketName, commonPrefixe)) {
				listPrefixeslist.addAll(listPrefixeslist(bucketName, commonPrefixe));
			}
			do {
				// 设置参数
				listObjectsRequest.setDelimiter("/");
				listObjectsRequest.setMaxKeys(1000);
				String nextMarker = "";
				listObjectsRequest.setMarker(nextMarker);
				listObjectsRequest.setPrefix(commonPrefixe);
				objectListing = aliyunConnect(listObjectsRequest);
				nextMarker = objectListing.getNextMarker();
				List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
				if (objectSummaries != null && objectSummaries.size() > 0) {
					ossslist.addAll(objectSummaries);
				}
			} while (objectListing.isTruncated());
		}
		// // 遍历所有Object
		// List<OSSObjectSummary> objectSummaries =
		// objectListing.getObjectSummaries();
		// ossslist.addAll(objectSummaries);
		return ossslist;
	}

	/**
	 * 阿里云服务器显示文件
	 * 
	 * @param sourceBucketName
	 * @param sourceKey
	 * @param destinationBucketName
	 * @param destinationKey
	 */
	public static boolean modifyTheFileStuffix(String bucketName, String prefix, boolean isCopy, boolean isDelete) {
		if (StringUtil.isEmptyString(bucketName)) {
			System.out.println("阿里云BUCKET不能为空!");
			return false;
		} else if (StringUtil.isEmptyString(prefix)) {
			System.out.println("阿里云文件路径不能为空!");
			return false;
		}
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
		List<String> listPrefixeslist = listAliyunFloder(bucketName, prefix, true);
		if (listPrefixeslist == null) {
			listPrefixeslist = new ArrayList<String>();
			listPrefixeslist.add(prefix);
		}
		ObjectListing objectListing = null;
		listObjectsRequest.setDelimiter("/");
		listObjectsRequest.setMaxKeys(1000);
		String nextMarker = "";
		Iterator<String> iterator = listPrefixeslist.iterator();
		for (; iterator.hasNext();) {
			String commonPrefixe = iterator.next();
			do {
				// 设置参数
				listObjectsRequest.setMarker(nextMarker);
				listObjectsRequest.setPrefix(commonPrefixe);
				objectListing = aliyunConnect(listObjectsRequest);
				nextMarker = objectListing.getNextMarker();
				List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
				if (objectSummaries != null) {
					for (OSSObjectSummary osb : objectSummaries) {
						String key = osb.getKey();
						if (StringUtil.isEmptyString(key)) {
							continue;
						} else if (key.endsWith("/")) {
							continue;
						}
						int index = key.lastIndexOf(".");
						if (index < 0) {
							if (isCopy) {
								String targtKey = key + ".mp3";
								System.out.println("原始key:::" + key);
								System.out.println("修改key:::" + targtKey);
								copyObject(bucketName, key, bucketName, targtKey);
							}
							if (isDelete) {
								deleteObject(bucketName, key);
							}
						}
					}
				}
			} while (objectListing.isTruncated());
		}
		return true;
	}

	static List<String> prefixes = new ArrayList<String>();

	/**
	 * 
	 * @Title: listPrefixeslist 显示目录
	 * @param bucketName
	 * @param prefix
	 * @return List<String>
	 * @throws
	 */
	public static List<String> listPrefixeslist(String bucketName, String prefix) {
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
		// 设置参数
		listObjectsRequest.setDelimiter("/");
		listObjectsRequest.setPrefix(prefix);
		listObjectsRequest.setMaxKeys(1000);
		ObjectListing objectListing = null;
		String nextMarker = "";
		List<String> commonPrefixeslist = new CopyOnWriteArrayList<String>();
		do {
			listObjectsRequest.setMarker(nextMarker);
			objectListing = aliyunConnect(listObjectsRequest);
			nextMarker = objectListing.getNextMarker();
			List<String> commonPrefixes = objectListing.getCommonPrefixes();
			if (commonPrefixes != null && commonPrefixes.size() > 0) {
				commonPrefixeslist.addAll(commonPrefixes);
			}
		} while (objectListing.isTruncated());
		for (String commonPrefixe : commonPrefixeslist) {
			// if (isLastPrefixes(bucketName, commonPrefixe)) {
			// prefixes.add(prefix);
			// }else{
			List<String> list = listPrefixeslist(bucketName, commonPrefixe);
			if (list != null && !list.isEmpty()) {
				commonPrefixeslist.addAll(list);
			}
			// }
		}
		return commonPrefixeslist;
	}

	public static List<String> listAliyunFloder(String bucketName, String rootFloder, boolean isViewAll) {
		if (StringUtil.isEmptyString(bucketName)) {
			return null;
		} else if (rootFloder == null) {
			return null;
		}
		bucketName = bucketName.trim();
		List<String> commonPrefixeslist = new CopyOnWriteArrayList<String>();
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
		// 设置参数
		listObjectsRequest.setDelimiter("/");
		listObjectsRequest.setPrefix(rootFloder);
		listObjectsRequest.setMaxKeys(500);
		ObjectListing objectListing = null;
		String nextMarker = "";
		do {
			listObjectsRequest.setMarker(nextMarker);
			objectListing = aliyunConnect(listObjectsRequest);
			nextMarker = objectListing.getNextMarker();
			List<String> commonPrefixes = objectListing.getCommonPrefixes();
			if (commonPrefixes != null && !commonPrefixes.isEmpty()) {
				commonPrefixeslist.addAll(commonPrefixes);
			}
		} while (objectListing.isTruncated());
		if (isViewAll) {
			for (String commonPrefixe : commonPrefixeslist) {
				List<String> list = listAliyunFloder(bucketName, commonPrefixe, isViewAll);
				if (list != null && !list.isEmpty()) {
					commonPrefixeslist.addAll(list);
				}
			}
		}
		return commonPrefixeslist;
	}

	public static void listAliyunFloder(String bucketName, String rootFloder, List<String> floderList, List<OSSObjectSummary> fileList) {
		if (StringUtil.isEmptyString(bucketName)) {
			return;
		} else if (rootFloder == null) {
			return;
		}
		bucketName = bucketName.trim();
		rootFloder = rootFloder.trim();
		floderList = floderList == null ? new ArrayList<String>() : floderList;
		fileList = fileList == null ? new ArrayList<OSSObjectSummary>() : fileList;
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
		// 设置参数
		listObjectsRequest.setDelimiter("/");
		listObjectsRequest.setPrefix(rootFloder);
		listObjectsRequest.setMaxKeys(500);
		ObjectListing objectListing = null;
		String nextMarker = "";
		do {
			listObjectsRequest.setMarker(nextMarker);
			objectListing = aliyunConnect(listObjectsRequest);
			nextMarker = objectListing.getNextMarker();
			List<String> commonPrefixes = objectListing.getCommonPrefixes();
			if (commonPrefixes != null && !commonPrefixes.isEmpty()) {
				floderList.addAll(commonPrefixes);
			}
			List<OSSObjectSummary> objectList = objectListing.getObjectSummaries();
			if (objectList != null && !objectList.isEmpty()) {
				fileList.addAll(objectList);
			}
		} while (objectListing.isTruncated());

	}

	public static boolean modifyAliyunFloderName(String bucketName, String prefix, String newBucketName, String newKey) {
		System.out.println(" 开始     复制文件............................");
		if (StringUtil.isEmptyString(bucketName)) {
			System.out.println("复制文件所属BUCKET为空!");
			return false;
		} else if (StringUtil.isEmptyString(prefix)) {
			System.out.println("复制文件路径为空!");
			return false;
		} else if (StringUtil.isEmptyString(newBucketName)) {
			System.out.println("复制到文件所属BUCKET为空!");
			return false;
		} else if (StringUtil.isEmptyString(newKey)) {
			System.out.println("复制到文件路径为空!");
			return false;
		}
		bucketName = bucketName.trim();
		prefix = prefix.trim();
		newBucketName = newBucketName.trim();
		newKey = newKey.trim();
		List<String> aliyunFloders = listAliyunFloder(bucketName, prefix, true);
		if (aliyunFloders == null || aliyunFloders.isEmpty()) {
			aliyunFloders = new ArrayList<String>();
			aliyunFloders.add(prefix);
		}

		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
		// 设置参数
		listObjectsRequest.setDelimiter("/");
		listObjectsRequest.setMaxKeys(1000);
		String nextMarker = "";
		ObjectListing objectListing = null;
		for (String floder : aliyunFloders) {
			if (StringUtil.isEmptyString(floder)) {
				continue;
			}
			System.out.println("=====================================================================================");
			System.out.println("the Floder Name::::" + floder);
			System.out.println("=====================================================================================");
			do {
				listObjectsRequest.setMarker(nextMarker);
				listObjectsRequest.setPrefix(floder);
				objectListing = aliyunConnect(listObjectsRequest);
				nextMarker = objectListing.getNextMarker();
				List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
				if (objectSummaries != null && objectSummaries.size() > 0) {
					// ossslist.addAll(objectSummaries);
					for (OSSObjectSummary ossObjectSummary : objectSummaries) {
						if (ossObjectSummary != null) {
							String key = ossObjectSummary.getKey();
							String newFloders = key.replace(prefix, newKey);
							logger.debug("older key::" + key + "\t new key::" + newFloders);
							if (!isObjectExist(newBucketName, newFloders)) {
								logger.debug(newFloders + " 文件不存在    进行复制.................");
								copyObject(bucketName, key, newBucketName, newFloders);
								logger.debug(newFloders + " 文件复制    完成.................");
							} else {
								logger.debug(newFloders + " 文件存在    不进行复制.................");
							}
						}
					}
				}
			} while (objectListing.isTruncated());
		}
		System.out.println(" 结束    复制文件............................");
		return true;
	}

	/**
	 * 
	 * @Title: isPrefixes 是否是最终目录
	 * @param bucketName
	 * @param prefix
	 * @return boolean
	 * @throws
	 */
	public static boolean isLastPrefixes(String bucketName, String prefix) {
		List<String> commonPrefixeslist = new ArrayList<String>();
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
		// 设置参数
		listObjectsRequest.setDelimiter("/");
		listObjectsRequest.setPrefix(prefix);
		listObjectsRequest.setMaxKeys(1000);
		ObjectListing objectListing = null;
		String nextMarker = "";
		do {
			listObjectsRequest.setMarker(nextMarker);
			objectListing = aliyunConnect(listObjectsRequest);
			nextMarker = objectListing.getNextMarker();
			List<String> commonPrefixes = objectListing.getCommonPrefixes();
			if (commonPrefixes != null && commonPrefixes.size() > 0) {
				commonPrefixeslist.addAll(commonPrefixes);
			}
		} while (objectListing.isTruncated());
		if (commonPrefixeslist == null || commonPrefixeslist.size() <= 0) {
			return true;
		}
		return false;
	}

	public static void modifyObjectSummaryName(String bucket, String key, String newKey) {

	}
	
	/**
	 * 判断阿里云的key是否不合法
	 * @param bucket
	 * @param key
	 * @return
	 */
	public static boolean isAliyunObjectKeyIllegal(String bucket, String key){
		boolean flag = false;
		try {
			isExistObjectForTheKey(bucket, key);
		} catch (AliyunObjectKeyIllegalException e) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 判断阿里云服务器指定bucket下面是否存在此key值的文件 .<br>
	 * 
	 * @param bucket
	 * @param key
	 * @return
	 */
	public static boolean isExistObjectForTheKey(String bucket, String key)throws AliyunObjectKeyIllegalException {
		boolean flag = true;
		try {
			createOSSClient().getObject(bucket, key);
		} catch (OSSException e) {
			String errorCode = e.getErrorCode();
			if ("NoSuchKey".equalsIgnoreCase(errorCode)) {
				flag = false;
				logger.debug("isExistObjectForTheKey::[bucket=" + bucket + ";key=" + key + "]" + e.getMessage());
				return flag;
			}
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			flag = false;
		}catch (Exception e) {
			String msg = e.getMessage();
			if(!StringUtil.isEmptyString(msg) && msg.startsWith("Object Key无效")){
				throw new AliyunObjectKeyIllegalException(msg,e);
			}else{
				e.printStackTrace();
				logger.error(e.getMessage(),e);
				flag = false;
			}
		}
		return flag;
	}

	public static void downLoadFile(String bucket, String key) throws AliyunObjectKeyIllegalException {
		boolean flag = isExistObjectForTheKey(bucket, key);
		System.out.println(flag);
		if (flag) {
			GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);
			File file = new File("D:/java/aliyun" + key);
			FileDoUtil.mkDirs(file);
			if (file.exists()) {
				// 下载Object到文件
				createOSSClient().getObject(getObjectRequest, file);
			}
		}
	}
}


