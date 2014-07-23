package com.tools.song1.aliyun;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.aliyun.openservices.ClientConfiguration;
import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.openservices.oss.model.CopyObjectResult;
import com.aliyun.openservices.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.openservices.oss.model.InitiateMultipartUploadResult;
import com.aliyun.openservices.oss.model.ListMultipartUploadsRequest;
import com.aliyun.openservices.oss.model.ListObjectsRequest;
import com.aliyun.openservices.oss.model.ListPartsRequest;
import com.aliyun.openservices.oss.model.MultipartUpload;
import com.aliyun.openservices.oss.model.MultipartUploadListing;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PartETag;
import com.aliyun.openservices.oss.model.PartListing;
import com.aliyun.openservices.oss.model.PartSummary;
import com.aliyun.openservices.oss.model.PutObjectResult;
import com.aliyun.openservices.oss.model.UploadPartRequest;
import com.aliyun.openservices.oss.model.UploadPartResult;
import com.upload.aliyun.EtagComparator;
import com.upload.aliyun.MusicConstants;
import com.upload.aliyun.util.FileDoUtil;
import com.upload.aliyun.util.StringUtil;

public class OSSUploadUtil {
	public static OSSClient client = null;
	private static final long PART_SIZE = 1 * 512 * 1024L; // 每个Part的大小 521kb
	private static final int CONCURRENCIES = 10; // 上传Part的并发线程数。

	/**
	 * init
	 */
	public static void init() {
		ClientConfiguration config = new ClientConfiguration();
		client = new OSSClient(MusicConstants.PROTOCOL + MusicConstants.ALIYUN_IMAGE_HOST, MusicConstants.ALIYUN_ACCESSKEYID, MusicConstants.ALIYUN_ACCESSKEYSECRET, config);
	}

	public static String generateAliyunURL(String bucket, String key, long expirationTimes) {
		long times = new Date().getTime();
		FileDoUtil.outLog(times + "");
		Date expiration = new Date(times + expirationTimes);
		FileDoUtil.outLog(expiration.getTime() + "");
		URL url = client.generatePresignedUrl(bucket, key, expiration);
		FileDoUtil.outLog(url.toString());
		return url.toString();
	}

	/**
	 * 单个小文件上传
	 * 
	 * @param client
	 * @param bucket
	 * @param key
	 * @param uploadFile
	 * @return
	 * @throws IOException
	 */
	public static String uploadObject(String bucket, String key, File uploadFile) throws IOException {
		String suffix = key.substring(key.lastIndexOf(".") + 1);
		FileInputStream fin = new FileInputStream(uploadFile);
		// 创建上传Object的Metadata
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(uploadFile.length());
		String contentType = (String) MusicConstants.contentTypeMap.get(suffix);
		if (contentType == null) {
			contentType = (String) MusicConstants.contentTypeMap.get("stream");
		}
		meta.setContentType(contentType);
		PutObjectResult result = client.putObject(bucket, key, fin, meta);
		fin.close();
		return result.getETag();
	}

