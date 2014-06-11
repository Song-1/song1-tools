package com.gary.util;

import java.io.IOException;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;
import org.farng.mp3.id3.ID3v1;
import org.farng.mp3.lyrics3.AbstractLyrics3;

/**
 * MP3工具
 * 
 * supports:
 * ID3 v1 & v1.1
 * Lyrics3 v1 & v2
 * ID3 v2.2 & v2.3 & v2.4 tags
 * MP3 Frame Headers
 * 
 * @author gary
 *
 */
public class MP3Util {

	public static MP3Info getMP3Info(String path) throws IOException, TagException{
		MP3File file = new MP3File(path);
		AbstractID3v2 id3v2 = file.getID3v2Tag();
		ID3v1 id3v1 = file.getID3v1Tag();
		AbstractLyrics3 lyrics = file.getLyrics3Tag();
		if(id3v2 != null){
			return new MP3Info(id3v2.getLeadArtist(), id3v2.getSongTitle(), 
					id3v2.getAlbumTitle(), id3v2.getTrackNumberOnAlbum(), 
					id3v2.getYearReleased(), lyrics == null ? null : lyrics.getSongLyric());
		}else if(id3v1 != null){
			return new MP3Info(id3v1.getLeadArtist(), id3v1.getSongTitle(), 
					id3v1.getAlbumTitle(), id3v1.getTrackNumberOnAlbum(), 
					id3v1.getYearReleased(), lyrics == null ? null : lyrics.getSongLyric());
		}else{
			return null;
		}
	}
}
