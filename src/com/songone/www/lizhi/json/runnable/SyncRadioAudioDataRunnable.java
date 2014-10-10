/**
 * 
 */
package com.songone.www.lizhi.json.runnable;

import com.songone.www.lizhi.json.model.RadioAudio;
import com.songone.www.lizhi.json.service.RadioAudioService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class SyncRadioAudioDataRunnable implements Runnable {
	private RadioAudio radioAudio;
	public SyncRadioAudioDataRunnable(RadioAudio radioAudio){
		this.radioAudio = radioAudio;
	}
	@Override
	public void run() {
		if(radioAudio == null){
			return;
		}
		RadioAudioService radioAudioService = new RadioAudioService();
		boolean flag = radioAudioService.syncAudioData(radioAudio);
		if(flag ){
			radioAudio.setSync_flag(true);
			radioAudioService.updateSyncFlag(radioAudio);
		}
	}

}
