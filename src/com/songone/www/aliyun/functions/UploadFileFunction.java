/**
 * 
 */
package com.songone.www.aliyun.functions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.openservices.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.openservices.oss.model.InitiateMultipartUploadResult;
import com.aliyun.openservices.oss.model.ListMultipartUploadsRequest;
import com.aliyun.openservices.oss.model.ListPartsRequest;
import com.aliyun.openservices.oss.model.MultipartUpload;
import com.aliyun.openservices.oss.model.MultipartUploadListing;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PartETag;
import com.aliyun.openservices.oss.model.PartListing;
import com.aliyun.openservices.oss.model.PartSummary;
import com.aliyun.openservices.oss.model.PutObjectResult;
import com.aliyun.openservices.oss.model.UploadPartRequest;
import com.aliyun.openservices.oss.model.UploadPartResult;
import com.songone.www.aliyun.AliyunUtil;
import com.songone.www.aliyun.EtagComparator;
import com.songone.www.aliyun.exceptions.AliyunObjectKeyIllegalException;
import com.songone.www.base.utils.BaseConstants;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class UploadFileFunction {

	private static final Logger logger = LogManager.getLogger(UploadFileFunction.class);

	private String bucket;
	private String key;
	private File file;
	private long fileSize;
	private long partSize;

	public UploadFileFunction(String bucket, String key, File file) {
		this.bucket = bucket;
		this.key = key;
		this.file = file;
		init();
	}

	private void init() {
		if (file != null) {
			fileSize = file.length();
		}
		partSize = AliyunUtil.PART_SIZE;
	}

	public boolean uploadFile() throws Exception {
//		if (!checkParams()) {
//			return false;
//		}
		if (fileSize <= partSize) {
			return putObject(bucket, key, file);
		}
		return uploadBigFile();
	}

	private boolean checkParams() {
		if (!AliyunUtil.valiDateParams(bucket, key, true)) {
			return false;
		}
		boolean result = false;
		try {
			result = AliyunUtil.isExistObjectForTheKey(bucket, key);
		} catch (AliyunObjectKeyIllegalException e) {
			return false;
		}
		if (result) {
			logger.debug("阿里云存在此文件:");
			logger.debug("bucket::" + bucket);
			logger.debug("key::" + key);
			return false;
		}
		return true;
	}

	private boolean uploadBigFile() throws Exception {
		int partCount = AliyunUtil.calPartCount(fileSize);
		String uploadId = "";
		boolean isBreakPointUploadFlag = false;
		List<PartSummary> partSummaryList = new ArrayList<PartSummary>();
//		ListMultipartUploadsRequest listMultipartUploadsRequest = new ListMultipartUploadsRequest(bucket);
//		// 获取Bucket内所有上传事件
//		MultipartUploadListing listing = AliyunUtil.createOSSClient().listMultipartUploads(listMultipartUploadsRequest);
//		// 遍历所有上传事件
//		for (MultipartUpload multipartUpload : listing.getMultipartUploads()) {
//			// logger.debug("Key: " + multipartUpload.getKey() + " UploadId: " +
//			// multipartUpload.getUploadId());
//			if (key.equals(multipartUpload.getKey())) {
//				isBreakPointUploadFlag = true;
//				uploadId = multipartUpload.getUploadId();
//				ListPartsRequest listPartsRequest = new ListPartsRequest(bucket, multipartUpload.getKey(), uploadId);
//				// 获取上传的所有Part信息
//				PartListing partListing = AliyunUtil.createOSSClient().listParts(listPartsRequest);
//				partSummaryList = partListing.getParts();
//				break;
//			}
//		}
		if (!isBreakPointUploadFlag) {
			uploadId = initMultipartUpload();
		}
		List<PartETag> eTags = Collections.synchronizedList(new ArrayList<PartETag>());
		new Thread(new SystemOutUploadFileThread(partCount, eTags)).start();
		uploadImpl(uploadId, partCount, isBreakPointUploadFlag, partSummaryList, eTags);
		if (eTags.size() != partCount) {
			throw new IllegalStateException("Multipart上传失败，有Part未上传成功。");
		}
		completeMultipartUpload(uploadId, eTags);
		logger.debug("key:::"+ key + "....... 文件上传成功..........");
		return true;
	}
	
	public void uploadImpl(String uploadId,int partCount,boolean isBreakPointUploadFlag,List<PartSummary> partSummaryList,List<PartETag> eTags){
		OSSClient client = AliyunUtil.createOSSClient();
		partUpload: for (int i = 0; i < partCount; i++) {
			int partNumber = i + 1;
			long start = partSize * i;
			long curPartSize = partSize < (fileSize - start) ? partSize : (fileSize - start);
			if (isBreakPointUploadFlag) {
				for (PartSummary part : partSummaryList) {
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
			upload(client, start, curPartSize, uploadId, eTags, partNumber);
		}
	}
	
	private void uploadImplByThreadPoolExecutor(String uploadId,int partCount,boolean isBreakPointUploadFlag,List<PartSummary> partSummaryList,List<PartETag> eTags){
		ThreadPoolExecutor pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(AliyunUtil.THREAD_POOL_COUNT);
		partUpload: for (int i = 0; i < partCount; i++) {
			int partNumber = i + 1;
			long start = partSize * i;
			long curPartSize = partSize < (fileSize - start) ? partSize : (fileSize - start);
			if (isBreakPointUploadFlag) {
				for (PartSummary part : partSummaryList) {
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
			UploadPartThread uploadPartThread = new UploadPartThread(uploadId, start, curPartSize, partNumber, eTags);
			pool.execute(uploadPartThread);
			//upload(client, start, curPartSize, uploadId, eTags, partNumber);
		}
		pool.shutdown();
		while (!pool.isTerminated()) {
			try {
				pool.awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error(e.getMessage(),e);
			}
		}
	}

	// 初始化一个Multi-part upload请求。
	private String initMultipartUpload() throws OSSException, ClientException {
		InitiateMultipartUploadResult initResult = getMultipartUpload();
		String uploadId = initResult.getUploadId();
		return uploadId;
	}

	// 初始化一个Multi-part upload请求。
	private InitiateMultipartUploadResult getMultipartUpload() throws OSSException, ClientException {
		InitiateMultipartUploadRequest initUploadRequest = new InitiateMultipartUploadRequest(bucket, key);
		InitiateMultipartUploadResult initResult = AliyunUtil.createOSSClient().initiateMultipartUpload(initUploadRequest);
		return initResult;
	}

	// 完成一个multi-part请求。
	private void completeMultipartUpload(String uploadId, List<PartETag> eTags) throws OSSException, ClientException {
		// 为part按partnumber排序
		Collections.sort(eTags, new EtagComparator());
		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucket, key, uploadId, eTags);
		AliyunUtil.createOSSClient().completeMultipartUpload(completeMultipartUploadRequest);
	}

	/**
	 * 文件上传(适合小文件上传).<br>
	 * 
	 * <pre>
	 * 		备注:
	 * 		1.当key是对应文件目录时,默认新建文件目录
	 * 		2.当key是对应文件目录时,且file== null,,默认会新建一个文件
	 * </pre>
	 * 
	 * @param bucket
	 * @param key
	 * @param uploadFile
	 * @return
	 */
	private boolean putObject(String bucket, String key, File uploadFile) {
		boolean result = false;
		if (AliyunUtil.valiDateParams(bucket, key, false)) {
			if (key.endsWith("/") && uploadFile == null) {
				String path = AliyunUtil.class.getResource("/com/songone/www/aliyun/uploadfile.txt").getFile();
				uploadFile = new File(path);
			} else if (uploadFile == null) {
				logger.error("参数:上传的文件为空");
				return result;
			} else if (!uploadFile.exists()) {
				logger.error("参数:上传的文件不存在");
				return result;
			} else {
				FileInputStream in = null;
				try {
					in = new FileInputStream(uploadFile);
					result = putObject(bucket, key, in, uploadFile.length());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					in = null;
				}
			}
		}
		return result;
	}

	/**
	 * 文件上传(适合小文件上传).<br>
	 * 
	 * @param bucket
	 * @param key
	 * @param in
	 * @param fileSize
	 * @return
	 */
	private boolean putObject(String bucket, String key, InputStream in, long fileSize) {
		boolean result = false;
		if (AliyunUtil.valiDateParams(bucket, key, false)) {
			if (in != null && fileSize > 0) {
				try {
					ObjectMetadata meta = new ObjectMetadata();
					meta.setContentLength(fileSize);
					if (!key.endsWith("/")) {
						String suffix = "";
						if (key.lastIndexOf(".") > 0) {
							suffix = new String(key.substring(key.lastIndexOf(".") + 1));
						}
						String contentType = (String) BaseConstants.contentTypeMap.get(suffix);
						if (contentType == null) {
							contentType = (String) BaseConstants.contentTypeMap.get("stream");
						}
					}
					PutObjectResult putObjectResult = AliyunUtil.createOSSClient().putObject(bucket, key, in, meta);
					logger.debug("上传文件Etag:" + putObjectResult.getETag() + " key :: " + key);
					in.close();
					result = true;
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					in = null;
				}
			}
		}
		return result;
	}
	
	private boolean upload(OSSClient client,long partStartSize,long curPartSize, String uploadId,List<PartETag> eTags,int partNumber){
		boolean result = false;
		try {
			FileInputStream in = new FileInputStream(file);
			in.skip(partStartSize);
			UploadPartRequest uploadPartRequest = new UploadPartRequest();
			uploadPartRequest.setBucketName(bucket);
			uploadPartRequest.setKey(key);
			uploadPartRequest.setUploadId(uploadId);
			uploadPartRequest.setInputStream(in);
			uploadPartRequest.setPartSize(curPartSize);
			uploadPartRequest.setPartNumber(partNumber);
			long startTime = System.currentTimeMillis();
			UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
			long endtTime = System.currentTimeMillis();
			long times = (endtTime - startTime) / 1000L;
			times = times == 0 ? 1 : times;
			curPartSize = curPartSize / 1024;
			logger.debug("part :: " + partNumber + " size :: " + curPartSize + "  times ::: " + times + " speed:::" + (curPartSize / times) + "kb/s");
			eTags.add(uploadPartResult.getPartETag());
			result = true;
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
		return result;
	}
	
	private class UploadPartThread implements Runnable {
		private long partStartSize;
		private String uploadId;
		private long curPartSize;
		private int partNumber;
		private List<PartETag> eTags;
		private OSSClient client = AliyunUtil.createOSSClient();
		
		public UploadPartThread(String uploadId,long partStartSize,long curPartSize,int partNumber,List<PartETag> eTags){
			this.partStartSize = partStartSize;
			this.uploadId = uploadId;
			this.curPartSize = curPartSize;
			this.partNumber = partNumber;
			this.eTags = eTags;
		}

		public void run() {
			boolean result = upload(client, partStartSize, curPartSize, uploadId, eTags, partNumber);
			int counts = 0;
			while(!result){
				if(counts > 5){
					break;
				}
				counts += 1;
				result = upload(client, partStartSize, curPartSize, uploadId, eTags, partNumber);
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
					logger.debug(outStr + "%  ");
				}

			}
		}
	}

}
