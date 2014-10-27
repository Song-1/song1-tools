/**
 * 
 */
package com.songone.www.aliyun.runnable;

import java.util.List;

import com.songone.www.aliyun.model.AliyunMappingFileModel;
import com.songone.www.aliyun.model.UploadServiceObserver;
import com.songone.www.aliyun.service.AliyunMappingFileModelService;
import com.songone.www.base.utils.BaseConstants;
import com.songone.www.base.utils.HttpClientUtil;
import com.songone.www.cherrytime.constants.CherryTimeConstants;
import com.songone.www.cherrytime.model.Songs;
import com.songone.www.cherrytime.service.SongListService;
import com.songone.www.cherrytime.service.SongsService;
import com.songone.www.enjoy.models.AlbumSong;
import com.songone.www.enjoy.service.AlbumService;
import com.songone.www.enjoy.service.AlbumSongService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class UpdateOtherDataRunnable implements Runnable {
	private UploadServiceObserver observer;
	private AlbumSongService albumSongService = new AlbumSongService();
	private AlbumService albumService = new AlbumService();
	private AliyunMappingFileModelService aliyunMappingFileModelService = new AliyunMappingFileModelService();
	private SongListService songListService = new SongListService();
	private SongsService songsService = new SongsService();
	
	public UpdateOtherDataRunnable(UploadServiceObserver observer) {
		this.observer = observer;
	}

	public void update(int dateType, int id) {
		if (id <= 0) {
			return;
		}
		switch (dateType) {
		case 1:
//			AlbumSong songs = albumSongService.getModelById(id);
//			if(songs != null){
//				songs.setState(2);
//				albumSongService.update(songs);
//				albumService.updateAlbumState(songs.getParentId());
//			}
//			String url = BaseConstants.PROTOCOL + BaseConstants.SONG_ONE_SERVER_HOST + CherryTimeConstants.SONG_ONE_CHERRYTIME_API_SYNC_SONGLIST;
//			HttpClientUtil.doPut(url, map);
			albumSongService.updateAlbumSongsState(id);
			break;
		case 2:
			// // 樱桃时光
			Songs song  = songsService.getModelById(id);
			if(song != null){
				song.setState(2);
				songsService.update(song);
				songListService.updateListState(song.getParentId());
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void run() {
		List<AliyunMappingFileModel> datas = null;
		if(observer != null){
			datas = observer.getDatas();
		}
		if(datas == null || datas.isEmpty()){
			return;
		}
		for (AliyunMappingFileModel model : datas) {
			if(model == null){
				continue;
			}
			int id = model.getForeignKeyId();
			update(model.getDataType(), id);
			model.setSyncFlag(true);
			aliyunMappingFileModelService.update(model);
		}

	}

}
