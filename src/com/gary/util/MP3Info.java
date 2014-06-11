package com.gary.util;
/**
 * MP3信息
 * @author gary
 *
 */
public class MP3Info {

	/**
	 * 歌手
	 */
	private String artist;
	/**
	 * 歌名
	 */
	private String songTitle;
	/**
	 * 专辑名称
	 */
	private String albumTitle;
	/**
	 * 音轨
	 */
	private String trackNumberOnAlbum;
	/**
	 * 发行年份
	 */
	private String yearReleased;
	/**
	 * 歌词
	 */
	private String songLyric;
	
	/**
	 * 
	 * <p>Title: </p> 
	 * <p>Description: MP3信息</p> 
	 * @param artist 歌手
	 * @param songTitle 歌曲名:
	 * @param albumTitle
	 * @param trackNumberOnAlbum
	 * @param yearReleased
	 * @param songLyric
	 */
	public MP3Info(String artist, String songTitle, String albumTitle, 
			String trackNumberOnAlbum, String yearReleased, String songLyric) {
		this.artist = artist;
		this.songTitle = songTitle;
		this.albumTitle = albumTitle;
		this.trackNumberOnAlbum = trackNumberOnAlbum;
		this.yearReleased = yearReleased;
		this.songLyric = songLyric;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getSongTitle() {
		return songTitle;
	}
	
	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}
	
	public String getAlbumTitle() {
		return albumTitle;
	}

	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}
	
	public String getTrackNumberOnAlbum() {
		return trackNumberOnAlbum;
	}
	
	public void setTrackNumberOnAlbum(String trackNumberOnAlbum) {
		this.trackNumberOnAlbum = trackNumberOnAlbum;
	}
	
	public String getYearReleased() {
		return yearReleased;
	}
	
	public void setYearReleased(String yearReleased) {
		this.yearReleased = yearReleased;
	}
	
	public String getSongLyric() {
		return songLyric;
	}
	
	public void setSongLyric(String songLyric) {
		this.songLyric = songLyric;
	}
	
	public void printMP3Info(){
		System.out.println("歌手:" + artist);
		System.out.println("歌曲名:" + songTitle);
		System.out.println("专辑名:" + albumTitle);
		System.out.println("音轨:" + trackNumberOnAlbum);
		System.out.println("发行年份:" + yearReleased);
		System.out.println("歌词:" + songLyric);
	}
}