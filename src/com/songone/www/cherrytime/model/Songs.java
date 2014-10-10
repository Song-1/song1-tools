/**
 * 
 */
package com.songone.www.cherrytime.model;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class Songs {
	
	private int id;
	private String name;
	private int parentId;
	private String singerName;
	private String url;
	private String coderate;
	private String timestate;
	private String img;
	private int state;
	private String lyric;
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
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getSingerName() {
		return singerName;
	}
	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCoderate() {
		return coderate;
	}
	public void setCoderate(String coderate) {
		this.coderate = coderate;
	}
	public String getTimestate() {
		return timestate;
	}
	public void setTimestate(String timestate) {
		this.timestate = timestate;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	public String getStateName(){
		switch (state) {
		case 1:
			return "上传中";
		case 2:
			return "待同步";
		default:
			break;
		}
		return "";
	}
	public String getLyric() {
		return lyric;
	}
	public void setLyric(String lyric) {
		this.lyric = lyric;
	}

}
