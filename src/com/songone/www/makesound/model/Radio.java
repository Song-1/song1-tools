/**
 * 
 */
package com.songone.www.makesound.model;

import com.songone.www.base.utils.StringUtil;
import com.songone.www.makesound.constants.RadioConstants;

/**
 * 电台
 * 
 * @author Jelly.Liu
 *
 */
public class Radio {
	private String name;
	private int band;
	private int au_cnt;
	private String desc;
	private String user_name;
	private String u_thumb;
	private String cover;
	private String coverKey;
	private String u_thumbKey;
	
	private int programListId;
	private int programCounts;
	
	public int getProgramCounts() {
		return programCounts;
	}
	public void setProgramCounts(int programCounts) {
		this.programCounts = programCounts;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBand() {
		return band;
	}
	public void setBand(int band) {
		this.band = band;
	}
	public int getAu_cnt() {
		return au_cnt;
	}
	public void setAu_cnt(int au_cnt) {
		this.au_cnt = au_cnt;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getU_thumb() {
		if(!StringUtil.isEmptyString(u_thumb)){
			return RadioConstants.RADIO_U_THUMB_PREFIX + u_thumb;
		}
		return u_thumb;
	}
	public void setU_thumb(String u_thumb) {
		this.u_thumb = u_thumb;
	}
	public String getCover() {
		if(!StringUtil.isEmptyString(cover)){
			return RadioConstants.RADIO_COVER_PREFIX + cover;
		}
		return cover;
	}
	
	public String getCoverKey() {
		if(!StringUtil.isEmptyString(cover)){
			return RadioConstants.ALIYUN_SAVE_AUDIO_COVER + cover;
		}
		return coverKey;
	}
	public String getU_thumbKey() {
		if(!StringUtil.isEmptyString(u_thumb)){
			return RadioConstants.ALIYUN_SAVE_RADIO_UTHUMB + u_thumb;
		}
		return u_thumbKey;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public int getProgramListId() {
		return programListId;
	}
	public void setProgramListId(int programListId) {
		this.programListId = programListId;
	}

}
