/**
 * 
 */
package com.songone.www.enjoycd.models;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class LocalFileDataInfo {

	private int id;
	private String filePath;
	private int parentId;
	private String createDate;
	private String createUser;
	private String fileType;
	private String infoData;
	private boolean isUpload;
	private boolean syncDataFlag;
	private boolean isBasePath;
	private String aliyunKey;
	private boolean syncFail;

	private boolean uploadFail;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getInfoData() {
		return infoData;
	}

	public void setInfoData(String infoData) {
		this.infoData = infoData;
	}

	public boolean isUpload() {
		return isUpload;
	}

	public void setUpload(boolean isUpload) {
		this.isUpload = isUpload;
	}

	public boolean isSyncDataFlag() {
		return syncDataFlag;
	}

	public void setSyncDataFlag(boolean syncDataFlag) {
		this.syncDataFlag = syncDataFlag;
	}

	public boolean isBasePath() {
		return isBasePath;
	}

	public void setBasePath(boolean isBasePath) {
		this.isBasePath = isBasePath;
	}

	public String getAliyunKey() {
		return aliyunKey;
	}

	public void setAliyunKey(String aliyunKey) {
		this.aliyunKey = aliyunKey;
	}

	public boolean isUploadFail() {
		return uploadFail;
	}

	public void setUploadFail(boolean uploadFail) {
		this.uploadFail = uploadFail;
	}

	public boolean isSyncFail() {
		return syncFail;
	}

	public void setSyncFail(boolean syncFail) {
		this.syncFail = syncFail;
	}

	@Override
	public String toString() {
		return "LocalFileDataInfo [filePath=" + filePath + ", fileType=" + fileType + ", infoData=" + infoData + ", aliyunKey=" + aliyunKey + "]";
	}

}
