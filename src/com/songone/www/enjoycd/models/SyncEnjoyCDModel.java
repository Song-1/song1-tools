/**
 * 
 */
package com.songone.www.enjoycd.models;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class SyncEnjoyCDModel {
	private int id;
	private int parentId;
	private String name;
	private String url;
	private int seat;
	private boolean isAlbum;
	private boolean syncFlag;
	private String createDate;
	private String createUser;
	private boolean syncFail;
	private boolean isSinger;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
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

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public boolean isAlbum() {
		return isAlbum;
	}

	public void setAlbum(boolean isAlbum) {
		this.isAlbum = isAlbum;
	}

	public boolean isSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(boolean syncFlag) {
		this.syncFlag = syncFlag;
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

	public boolean isSyncFail() {
		return syncFail;
	}

	public void setSyncFail(boolean syncFail) {
		this.syncFail = syncFail;
	}

	public boolean isSinger() {
		return isSinger;
	}

	public void setSinger(boolean isSinger) {
		this.isSinger = isSinger;
	}

	@Override
	public String toString() {
		return "SyncEnjoyCDModel [name=" + name + ", url=" + url + ", seat=" + seat + "]";
	}
	
}
