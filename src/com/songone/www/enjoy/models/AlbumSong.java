/**
 * 
 */
package com.songone.www.enjoy.models;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class AlbumSong {
	
	private int id;
	private String name;
	private String url;
	private int parentId;
	private int seat;
	private int state = 1;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getSeat() {
		return seat;
	}
	public void setSeat(int seat) {
		this.seat = seat;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	public String getStateName() {
		if(state == 0){
			return "新建";
		}else if(state == 1){
			return "上传中";
		}else if(state == 2){
			return "待同步";
		}else if(state == 3){
			return "同步成功";
		}else if(state == 4){
			return "同步失败";
		}else{
			return "";
		}
	}
	
	@Override
	public String toString() {
		return "AlbumSong [name=" + name + ", url=" + url + ", parentId=" + parentId + ", seat=" + seat + ", getStateName()=" + getStateName() + "]";
	}
}
