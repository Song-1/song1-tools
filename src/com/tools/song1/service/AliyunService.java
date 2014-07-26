package com.tools.song1.service;

import java.util.List;

import com.aliyun.openservices.oss.model.OSSObjectSummary;

public interface AliyunService {
	
	public List<String> listAllBucketName();
	public void listAliyunFiles(String bucketName, String rootFloder, List<String> floderList,List<OSSObjectSummary> fileList);

}