	/**
	 * 单个小文件上传
	 * 
	 * @param client
	 * @param bucket
	 * @param key
	 * @param uploadFile
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String uploadImage(String bucket, String key, byte[] uploadFile) throws FileNotFoundException {
		InputStream sbs = new ByteArrayInputStream(uploadFile);
		// 创建上传Object的Metadata
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(uploadFile.length);
		String suffix = key.substring(key.lastIndexOf(".") + 1);
		String contentType = (String) MusicConstants.contentTypeMap.get(suffix);
		if (contentType == null) {
			contentType = (String) MusicConstants.contentTypeMap.get("stream");
		}
		meta.setContentType(contentType);
		PutObjectResult result = client.putObject(bucket, key, sbs, meta);
		return result.getETag();
	}

	// 通过Multipart的方式上传一个大文件
	// 要上传文件的大小必须大于一个Part允许的最小大小，即5MB。
	public static void uploadBigFile(String bucketName, String key, File uploadFile) throws Exception {
		long size = uploadFile.length();
		int partCount = calPartCount(size);
		if (partCount <= 1) {
			FileDoUtil.outLog("[INFO]上传文件的大小小于一个Part的字节数：" + PART_SIZE + ",使用单文件上传");
			uploadObject(bucketName, key, uploadFile);
		} else {
			String uploadId = "";
			boolean isBreakPointUploadFlag = false;
			List<PartSummary> partSummaryList = new ArrayList<PartSummary>();
			ListMultipartUploadsRequest listMultipartUploadsRequest = new ListMultipartUploadsRequest(bucketName);
			// 获取Bucket内所有上传事件
			MultipartUploadListing listing = client.listMultipartUploads(listMultipartUploadsRequest);
			// 遍历所有上传事件
			for (MultipartUpload multipartUpload : listing.getMultipartUploads()) {
				FileDoUtil.outLog("Key: " + multipartUpload.getKey() + " UploadId: " + multipartUpload.getUploadId());
				if (key.equals(multipartUpload.getKey())) {
					isBreakPointUploadFlag = true;
					uploadId = multipartUpload.getUploadId();
					ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, multipartUpload.getKey(), uploadId);
					// 获取上传的所有Part信息
					PartListing partListing = client.listParts(listPartsRequest);
					partSummaryList = partListing.getParts();
					break;
				}
			}
			if (!isBreakPointUploadFlag) {
				uploadId = initMultipartUpload(client, bucketName, key);
			}
			ExecutorService pool = Executors.newFixedThreadPool(CONCURRENCIES);
			List<PartETag> eTags = Collections.synchronizedList(new ArrayList<PartETag>());
			new Thread(new SystemOutUploadFileThread(partCount, eTags)).start();
			partUpload: for (int i = 0; i < partCount; i++) {
				int partNumber = i + 1;
				long start = PART_SIZE * i;
				long curPartSize = PART_SIZE < size - start ? PART_SIZE : size - start;
				if (isBreakPointUploadFlag) {
					for (PartSummary part : partSummaryList) {
						System.out.print("PartNumber: " + part.getPartNumber() + " ETag: " + part.getETag());
						FileDoUtil.outLog("    size : " + part.getSize());
						if (partNumber == part.getPartNumber()) {
							if (curPartSize != part.getSize()) {
								start += part.getSize();
							} else {
								eTags.add(new PartETag(partNumber, part.getETag()));
								continue partUpload;
							}
						}
					}
				}
				pool.execute(new UploadPartThread(client, bucketName, key, new FileInputStream(uploadFile), uploadId, partNumber, PART_SIZE * i, curPartSize, eTags));
			}

			pool.shutdown();
			while (!pool.isTerminated()) {
				pool.awaitTermination(5, TimeUnit.SECONDS);
			}
			if (eTags.size() != partCount) {
				throw new IllegalStateException("Multipart上传失败，有Part未上传成功。");
			}
			FileDoUtil.outLog("over");
			completeMultipartUpload(client, bucketName, key, uploadId, eTags);
		}
	}

	// 根据文件的大小和每个Part的大小计算需要划分的Part个数。
	private static int calPartCount(long size) {
		int partCount = (int) (size / PART_SIZE);
		if (size % PART_SIZE != 0) {
			partCount++;
		}
		return partCount;
	}

	// 初始化一个Multi-part upload请求。
	private static String initMultipartUpload(OSSClient client, String bucketName, String key) throws OSSException, ClientException {
		InitiateMultipartUploadResult initResult = getMultipartUpload(client, bucketName, key);
		String uploadId = initResult.getUploadId();
		return uploadId;
	}

	// 初始化一个Multi-part upload请求。
	private static InitiateMultipartUploadResult getMultipartUpload(OSSClient client, String bucketName, String key) throws OSSException, ClientException {
		InitiateMultipartUploadRequest initUploadRequest = new InitiateMultipartUploadRequest(bucketName, key);
		InitiateMultipartUploadResult initResult = client.initiateMultipartUpload(initUploadRequest);
		return initResult;
	}

	// 完成一个multi-part请求。
	private static void completeMultipartUpload(OSSClient client, String bucketName, String key, String uploadId, List<PartETag> eTags) throws OSSException, ClientException {
		// 为part按partnumber排序
		Collections.sort(eTags, new EtagComparator());
		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, key, uploadId, eTags);
		client.completeMultipartUpload(completeMultipartUploadRequest);
	}

	/**
	 * 实现分块并发上传
	 * 
	 * @author Administrator
	 *
	 */
	private static class UploadPartThread implements Runnable {
		private FileInputStream uploadFile;
		private String bucket;
		private String object;
		private long start;
		private long size;
		private List<PartETag> eTags;
		private int partId;
		private OSSClient client;
		private String uploadId;

