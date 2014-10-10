/**
 * 
 */
package com.songone.www.cherrytime.model;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class SongList {
	private int id;
	private String name;
	private String styleName;
	private String environmentName;
	private String description;
	private int songs;
	private String img;
	private String icon;
	private String createUser;
	private String createDate;
	private int state;
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
	public String getStyleName() {
		return styleName;
	}
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	public String getEnvironmentName() {
		return environmentName;
	}
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getSongs() {
		return songs;
	}
	public void setSongs(int songs) {
		this.songs = songs;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	public String getStateName(){
		if(state == 1){
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

}
