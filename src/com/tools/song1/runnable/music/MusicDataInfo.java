/**
 * 
 */
package com.tools.song1.runnable.music;


/**
 * @author Administrator
 *
 */
public class MusicDataInfo {
	public MusicDataInfo() {
	}
	private String listName;
	private String desc;
	private String listType;
	private String time;
	private String enverionment;
	private String category;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getEnverionment() {
		return enverionment;
	}
	public void setEnverionment(String enverionment) {
		this.enverionment = enverionment;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getListName() {
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getListType() {
		return listType;
	}
	public void setListType(String listType) {
		this.listType = listType;
	}
	@Override
	public String toString() {
		return "MusicDataInfo [listName=" + listName + ", desc=" + desc + ", listType=" + listType + ", time=" + time + ", enverionment=" + enverionment + ", category=" + category + "]";
	}
	


}
