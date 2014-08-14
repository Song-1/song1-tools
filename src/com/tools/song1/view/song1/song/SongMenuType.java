/**
 * 
 */
package com.tools.song1.view.song1.song;

/**
 * @author Administrator
 *
 */
public enum SongMenuType {
	SONG("樱桃时光"),BOOK("状元听书"),ENJOYCD("享CD");
	
	private String text;
	private SongMenuType(String text){
		this.text = text;
	}
	
	public String getText(){
		return this.text;
	}

}