		UploadPartThread(OSSClient client, String bucket, String object, FileInputStream uploadFile, String uploadId, int partId, long start, long partSize, List<PartETag> eTags) {
			this.uploadFile = uploadFile;
			this.bucket = bucket;
			this.object = object;
			this.start = start;
			this.size = partSize;
			this.eTags = eTags;
			this.partId = partId;
			this.client = client;
			this.uploadId = uploadId;
		}

		@Override
		public void run() {

			InputStream in = null;
			try {
				in = uploadFile;
				in.skip(start);

				UploadPartRequest uploadPartRequest = new UploadPartRequest();
				uploadPartRequest.setBucketName(bucket);
				uploadPartRequest.setKey(object);
				uploadPartRequest.setUploadId(uploadId);
				uploadPartRequest.setInputStream(in);
				uploadPartRequest.setPartSize(size);
				uploadPartRequest.setPartNumber(partId);
				long startTime = System.currentTimeMillis();
				UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
				long endtTime = System.currentTimeMillis();
				long times = (endtTime - startTime) / 1000L;
				FileDoUtil.outLog("part :: " + partId + " size :: " + size + "  times ::: " + times);
				eTags.add(uploadPartResult.getPartETag());

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (in != null)
					try {
						in.close();
					} catch (Exception e) {
					}
			}
		}
	}

	/**
	 * 打印进度信息
	 * 
	 * @author Administrator
	 *
	 */
	private static class SystemOutUploadFileThread implements Runnable {

		private List<PartETag> eTags;
		private int partCount;

		SystemOutUploadFileThread(int partCount, List<PartETag> eTags) {
			this.eTags = eTags;
			this.partCount = partCount;
		}

