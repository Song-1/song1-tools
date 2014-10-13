package com.songone.www.enjoy.sync;

import java.util.Date;



public class Album {

	private int id;
	private String name;
	private String image;
	private String icon;
	
	private String description;
	private String createUser;
	private Date createDate;
	
	
	private Date releaseTime;
	private int songsCount;
	private String releaseCompany;
	
	private String compose;
	
	private String playing;

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public int getSongsCount() {
		return songsCount;
	}

	public void setSongsCount(int songsCount) {
		this.songsCount = songsCount;
	}

	public String getReleaseCompany() {
		return releaseCompany;
	}

	public void setReleaseCompany(String releaseCompany) {
		this.releaseCompany = releaseCompany;
	}

	public String getCompose() {
		return compose;
	}

	public void setCompose(String compose) {
		this.compose = compose;
	}

	public String getPlaying() {
		return playing;
	}

	public void setPlaying(String playing) {
		this.playing = playing;
	}
}
