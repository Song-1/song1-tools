package com.songone.www.base.model;

/**
 * API接口返回数据基本格式封装对象
 * @author Administrator
 *
 * @param <T>
 */
public class BaseResultBean<T> {
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
	private T data;

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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