		@Override
		public void run() {
			int outStr = -1;
			while (eTags.size() != partCount) {
				int p = (eTags.size() * 100) / partCount;
				if (outStr == p) {
					continue;
				} else {
					outStr = p;
					System.out.print(outStr + "%  ");
				}

			}
		}
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
		ObjectListing objectListing = client.listObjects(listObjectsRequest);
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
			FileDoUtil.outLog("阿里云第" + (++CONNECT_COUNT) + "次连接");
			objectListing = client.listObjects(listObjectsRequest);
		} catch (Exception e) {
			FileDoUtil.outLog("阿里云第" + (++CONNECT_COUNT) + "次连接错误：" + e.getMessage());
			e.printStackTrace();
		}
		while (objectListing == null && CONNECT_COUNT < 100) {
			try {
				Thread.sleep(10000); // 线程沉睡10秒
				FileDoUtil.outLog("阿里云第" + (++CONNECT_COUNT) + "次连接");
				objectListing = client.listObjects(listObjectsRequest);
				if (objectListing != null) {
					break;
				}
			} catch (Exception e) {
				FileDoUtil.outLog("阿里云第" + (++CONNECT_COUNT) + "次连接错误：" + e.getMessage());
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
		if (client != null) {
			boolean falg = isObjectExist(destinationBucketName, destinationKey);
			if(!falg){
				CopyObjectResult result = client.copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
				FileDoUtil.outLog("[File Copy]Etag:::" + result.getETag());
			}
			FileDoUtil.outLog("[File Copy]目标文件已存在" );
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
		try {
			if (client != null) {
				System.out.println("delete..........."+sourceKey);
				client.deleteObject(sourceBucketName, sourceKey);
			}else{
				OSSUploadUtil.init();
				deleteObject(sourceBucketName, sourceKey);
			}
			
		} catch (Exception e) {
			try {
				Thread.sleep(5000);
			} catch (Exception e2) {
				// TODO: handle exception
			}
			deleteObject(sourceBucketName, sourceKey);
		}
	}
	
	public static void deleteKey(String sourceBucketName, String sourceKey) {
		if (StringUtil.isEmptyString(sourceBucketName)) {
			return ;
		} else if (StringUtil.isEmptyString(sourceKey)) {
			return ;
		}
		List<String> list = new ArrayList<String>();
		List<OSSObjectSummary> fileList = new ArrayList<OSSObjectSummary>();
		OSSUploadUtil.listAliyunFloder(sourceBucketName, sourceKey, list, fileList);
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
		if (client != null) {
			CopyObjectResult result = client.copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
			client.deleteObject(sourceBucketName, sourceKey);
			return result;
		}
		return null;
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
	public static boolean modifyTheFileStuffix(String bucketName, String prefix,boolean isCopy,boolean isDelete) {
		if(StringUtil.isEmptyString(bucketName)){
			System.out.println("阿里云BUCKET不能为空!");
			return false;
		}else if(StringUtil.isEmptyString(prefix)){
			System.out.println("阿里云文件路径不能为空!");
			return false;
		}
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
		List<String> listPrefixeslist = listAliyunFloder(bucketName, prefix, true);
		if(listPrefixeslist == null){
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
				if(objectSummaries != null){
					for (OSSObjectSummary osb : objectSummaries) {
						String key = osb.getKey();
						if(StringUtil.isEmptyString(key)){
							continue;
						}else if (key.endsWith("/")){
							continue;
						}
						int index = key.lastIndexOf(".");
						if(index < 0){
							if(isCopy){
								String targtKey = key + ".mp3";
								System.out.println("原始key:::"+key);
								System.out.println("修改key:::"+targtKey);
								copyObject(bucketName, key, bucketName, targtKey);
							}
							if(isDelete){
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
		} else if (StringUtil.isEmptyString(rootFloder)) {
			return null;
		}
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
	
	public static void listAliyunFloder(String bucketName, String rootFloder, List<String> floderList,List<OSSObjectSummary> fileList) {
		if (StringUtil.isEmptyString(bucketName)) {
			return;
		} else if (StringUtil.isEmptyString(rootFloder)) {
			return;
		}
		floderList = floderList == null?new ArrayList<String>() :floderList;
		fileList = fileList == null?new ArrayList<OSSObjectSummary>() :fileList;
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


	public static void modifyAliyunFloderName(String bucketName, String prefix, String newBucketName, String newKey) {
		System.out.println(" 开始     复制文件............................");
		if (StringUtil.isEmptyString(bucketName)) {
			System.out.println("复制文件所属BUCKET为空!");
			return;
		} else if (StringUtil.isEmptyString(prefix)) {
			System.out.println("复制文件路径为空!");
			return;
		} else if (StringUtil.isEmptyString(newBucketName)) {
			System.out.println("复制到文件所属BUCKET为空!");
			return;
		} else if (StringUtil.isEmptyString(newKey)) {
			System.out.println("复制到文件路径为空!");
			return;
		}
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
							FileDoUtil.outLog("older key::" + key + "\t new key::" + newFloders);
							if (!isObjectExist(newBucketName, newFloders)) {
								FileDoUtil.outLog(newFloders + " 文件不存在    进行复制.................");
								copyObject(bucketName, key, newBucketName, newFloders);
								FileDoUtil.outLog(newFloders + " 文件复制    完成.................");
							}
						}
					}
				}
			} while (objectListing.isTruncated());
		}
		System.out.println(" 结束    复制文件............................");
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
}
