/**
 * 
 */
package com.songone.www.lizhi.json.runnable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.utils.StringUtil;
import com.songone.www.lizhi.json.constans.ThreadConstans;
import com.songone.www.lizhi.json.exceptions.FormateDescException;
import com.songone.www.lizhi.json.exceptions.FormateNameException;
import com.songone.www.lizhi.json.exceptions.FormateUserNameException;
import com.songone.www.lizhi.json.model.Radio;
import com.songone.www.lizhi.json.service.RadioService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class GetAndSaveRadioRunnable implements Runnable{
	private static final Logger logger = LogManager.getLogger(GetAndSaveRadioRunnable.class);
	private RadioService radioService = new RadioService();
	@Override
	public void run() {
		while (true) {
			int band = ThreadConstans.getRadioBand();
			if(band < 0){
				break;
			}
			//logger.debug(Thread.currentThread().getName() + " ::: band == " + band);
			getRadioAndSave(band);
		}
		
	}
	
	private void getRadioAndSave(int band) {
		Radio radio = radioService.getRadioData(band + "");
		if (radio == null) {
			logger.debug("请求电台数据::: 失败..............");
		} else {
			Radio r = radioService.getRadioById(radio.getId());
			if(r != null && r.getAu_cnt() == radio.getAu_cnt()){
				return;
			}
			boolean flag = saveRadio(radio, 0);
			if (!flag) {
				logger.debug("保存电台数据:::失败");
			}
		}
	}

	private boolean saveRadio(Radio radio, int saveCounts) {
		boolean flag = false, isExceptionFlag = false;
		try {
			flag = radioService.save(radio);
		} catch (FormateUserNameException e) {
			isExceptionFlag = true;
			String user_name = StringUtil.formateStrEncod(radio.getUser_name());
			radio.setUser_name(user_name);
		} catch (FormateNameException e) {
			isExceptionFlag = true;
			String name = StringUtil.formateStrEncod(radio.getName());
			radio.setName(name);
		} catch (FormateDescException e) {
			isExceptionFlag = true;
			String desc = StringUtil.formateStrEncod(radio.getDesc());
			radio.setDesc(desc);
		}
		if (isExceptionFlag && saveCounts < 4) {
			flag = saveRadio(radio, saveCounts + 1);
		}
		return flag;
	}

}
