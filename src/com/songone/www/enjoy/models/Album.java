/**
 * 
 */
package com.songone.www.enjoy.models;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class Album {
	
	private int id;
	private String albumName;
	private String singerName;
	private String styleName;
	private String albumImg;
	private String albumIcon;
	private String singerImg;
	private String singerIcon;
	private int albumSongs;
	private String remark;
	private int state = 1;
	
	private String createUser;
	private String createDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public String getSingerName() {
		return singerName;
	}
	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}
	public String getStyleName() {
		return styleName;
	}
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	public String getAlbumImg() {
		return albumImg;
	}
	public void setAlbumImg(String albumImg) {
		this.albumImg = albumImg;
	}
	public String getAlbumIcon() {
		return albumIcon;
	}
	public void setAlbumIcon(String albumIcon) {
		this.albumIcon = albumIcon;
	}
	public String getSingerImg() {
		return singerImg;
	}
	public void setSingerImg(String singerImg) {
		this.singerImg = singerImg;
	}
	public String getSingerIcon() {
		return singerIcon;
	}
	public void setSingerIcon(String singerIcon) {
		this.singerIcon = singerIcon;
	}
	public int getAlbumSongs() {
		return albumSongs;
	}
	public void setAlbumSongs(int albumSongs) {
		this.albumSongs = albumSongs;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
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
	public String getStateName(){
		if(state == 0){
			return "";
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
		return "Album [albumName=" + albumName + ", singerName=" + singerName + ", styleName=" + styleName + ", albumSongs=" + albumSongs + ", createUser=" + createUser + ", getStateName()=" + getStateName() + "]";
	}

}
