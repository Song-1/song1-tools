/**
 * 
 */
package com.songone.www.makesound.service;

import java.util.List;

import com.songone.www.makesound.model.RadioAudio;

/**
 * 
 * @author Jelly.Liu
 *
 */
public interface RadioAudioService {
	public boolean saveOrUpdate(RadioAudio model);
	
	public RadioAudio listByNameAndBand(String name,String band);
	
	public List<RadioAudio> listSyncAudioDatas(int start,int pageSize);
}
