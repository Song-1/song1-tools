/**
 * 
 */
package com.songone.www.lizhi.json.model;

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
	@Override
	public String toString() {
		return "RadioTag [id=" + id + ", name=" + name + ", tags=" + tags + "]";
	}
}
