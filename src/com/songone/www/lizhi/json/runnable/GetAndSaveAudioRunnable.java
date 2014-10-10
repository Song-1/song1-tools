/**
 * 
 */
package com.songone.www.lizhi.json.runnable;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.utils.StringUtil;
import com.songone.www.lizhi.json.exceptions.FormateNameException;
import com.songone.www.lizhi.json.model.Radio;
import com.songone.www.lizhi.json.model.RadioAudio;
import com.songone.www.lizhi.json.service.RadioAudioService;
import com.songone.www.lizhi.json.service.RadioService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class GetAndSaveAudioRunnable implements Runnable {
	private static final Logger logger = LogManager.getLogger(GetAndSaveAudioRunnable.class);
	public String runnableName = "";
	private String band;
	private int au_cnt;
	private String radioName;
	private Radio radio;
	private RadioAudioService radioAudioService = new RadioAudioService();
	private RadioService radioService = new RadioService();

	public GetAndSaveAudioRunnable(Radio radio) {
		this.radio = radio;
		this.band = radio.getBand() + "";
		this.runnableName = "[Thread_" + band + "]";
		this.au_cnt = radio.getAu_cnt();
		this.radioName = radio.getName();
	}

	@Override
	public void run() {
		if (StringUtil.isEmptyString(runnableName)) {
			this.runnableName = Thread.currentThread().getName();
		}
		boolean flag = validateRadio(radio);
		if(flag){
			saveRadioAudioDatasByBand();
		}
	}
	
	private boolean validateRadio(Radio radio ){
		boolean flag = false;
		if (radio == null) {
			return flag;
		}
		int band = radio.getBand();
		if(band <= 0){
			return flag;
		}
		int au_cnt = radio.getAu_cnt();
		int audios = 0;
		if(au_cnt > 0 ){
			audios = radioAudioService.listAudioCountsByBand(radio.getBand());
		}
		if(audios == au_cnt){
			radio.setGetSaveAudioFlag(true);
			radioService.updateGetSaveAudioFlag(radio);
		}else{
			flag = true;
		}
		return flag;
	}

	private void saveRadioAudioDatasByBand() {
		logger.debug(runnableName + "开始获取保存荔枝FM的电台[band=" + band + "]下面的节目数据..........");
		List<RadioAudio> audios = radioAudioService.getRadioAudioDatas(band, au_cnt);
		if (audios == null) {
			logger.debug(runnableName + "电台[band=" + band + "]...下面节目数据为空");
			return;
		}
		for (RadioAudio radioAudio : audios) {
			if (radioAudio == null) {
				continue;
			}
			radioAudio.setBand(band);
			radioAudio.setRadioName(radioName);
			saveRadioAudio(radioAudio, 0);
		}

		int audioCounts = radioAudioService.listAudioCountsByBand(Integer.parseInt(band));
		if (audioCounts == au_cnt) {
			radio.setGetSaveAudioFlag(true);
			radioService.updateGetSaveAudioFlag(radio);
		}
	}

	private boolean saveRadioAudio(RadioAudio radioAudio, int index) {
		boolean flag = false;
		if (index > 2) {
			return flag;
		}
		try {
			logger.debug(runnableName + "::保存::" + radioAudio);
			flag = radioAudioService.save(radioAudio);
		} catch (FormateNameException e) {
			radioAudio.setName(StringUtil.formateStrEncod(radioAudio.getName()));
			flag = saveRadioAudio(radioAudio, index + 1);
		}
		if (flag) {
			logger.debug(runnableName + "保存电台节目数据:::" + radioAudio + ".......成功");
		} else {
			logger.debug(runnableName + "保存电台节目数据:::" + radioAudio + ".......失败");
		}
		return flag;
	}

}