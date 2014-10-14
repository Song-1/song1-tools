/**
 * 
 */
package com.songone.www.cherrytime.sync;

import java.util.ArrayList;
import java.util.List;

import com.songone.www.base.utils.StringUtil;
import com.songone.www.cherrytime.model.SongList;
import com.songone.www.cherrytime.model.Songs;

/**
 * @author Administrator
 *
 */
public class SyncData {
	
	private SyncSongList songList;
	private List<SyncSongs> songs;
	private String environment;
	private String style;
	public SyncSongList getSongList() {
		return songList;
	}
	public void setSongList(SyncSongList songList) {
		this.songList = songList;
	}
	public List<SyncSongs> getSongs() {
		return songs;
	}
	public void setSongs(List<SyncSongs> songs) {
		this.songs = songs;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	
	public void setSongList(SongList list){
		if(list == null){
			return;
		}
		songList = new SyncSongList();
		songList.setName(list.getName());
		songList.setImg(list.getImg());
		songList.setIcon(list.getIcon());
		songList.setIntroduction(list.getDescription());
		songList.setCreateUser(list.getCreateUser());
		songList.setCreateDate(StringUtil.getDateFromStr(list.getCreateDate(), "yyyyMMdd"));
		songList.setCount(list.getSongs());
		environment = list.getEnvironmentName();
		style = list.getStyleName();
	}
	
	public void setSongListSongs(List<Songs> list){
		if(list == null){
			return;
		}
		songs = new ArrayList<SyncSongs>();
		for (Songs s : list) {
			if(s == null){
				continue;
			}
			SyncSongs song = new SyncSongs();
			song.setName(s.getName());
			song.setImg(s.getImg());
			song.setUrl(s.getUrl());
			song.setCoderate(Integer.valueOf(StringUtil.getIntFormStr(s.getCoderate())));
			song.setTimestate(s.getTimestate());
			song.setLyric(s.getLyric());
			song.setSinger(s.getSingerName());
			songs.add(song);
		}
	}

}
