/**
 * 
 */
package com.tools.song1.service.impl;

import java.util.List;

import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.tools.song1.aliyun.OSSUploadUtil;
import com.tools.song1.service.AliyunService;

/**
 * @author Administrator
 *
 */
public class AliyunServiceImpl implements AliyunService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tools.song1.service.AliyunService#listAllBucketName()
	 */
	@Override
	public List<String> listAllBucketName() {
		return OSSUploadUtil.listAllBucketName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tools.song1.service.AliyunService#listAliyunFiles(java.lang.String,
	 * java.lang.String, java.util.List, java.util.List)
	 */
	@Override
	public void listAliyunFiles(String bucketName, String rootFloder, List<String> floderList, List<OSSObjectSummary> fileList) {
		OSSUploadUtil.listAliyunFloder(bucketName, rootFloder, floderList, fileList);
	}

}
