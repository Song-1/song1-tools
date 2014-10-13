package com.songone.www.enjoy.sync;

import java.util.ArrayList;
import java.util.List;

import com.songone.www.base.utils.StringUtil;
import com.songone.www.enjoy.models.AlbumSong;


public class SyncData {
	private Album album;
	private Singer singer;
	private List<AlbumSongs> songs;
	private String styleName;
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
	}
	public List<AlbumSongs> getSongs() {
		return songs;
	}
	public void setSongs(List<AlbumSongs> songs) {
		this.songs = songs;
	}
	public Singer getSinger() {
		return singer;
	}
	public void setSinger(Singer singer) {
		this.singer = singer;
	}
	public String getStyleName() {
		return styleName;
	}
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	public void setAlbum(com.songone.www.enjoy.models.Album model){
		if(model == null){
			return;
		}
		album = new Album();
		album.setName(model.getAlbumName());
		album.setImage(model.getAlbumImg());
		album.setIcon(model.getAlbumIcon());
		album.setSongsCount(model.getAlbumSongs());
		album.setCreateUser(model.getCreateUser());
		album.setCreateDate(StringUtil.getDateFromStr(model.getCreateDate(), "yyyyMMdd"));
	}
	
	public  void setAlbumSongs(List<AlbumSong> albumSongs) {
		if(albumSongs == null || albumSongs.isEmpty()){
			return;
		}
		songs = new ArrayList<AlbumSongs>();
		for (AlbumSong albumSong : albumSongs) {
			AlbumSongs song = new AlbumSongs();
			song.setName(albumSong.getName());
			song.setUrl(albumSong.getUrl());
			song.setSeat(albumSong.getSeat());
			songs.add(song);
		}
	}
	
	public void setSinger(String name,String img ,String icon){
		singer = new Singer();
		singer.setName(name);
		singer.setIcon(icon);
		singer.setImageUrl(img);
	}
}
