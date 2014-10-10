/**
 * 
 */
package com.songone.www.base.db.mybatis;

import java.util.List;

import com.songone.www.base.utils.BaseConstants;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class PageResultModel<T> {
	
	private int start;
	private int pageSize = BaseConstants.SAVE_RADIOAUDIO_DATAS_PAGESIZE;
	private int counts;
	
	private List<T> datas;
	
	private T search;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCounts() {
		return counts;
	}

	public void setCounts(int counts) {
		this.counts = counts;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	public T getSearch() {
		return search;
	}

	public void setSearch(T search) {
		this.search = search;
	}

}
