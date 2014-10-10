/**
 * 
 */
package com.songone.www.lizhi.json.runnable;

import java.util.List;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.db.mybatis.PageResultModel;
import com.songone.www.lizhi.json.constans.EachDoDatasType;
import com.songone.www.lizhi.json.constans.ThreadConstans;
import com.songone.www.lizhi.json.model.Radio;
import com.songone.www.lizhi.json.service.RadioAudioService;
import com.songone.www.lizhi.json.service.RadioService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class ToDoTaskRunnable extends TimerTask {
	private RadioService radioService = new RadioService();
	private RadioAudioService radioAudioService = new RadioAudioService();
	private EachDoDatasType type;

	public ToDoTaskRunnable(EachDoDatasType type) {
		this.type = type;
	}

	@Override
	public void run() {
		eachDoDatas();
	}

	private void eachDoDatas() {
		int start = 0;
		int counts = 0;
		while (true) {
			if (start != 0 && start >= counts) {
				break;
			}
			PageResultModel<Radio> page = pageRadio(start, 0);
			start += page.getPageSize();
			counts = page.getCounts();
			List<Radio> radios = page.getDatas();
			if (radios == null) {
				continue;
			}
			for (Radio radio : radios) {
				Runnable runnable = createRunnable(radio);
				ThreadConstans.putJob(runnable);
			}
		}
	}

	private PageResultModel<Radio> pageRadio(int start, int pageSize) {
		PageResultModel<Radio> page = new PageResultModel<Radio>();
		switch (type) {
		case SYNC_RADIO:
			page = radioService.queryModelByPageForSync(start, 0);
			break;
		case GET_SAVE_AUDIO:
			page = radioService.listRadioByPageForDoRadioAudio(start, 0);
			break;
		case SYNC_AUDIO:

			break;

		default:
			break;
		}
		return page;
	}

	private Runnable createRunnable(Radio radio) {
		Runnable runnable = null;
		switch (type) {
		case SYNC_RADIO:
			runnable = new SyncRadioRunnable(radio);
			break;
		case GET_SAVE_AUDIO:
			runnable = new GetAndSaveAudioRunnable(radio);
			break;
		case SYNC_AUDIO:
			break;

		default:
			break;
		}
		return runnable;
	}

}
