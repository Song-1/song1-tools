package com.upload.aliyun.runnable.book;

public class BaseResultBean {
	/**
	 * 返回码,具体设置参考返回码说明
	 */
	private String status;
	/**
	 * 返回操作信息
	 */
	private String message;
	/**
	 * 返回的数据
	 */
	private PageDataModel<BookResponseDataModel> data;
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PageDataModel<BookResponseDataModel> getData() {
		return data;
	}

	public void setData(PageDataModel<BookResponseDataModel> data) {
		this.data = data;
	}

}
