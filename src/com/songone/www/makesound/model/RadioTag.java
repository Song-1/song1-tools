/**
 * 
 */
package com.songone.www.makesound.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 荔枝FM电台分类
 * @author Jelly.Liu
 *
 */
public class RadioTag implements Serializable{
	private static final long serialVersionUID = 519842276260889238L;
	private int id;
	private String name;
	private List<RadioTag> tags = new ArrayList<RadioTag>();
	private int parentId;
	private int tagId;
	/**
	 * 造音社节目单分类的ID
	 */
	private String makeSoundCategoryId;
	/**
	 * 造音社节目单分类的CODE
	 */
	private String makeSoundCategoryCode;
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
	public List<RadioTag> getTags() {
		return tags;
	}
	public void setTags(List<RadioTag> tags) {
		this.tags = tags;
	}
	public String getMakeSoundCategoryId() {
		return makeSoundCategoryId;
	}
	public void setMakeSoundCategoryId(String makeSoundCategoryId) {
		this.makeSoundCategoryId = makeSoundCategoryId;
	}
	public String getMakeSoundCategoryCode() {
		return makeSoundCategoryCode;
	}
	public void setMakeSoundCategoryCode(String makeSoundCategoryCode) {
		this.makeSoundCategoryCode = makeSoundCategoryCode;
	}
	
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
	public int getTagId() {
		return tagId;
	}
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
	
	@Override
	public String toString() {
		return "RadioTag [id=" + id + ", name=" + name + ", tags=" + tags + ", parentId=" + parentId + ", tagId=" + tagId + ", makeSoundCategoryId=" + makeSoundCategoryId + ", makeSoundCategoryCode=" + makeSoundCategoryCode + "]";
	}
	
}
