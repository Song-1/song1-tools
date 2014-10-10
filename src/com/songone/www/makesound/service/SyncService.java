/**
 * 
 */
package com.songone.www.makesound.service;

import java.util.List;

import com.songone.www.makesound.model.MakesoundSyncRadioModel;

/**
 * 
 * @author Jelly.Liu
 *
 */
public interface SyncService {
	public void save(MakesoundSyncRadioModel model);
	
	public List<MakesoundSyncRadioModel> list(String band);
	
	public MakesoundSyncRadioModel listByBand(String band);
	
	public List<MakesoundSyncRadioModel> listSyncAudioDatas(int start,int pageSize);
	
}
