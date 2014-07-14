package com.upload.aliyun.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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

public class OSSUploadUtil {
	public static OSSClient client = null;
	private static final long PART_SIZE = 1 * 512 * 1024L; // 每个Part的大小 521kb
	private static final int CONCURRENCIES = 10; // 上传Part的并发线程数。

	/**
	 * init
	 */
	public static void init() {
		ClientConfiguration config = new ClientConfiguration();
		client = new OSSClient(MusicConstants.PROTOCOL + MusicConstants.ALIYUN_IMAGE_HOST,
				MusicConstants.ALIYUN_ACCESSKEYID, MusicConstants.ALIYUN_ACCESSKEYSECRET, config);
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
	 * @throws FileNotFoundException
	 */
	public static String uploadObject(String bucket, String key, File uploadFile) throws FileNotFoundException {
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
					ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, multipartUpload.getKey(),
							uploadId);
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
				pool.execute(new UploadPartThread(client, bucketName, key, new FileInputStream(uploadFile), uploadId,
						partNumber, PART_SIZE * i, curPartSize, eTags));
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
	private static String initMultipartUpload(OSSClient client, String bucketName, String key) throws OSSException,
			ClientException {
		InitiateMultipartUploadResult initResult = getMultipartUpload(client, bucketName, key);
		String uploadId = initResult.getUploadId();
		return uploadId;
	}

	// 初始化一个Multi-part upload请求。
	private static InitiateMultipartUploadResult getMultipartUpload(OSSClient client, String bucketName, String key)
			throws OSSException, ClientException {
		InitiateMultipartUploadRequest initUploadRequest = new InitiateMultipartUploadRequest(bucketName, key);
		InitiateMultipartUploadResult initResult = client.initiateMultipartUpload(initUploadRequest);
		return initResult;
	}

	// 完成一个multi-part请求。
	private static void completeMultipartUpload(OSSClient client, String bucketName, String key, String uploadId,
			List<PartETag> eTags) throws OSSException, ClientException {
		// 为part按partnumber排序
		Collections.sort(eTags, new EtagComparator());
		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName,
				key, uploadId, eTags);
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

		UploadPartThread(OSSClient client, String bucket, String object, FileInputStream uploadFile, String uploadId,
				int partId, long start, long partSize, List<PartETag> eTags) {
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

	public static boolean isObjectExist(String bucket, String key) {
		if (key == null || "".equals(key.trim())) {
			return false;
		}
		int index = key.lastIndexOf("/") + 1;
		String filePath = new String(key.substring(0, index -1 ));
		String name = new String(key.substring(index));
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucket);
		// 设置参数
		listObjectsRequest.setDelimiter("/");
		if (!filePath.endsWith("/")) {
			filePath = filePath + "/";
		}
		listObjectsRequest.setPrefix(filePath);
		listObjectsRequest.setMaxKeys(1000);
		ObjectListing objectListing = aliyunConnect(listObjectsRequest);
		// 遍历所有Object
		for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			String fileName = objectSummary.getKey();
			String fileName_ = fileName.replace(filePath, "");
			if (name.equals(fileName_)) {
				return true;
			}
		}
		return false;
	}

	private static ObjectListing aliyunConnect(ListObjectsRequest listObjectsRequest) {
		return aliyunConnect(listObjectsRequest, 1);
	}

	/**
	 * 
	 * @Title: aliyunConnect 当阿里云断线的时候重连,重连100次放弃
	 * @param listObjectsRequest
	 * @param a
	 * @return    
	 * ObjectListing    
	 * @throws
	 */
	private static ObjectListing aliyunConnect(ListObjectsRequest listObjectsRequest, int a) {
		ObjectListing objectListing = null;
		try {
			objectListing = client.listObjects(listObjectsRequest);
		} catch (Exception e) {
			FileDoUtil.outLog("阿里云第" + a + "次连接错误：" + e.getMessage());
			e.printStackTrace();
			if (a <= 10000) {
				a++;
				objectListing = aliyunConnect(listObjectsRequest, a);
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
	public static void copyObject(String sourceBucketName, String sourceKey, String destinationBucketName,
			String destinationKey) {
		if (client != null) {
			CopyObjectResult result = client.copyObject(sourceBucketName, sourceKey, destinationBucketName,
					destinationKey);
			FileDoUtil.outLog("[File Copy]Etag:::" + result.getETag());
		}
	}

}
