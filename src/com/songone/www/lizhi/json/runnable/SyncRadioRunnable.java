/**
 * 
 */
package com.songone.www.lizhi.json.runnable;

import com.songone.www.lizhi.json.model.Radio;
import com.songone.www.lizhi.json.service.RadioService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class SyncRadioRunnable implements Runnable{
	private Radio radio;
	
	public SyncRadioRunnable(Radio radio){
		this.radio = radio;
	}
	@Override
	public void run() {
		if(radio == null){
			return;
		}
		RadioService radioService = new RadioService();
		boolean flag = radioService.syncRadioToServer(radio);
		if(flag ){
			radio.setSyncFlag(true);
			radioService.updateFlagAndAudios(radio);
		}
	}

}
