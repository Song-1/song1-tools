package com.upload.aliyun.runnable.book;

import java.util.List;

public class PageDataModel<T> {
	/**
	 * 总记录数
	 */
	private int recordCount = 0;

	/**
	 * 起始记录
	 */
	private int offSet = 0;

	/**
	 * 每页条数
	 */
	private int pageSize = 0;

	/**
	 * 总页数
	 */
	private int pageNum = 1;
	/**
	 * 所有对象
	 */
	private List<T> listPageObject;
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public int getOffSet() {
		return offSet;
	}
	public void setOffSet(int offSet) {
		this.offSet = offSet;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public List<T> getListPageObject() {
		return listPageObject;
	}
	public void setListPageObject(List<T> listPageObject) {
		this.listPageObject = listPageObject;
	}
	
}

