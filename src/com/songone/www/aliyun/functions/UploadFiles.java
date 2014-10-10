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
import com.songone.www.base.utils.StringUtil;

/**
 * @author Administrator
 *
 *
 */
public class UploadFiles {
	private static final Logger logger = LogManager.getLogger(UploadFiles.class);
	private static final long PART_SIZE = 1 * 512 * 1024L; // 每个Part的大小 521kb
	private static final int CONCURRENCIES = 1; // 上传Part的并发线程数。
	private int partCount = 0;
	private List<PartETag> eTags;
	private String bucket;
	private String key;
	private String errorMsg;

	private OSSClient getClient() {
		String urlPath = BaseConstants.PROTOCOL + BaseConstants.ALIYUN_IMAGE_HOST;
		OSSClient client = new OSSClient(urlPath, BaseConstants.ALIYUN_ACCESSKEYID, BaseConstants.ALIYUN_ACCESSKEYSECRET, null);
		return client;
	}

	public boolean uploadFile(String bucketName, String key, File uploadFile) {
		if (uploadFile == null || !uploadFile.exists()) {
			errorMsg = "文件不存在";
			return false;
		} else if (StringUtil.isEmptyString(bucketName)) {
			errorMsg = "bucket为空";
			return false;
		}
		boolean isExistFlag = false;
		try {
			isExistFlag = AliyunUtil.isExistObjectForTheKey(bucketName, key);
		} catch (AliyunObjectKeyIllegalException e) {
			errorMsg = e.getMessage();
			logger.error(errorMsg, e);
			return false;
		}
		if(isExistFlag){
			errorMsg = "key值已经存在";
			return true;
		}
		logger.debug("开始 上传文件 :::: " + key);
		boolean result = false;
		long startT = System.currentTimeMillis();
		bucketName = bucketName.trim();
		long size = uploadFile.length();
		partCount = calPartCount(size);
		try {
			if (partCount <= 1) {
				String etag = putObject(bucketName, key, uploadFile);
				if (!StringUtil.isEmptyString(etag)) {
					result = true;
				}
			} else {
				result = uploadBigFile(bucketName, key, uploadFile);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			logger.error(errorMsg, e);
		}
		long endT = System.currentTimeMillis();
		logger.debug("文件上传花费时间:::: " + (endT - startT) / 1000 + " s");
		logger.debug("结束 上传文件 :::: " + key);
		return result;
	}

	public String putObject(String bucketName, String key, File file) throws Exception {
		String etag = null;
		FileInputStream in = null;
		try {
			if (file == null) {
				String path = AliyunUtil.class.getResource("/com/songone/www/aliyun/uploadfile.txt").getFile();
				file = new File(path);
			}
			if (file.exists()) {
				in = new FileInputStream(file);
				OSSClient client = getClient();
				ObjectMetadata meta = new ObjectMetadata();
				meta.setContentLength(file.length());
				PutObjectResult result = client.putObject(bucketName, key, in, meta);
				etag = result.getETag();
				in.close();
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			in = null;
		}
		return etag;
	}

	// 通过Multipart的方式上传一个大文件
	// 要上传文件的大小必须大于一个Part允许的最小大小，即5MB。
	public boolean uploadBigFile(String bucketName, String key, File uploadFile) throws Exception {
		bucket = bucketName;
		this.key = key;
		long size = uploadFile.length();
		partCount = calPartCount(size);
		String uploadId = "";
		boolean isBreakPointUploadFlag = false;
		List<PartSummary> partSummaryList = new ArrayList<PartSummary>();
		ListMultipartUploadsRequest listMultipartUploadsRequest = new ListMultipartUploadsRequest(bucketName);
		// 获取Bucket内所有上传事件
		MultipartUploadListing listing = getClient().listMultipartUploads(listMultipartUploadsRequest);
		// 遍历所有上传事件
		for (MultipartUpload multipartUpload : listing.getMultipartUploads()) {
			// FileDoUtil.outLog("Key: " + multipartUpload.getKey() +
			// " UploadId: " + multipartUpload.getUploadId());
			if (key.equals(multipartUpload.getKey())) {
				isBreakPointUploadFlag = true;
				uploadId = multipartUpload.getUploadId();
				ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, multipartUpload.getKey(), uploadId);
				// 获取上传的所有Part信息
				PartListing partListing = getClient().listParts(listPartsRequest);
				partSummaryList = partListing.getParts();
				break;
			}
		}
		if (!isBreakPointUploadFlag) {
			uploadId = initMultipartUpload(bucketName, key);
		}
		// ExecutorService pool = Executors.newFixedThreadPool(CONCURRENCIES);
		eTags = Collections.synchronizedList(new ArrayList<PartETag>());
		new Thread(new SystemOutUploadFileThread()).start();
		partUpload: for (int i = 0; i < partCount; i++) {
			int partNumber = i + 1;
			long start = PART_SIZE * i;
			long curPartSize = PART_SIZE < size - start ? PART_SIZE : size - start;
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
			FileInputStream in = new FileInputStream(uploadFile);
			in.skip(PART_SIZE * i);
			UploadPartRequest uploadPartRequest = new UploadPartRequest();
			uploadPartRequest.setBucketName(bucket);
			uploadPartRequest.setKey(key);
			uploadPartRequest.setUploadId(uploadId);
			uploadPartRequest.setInputStream(in);
			uploadPartRequest.setPartSize(curPartSize);
			uploadPartRequest.setPartNumber(partNumber);
			long startTime = System.currentTimeMillis();
			UploadPartResult uploadPartResult = getClient().uploadPart(uploadPartRequest);
			long endtTime = System.currentTimeMillis();
			long times = (endtTime - startTime) / 1000L;
			times = times == 0 ? 1 : times;
			curPartSize = curPartSize / 1024;
			logger.debug("part :: " + partNumber + " size :: " + curPartSize + "  times ::: " + times + " speed:::" + (curPartSize / times) + "kb/s");
			eTags.add(uploadPartResult.getPartETag());
			// pool.execute(new UploadPartThread(new
			// FileInputStream(uploadFile), uploadId, partNumber, PART_SIZE * i,
			// curPartSize));
		}

		// pool.shutdown();
		// while (!pool.isTerminated()) {
		// pool.awaitTermination(5, TimeUnit.SECONDS);
		// }
		completeMultipartUpload(bucketName, key, uploadId, eTags);
		return true;
	}

	/**
	 * 实现分块并发上传
	 * 
	 * @author Administrator
	 *
	 */
	private class UploadPartThread implements Runnable {
		private FileInputStream uploadFile;
		private long start;
		private long size;
		private int partId;
		private String uploadId;

		UploadPartThread(FileInputStream uploadFile, String uploadId, int partId, long start, long partSize) {
			this.uploadFile = uploadFile;
			this.start = start;
			this.size = partSize;
			this.partId = partId;
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
				uploadPartRequest.setKey(key);
				uploadPartRequest.setUploadId(uploadId);
				uploadPartRequest.setInputStream(in);
				uploadPartRequest.setPartSize(size);
				uploadPartRequest.setPartNumber(partId);
				long startTime = System.currentTimeMillis();
				UploadPartResult uploadPartResult = getClient().uploadPart(uploadPartRequest);
				long endtTime = System.currentTimeMillis();
				long times = (endtTime - startTime) / 1000L;
				times = times == 0 ? 1 : times;
				size = size / 1024;
				logger.debug("part :: " + partId + " size :: " + size + "  times ::: " + times + " speed:::" + (size / times) + "kb/s");
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
	 */
	private class SystemOutUploadFileThread implements Runnable {
		@Override
		public void run() {
			int outStr = -1;
			while (eTags.size() != partCount) {
				int value = (eTags.size() * 100) / partCount;
				if (value == 0) {
					value = 1;
				}
				final int p = value;
				if (outStr == p) {
					continue;
				} else {
					outStr = p;
					System.out.print(outStr + "%  ");
				}
			}
			System.out.print("100%  ");
		}
	}

	// 根据文件的大小和每个Part的大小计算需要划分的Part个数。
	private int calPartCount(long size) {
		int partCount = (int) (size / PART_SIZE);
		if (size % PART_SIZE != 0) {
			partCount++;
		}
		return partCount;
	}

	// 初始化一个Multi-part upload请求。
	private String initMultipartUpload(String bucketName, String key) throws OSSException, ClientException {
		InitiateMultipartUploadRequest initUploadRequest = new InitiateMultipartUploadRequest(bucketName, key);
		InitiateMultipartUploadResult initResult = getClient().initiateMultipartUpload(initUploadRequest);
		String uploadId = initResult.getUploadId();
		return uploadId;
	}

	// 完成一个multi-part请求。
	private void completeMultipartUpload(String bucketName, String key, String uploadId, List<PartETag> eTags) throws OSSException, ClientException {
		// 为part按partnumber排序
		Collections.sort(eTags, new EtagComparator());
		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, key, uploadId, eTags);
		getClient().completeMultipartUpload(completeMultipartUploadRequest);
	}

	public String getErrorMsg() {
		return errorMsg;
	}
}
