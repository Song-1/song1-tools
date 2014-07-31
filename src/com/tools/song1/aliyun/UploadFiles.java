/**
 * 
 */
package com.tools.song1.aliyun;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

import com.aliyun.openservices.ClientConfiguration;
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
import com.aliyun.openservices.oss.model.PartETag;
import com.aliyun.openservices.oss.model.PartListing;
import com.aliyun.openservices.oss.model.PartSummary;
import com.aliyun.openservices.oss.model.UploadPartRequest;
import com.aliyun.openservices.oss.model.UploadPartResult;
import com.tools.song1.util.FileDoUtil;
import com.tools.song1.util.StringUtil;
import com.upload.aliyun.EtagComparator;
import com.upload.aliyun.MusicConstants;

/**
 * @author Administrator
 *
 *
 */
public class UploadFiles {

	private ProgressBar progressBar;
	private static final long PART_SIZE = 1 * 512 * 1024L; // 每个Part的大小 521kb
	private static final int CONCURRENCIES = 10; // 上传Part的并发线程数。
	private int partCount = 0;
	private List<PartETag> eTags;
	private String bucket;
	private String key;

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}
	
	private OSSClient getClient(){
		ClientConfiguration config = new ClientConfiguration();
		OSSClient client = new OSSClient(MusicConstants.PROTOCOL + MusicConstants.ALIYUN_IMAGE_HOST, MusicConstants.ALIYUN_ACCESSKEYID, MusicConstants.ALIYUN_ACCESSKEYSECRET, config);
		return client;
	}

	// 通过Multipart的方式上传一个大文件
	// 要上传文件的大小必须大于一个Part允许的最小大小，即5MB。
	public void uploadBigFile(String bucketName, String key, File uploadFile) throws Exception {
		if (uploadFile == null || !uploadFile.exists()) {
			FileDoUtil.outLog("[上传文件失败] 文件为空");
			return;
		} else if (StringUtil.isEmptyString(bucketName)) {
			FileDoUtil.outLog("[上传文件失败] 文件:" + uploadFile.getAbsolutePath() + "  bucket为空");
			return;
		}
		bucketName = bucketName.trim();
		bucket = bucketName;
		this.key = key;
		if(OSSUploadUtil.isExistObjectForTheKey(bucketName, key)){
			FileDoUtil.outLog("[上传文件失败] 阿里云已经存在此文件");
			return ;
		}
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
			FileDoUtil.outLog("Key: " + multipartUpload.getKey() + " UploadId: " + multipartUpload.getUploadId());
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
		ExecutorService pool = Executors.newFixedThreadPool(CONCURRENCIES);
		eTags = Collections.synchronizedList(new ArrayList<PartETag>());
		new Thread(new SystemOutUploadFileThread()).start();
		long startT = System.currentTimeMillis();
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
			pool.execute(new UploadPartThread(new FileInputStream(uploadFile), uploadId, partNumber, PART_SIZE * i, curPartSize));
		}
		long endT = System.currentTimeMillis();
		System.out.println("启动线程时间:::: "+(endT - startT));
		pool.shutdown();
		while (!pool.isTerminated()) {
			pool.awaitTermination(5, TimeUnit.SECONDS);
		}

		FileDoUtil.outLog("over");
		completeMultipartUpload( bucketName, key, uploadId, eTags);
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
				FileDoUtil.outLog("part :: " + partId + " size :: " + size + "  times ::: " + times + " speed:::" + (size / times ) + "kb/s");
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
				if(value == 0){
					value = 1;
				}
				final int p = value;
				if (outStr == p) {
					continue;
				} else {
					outStr = p;
					System.out.print(outStr + "%  ");
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							progressBar.setSelection(p);
							progressBar.update();
						}
					});

				}
			}
			System.out.print("100%  ");
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					progressBar.setSelection(100);
					progressBar.update();
				}
			});

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
	private String initMultipartUpload( String bucketName, String key) throws OSSException, ClientException {
		InitiateMultipartUploadRequest initUploadRequest = new InitiateMultipartUploadRequest(bucketName, key);
		InitiateMultipartUploadResult initResult = getClient().initiateMultipartUpload(initUploadRequest);
		String uploadId = initResult.getUploadId();
		return uploadId;
	}

	// 完成一个multi-part请求。
	private void completeMultipartUpload( String bucketName, String key, String uploadId, List<PartETag> eTags) throws OSSException, ClientException {
		// 为part按partnumber排序
		Collections.sort(eTags, new EtagComparator());
		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, key, uploadId, eTags);
		getClient().completeMultipartUpload(completeMultipartUploadRequest);
	}

}
