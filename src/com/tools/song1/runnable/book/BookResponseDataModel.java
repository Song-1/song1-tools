/**
 * 
 */
package com.tools.song1.runnable.book;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class BookResponseDataModel {
	private Integer id;
	private String name;
	private String description;
	private String icon;
	private String cover;
	private Integer recommand;// 推荐度，越大排越前
	private String bookTypeCode;
	private Integer bookTypeId;
	private String bookTypeName;
	private String url;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public Integer getRecommand() {
		return recommand;
	}
	public void setRecommand(Integer recommand) {
		this.recommand = recommand;
	}
	public String getBookTypeCode() {
		return bookTypeCode;
	}
	public void setBookTypeCode(String bookTypeCode) {
		this.bookTypeCode = bookTypeCode;
	}
	public Integer getBookTypeId() {
		return bookTypeId;
	}
	public void setBookTypeId(Integer bookTypeId) {
		this.bookTypeId = bookTypeId;
	}
	public String getBookTypeName() {
		return bookTypeName;
	}
	public void setBookTypeName(String bookTypeName) {
		this.bookTypeName = bookTypeName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	

}



