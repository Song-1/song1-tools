/**
 * 
 */
package com.songone.www.enjoycd;

import com.songone.www.base.utils.SystemUtil;
import com.songone.www.enjoycd.utils.EnjoyEachLocalFileUtil;
import com.songone.www.enjoycd.utils.SaveEnjoyCDDataUtil;
import com.songone.www.enjoycd.utils.SyncEnjoyDataUtil;
import com.songone.www.enjoycd.utils.UploadFileUtil;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class EnjoyDataMain {
	public static void main(String[] args) {
		SystemUtil.init();
		EnjoyEachLocalFileUtil.start(); // 开始扫描本地文件
		//UploadFileUtil.start(); // 开始上传本地文件到阿里云服务器
		SaveEnjoyCDDataUtil.start(); // 解析享CD数据并保存等待同步到音乐一号服务器
		SyncEnjoyDataUtil.start(); //// 同步享CD的专辑数据到音乐一号服务器
	}

}
