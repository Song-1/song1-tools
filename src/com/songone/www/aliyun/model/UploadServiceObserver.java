/**
 * 
 */
package com.songone.www.aliyun.model;

import java.util.ArrayList;
import java.util.List;

import com.songone.www.aliyun.model.AliyunMappingFileModel;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class UploadServiceObserver {
	private List<AliyunMappingFileModel> datas = new ArrayList<AliyunMappingFileModel>();
	
	public void attach(AliyunMappingFileModel obj){
		datas.add(obj);
	}
	
	public void detach(AliyunMappingFileModel obj){
		datas.remove(obj);
	}
	
	public List<AliyunMappingFileModel> getDatas(){
		return datas;
	}
	
	public void setDatas(List<AliyunMappingFileModel> data){
		this.datas = data;
	}

}
