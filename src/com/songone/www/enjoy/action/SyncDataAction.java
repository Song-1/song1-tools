/**
 * 
 */
package com.songone.www.enjoy.action;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.HttpClientUtil;
import com.songone.www.enjoy.models.Album;
import com.songone.www.enjoy.models.AlbumSong;
import com.songone.www.enjoy.service.AlbumService;
import com.songone.www.enjoy.service.AlbumSongService;
import com.songone.www.enjoy.sync.SyncData;
import com.songone.www.enjoycd.constants.EnjoyCDConstants;

/**
 * @author Administrator
 *
 */
public class SyncDataAction {
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(SyncDataAction.class);
	private AlbumService albumService = new AlbumService();
	private AlbumSongService albumSongService = new AlbumSongService();
	
	public void syncDatas() throws Exception{
		String url = BaseConstants.SONG_ONE_SERVER_HOST + EnjoyCDConstants.SONG_ONE_ENJOY_API_SYNC_ALBUM;
		List<Album> albums = null;
//		while(true){
			albums = albumService.listAlbumForSync(null);
			if(albums == null || albums.isEmpty()){
				return;
//				break;
			}
			for (Album album : albums) {
				SyncData data = new SyncData();
				data.setAlbum(album);
				data.setSinger(album.getSingerName(), album.getSingerImg(), album.getSingerIcon());
				data.setStyleName(album.getStyleName());
				List<AlbumSong> songs = albumSongService.listForSyncByParentId(album.getId());
				data.setAlbumSongs(songs);
				String str = HttpClientUtil.doPostByJson(url, data);
				if("\"OK\"\n".equals(str)){
					album.setState(4); //同步成功
				}else{
					album.setState(5); // 同步失败
					album.setRemark(str);
				}
				albumService.update(album);
			}
//		}
	}
}
