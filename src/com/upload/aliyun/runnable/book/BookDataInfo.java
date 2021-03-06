/**
 * 
 */
package com.upload.aliyun.runnable.book;

import java.util.List;
import java.util.UUID;

import com.upload.aliyun.util.FileDoUtil;

/**
 * @author Administrator
 *
 */
public class BookDataInfo {
	private String id;
	private String name;
	private String typeLevelOne;
	private String typeLevelTwo;
	private String author;
	private String player;
	private String bookInfo;
	private String desc;
	private String img;
	private List<String> books;
	private String uuid;
	public BookDataInfo() {
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name != null){
			String uuidStr = UUID.randomUUID().toString();
			uuid = uuidStr.replace("-", "").toUpperCase();
		}
		this.name = name;
	}

	public String getTypeLevelOne() {
		return typeLevelOne;
	}

	public void setTypeLevelOne(String typeLevelOne) {
		this.typeLevelOne = typeLevelOne;
	}

	public String getTypeLevelTwo() {
		return typeLevelTwo;
	}

	public void setTypeLevelTwo(String typeLevelTwo) {
		this.typeLevelTwo = typeLevelTwo;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getBookInfo() {
		return bookInfo;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public List<String> getBooks() {
		return books;
	}

	public void setBooks(List<String> books) {
		this.books = books;
	}

	public void addBook(String str) {
		if (str != null) {
			books.add(str);
		}
	}

	@Override
	public String toString() {
		return "BookDataInfo [id=" + id + ", name=" + name + ", typeLevelOne=" + typeLevelOne + ", typeLevelTwo=" + typeLevelTwo + ", author=" + author + ", player=" + player + ", bookInfo=" + bookInfo + ", desc=" + desc + ", img=" + img + ", books=" + books + ", uuid=" + uuid + "]";
	}

}
