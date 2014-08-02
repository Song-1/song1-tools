/**
 * 
 */
package com.tools.song1.aliyun;

import java.util.List;

import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.upload.aliyun.MusicConstants;

/**
 * @author Administrator
 *
 */
public class TestAliyun {

	///// test
	public static void main(String[] args) {
		try {
			// /// 加载配置文件
			MusicConstants.loadConfig();
			OSSUploadUtil.init();
			//JavascriptUtil.init();
			
//			String key = "test/状元听书/";
			String bucket = "testupload2";
			String key = "test状元听书1111/";
//			String newKey = "book/网络/";
			//OSSUploadUtil.putObject(bucket, key);
//			List<OSSObjectSummary> list = 
//			if(list != null){
//				for (OSSObjectSummary osb : list) {
//					System.out.println(osb.getKey());
//				}
//			}
			// OSSUploadUtil.modifyAliyunFloderName(bucket, key, MusicConstants.BUKET_NAME, newKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

}
