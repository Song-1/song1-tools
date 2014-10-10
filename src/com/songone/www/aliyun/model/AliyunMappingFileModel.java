/**
 * 
 */
package com.songone.www.aliyun.model;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class AliyunMappingFileModel {

	private int id;
	private String path;
	private String aliyunKey;
	private int uploadState = 0;
	private String remark;
	private int dataType;
	private String suffix;
	private int foreignKeyId;
	
	private boolean syncFlag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAliyunKey() {
		return aliyunKey;
	}

	public void setAliyunKey(String aliyunKey) {
		this.aliyunKey = aliyunKey;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dateType) {
		this.dataType = dateType;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public int getForeignKeyId() {
		return foreignKeyId;
	}

	public void setForeignKeyId(int foreignKeyId) {
		this.foreignKeyId = foreignKeyId;
	}
	
	public String getDataTypeName(){
		if(dataType == 1){
			return "享CD";
		}else if(dataType == 2){
			return "樱桃时光";
		}
		return "";
	}

	public int getUploadState() {
		return uploadState;
	}

	public void setUploadState(int uploadState) {
		this.uploadState = uploadState;
	}

	public String getUploadStateName() {
		///文件上传状态:0:未上传;1:上传中;2:上传成功;3:上传失败
		if(uploadState == 0){
			return "未上传";
		}else if(uploadState == 1){
			return "上传中";
		}else if(uploadState == 2){
			return "上传成功";
		}else if(uploadState == 3){
			return "上传失败";
		}
		return "";
	}
	public boolean isSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(boolean syncFlag) {
		this.syncFlag = syncFlag;
	}

	@Override
	public String toString() {
		return "AliyunMappingFileModel [path=" + path + ", aliyunKey=" + aliyunKey + ", dataType=" + dataType + ", foreignKeyId=" + foreignKeyId + ", state=" + getUploadStateName() + "]";
	}

}
