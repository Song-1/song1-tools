/**
 * 
 */
package com.songone.www.makesound.model;

import java.util.Date;

import com.songone.www.base.utils.StringUtil;
import com.songone.www.makesound.constants.RadioConstants;

/**
 * 
 * 电台音频
 * 
 * @author Jelly.Liu
 *
 */
public class RadioAudio {
	private String name;
	private String url;
	private String cover;
	private int duration;
	private long create_time;
	private Date createDate;
	private String coverKey;
	private String sync_flag = "N";
	private String band;
	private int audioId;
	private String radioName;

	public int getAudioId() {
		return audioId;
	}

	public void setAudioId(int audioId) {
		this.audioId = audioId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCover() {
		if(!StringUtil.isEmptyString(cover)){
			return RadioConstants.AUDIO_COVER_PREFIX + cover;
		}
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getCoverKey() {
		if(!StringUtil.isEmptyString(cover)){
			return RadioConstants.ALIYUN_SAVE_AUDIO_COVER + cover;
		}
		return coverKey;
	}

	public String getSync_flag() {
		return sync_flag;
	}

	public void setSync_flag(String sync_flag) {
		this.sync_flag = sync_flag;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public Date getCreateDate() {
		if(create_time > 0){
			return new Date(create_time);
		}
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRadioName() {
		return radioName;
	}

	public void setRadioName(String radioName) {
		this.radioName = radioName;
	}

}
