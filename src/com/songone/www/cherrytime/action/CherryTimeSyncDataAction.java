/**
 * 
 */
package com.songone.www.cherrytime.action;

import java.util.List;

import org.apache.http.HttpStatus;

import com.songone.www.base.model.HttpResponseData;
import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.HttpClientUtil;
import com.songone.www.cherrytime.constants.CherryTimeConstants;
import com.songone.www.cherrytime.model.SongList;
import com.songone.www.cherrytime.model.Songs;
import com.songone.www.cherrytime.service.SongListService;
import com.songone.www.cherrytime.service.SongsService;
import com.songone.www.cherrytime.sync.SyncData;

/**
 * @author Administrator
 *
 */
public class CherryTimeSyncDataAction {
	private SongListService songListService = new SongListService();
	private SongsService songsService = new SongsService();
	
	public void syncDatas(){
		String url = BaseConstants.PROTOCOL + BaseConstants.SONG_ONE_SERVER_HOST + CherryTimeConstants.SONG_ONE_CHERRYTIME_API_SYNC_SONGLIST;
		List<SongList> songLists = null;
		while(true){
			songLists = songListService.listSongListsForSync();
			if(songLists == null || songLists.isEmpty()){
				break;
			}
			for (SongList songList : songLists) {
				if(songList == null){
					continue;
				}
				SyncData data = new SyncData();
				data.setSongList(songList);
				List<Songs> songs = songsService.listSongsByParentId(songList.getId());
				data.setSongListSongs(songs);
				HttpResponseData responseData = HttpClientUtil.doPost(url, data);
				if(responseData != null && responseData.getCode() == HttpStatus.SC_OK){
					songList.setState(4); //同步成功
				}else{
					songList.setState(5); // 同步失败
					//songList.setRemark(str);
				}
				songListService.update(songList);
			}
		}
	}
}
