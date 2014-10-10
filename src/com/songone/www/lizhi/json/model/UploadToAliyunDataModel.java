/**
 * 
 */
package com.songone.www.lizhi.json.model;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class UploadToAliyunDataModel {

	private int id;

	private String url;

	private String aliyunKey;

	private boolean isUpload;

	private boolean uploadFail;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAliyunKey() {
		return aliyunKey;
	}

	public void setAliyunKey(String aliyunKey) {
		this.aliyunKey = aliyunKey;
	}

	public boolean isUpload() {
		return isUpload;
	}

	public void setUpload(boolean isUpload) {
		this.isUpload = isUpload;
	}

	public boolean isUploadFail() {
		return uploadFail;
	}

	public void setUploadFail(boolean uploadFail) {
		this.uploadFail = uploadFail;
	}

	@Override
	public String toString() {
		return "UploadToAliyunDataModel [url=" + url + ", aliyunKey=" + aliyunKey + "]";
	}
	
	

}
