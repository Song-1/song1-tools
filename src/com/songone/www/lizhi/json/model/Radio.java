/**
 * 
 */
package com.songone.www.lizhi.json.model;

import java.util.List;

import com.songone.www.base.utils.StringUtil;
import com.songone.www.makesound.constants.RadioConstants;

/**
 * 电台
 * 
 * @author Jelly.Liu
 *
 */
public class Radio {
	private long id;
	private String name;
	private int band;
	private int au_cnt;
	private String desc;
	private String user_name;
	private String u_thumb;
	private String cover;
	private int uid;
	private List<RadioTag> tags;
	private String tagName;
	
	/////
	private String coverKey;
	private String u_thumbKey;
	private int programListId;
	private int programCounts;
	
	///
	private boolean getSaveAudioFlag;
	private boolean syncFlag;
	
	public boolean isGetSaveAudioFlag() {
		if(au_cnt <= 0){
			return true;
		}
		return getSaveAudioFlag;
	}
	public void setGetSaveAudioFlag(boolean getSaveAudioFlag) {
		this.getSaveAudioFlag = getSaveAudioFlag;
	}
	public boolean isSyncFlag() {
		return syncFlag;
	}
	public void setSyncFlag(boolean syncFlag) {
		this.syncFlag = syncFlag;
	}
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
			if(u_thumb.startsWith(RadioConstants.RADIO_U_THUMB_PREFIX)){
				return u_thumb;
			}
			return RadioConstants.RADIO_U_THUMB_PREFIX + u_thumb;
		}
		return u_thumb;
	}
	public void setU_thumb(String u_thumb) {
		this.u_thumb = u_thumb;
	}
	public String getCover() {
		if(!StringUtil.isEmptyString(cover)){
			if(cover.startsWith(RadioConstants.RADIO_COVER_PREFIX)){
				return cover;
			}
			return RadioConstants.RADIO_COVER_PREFIX + cover;
		}
		return cover;
	}
	
	public String getCoverKey() {
		if(!StringUtil.isEmptyString(cover)){
			if(cover.startsWith(RadioConstants.RADIO_COVER_PREFIX )){
				String str = cover.replace(RadioConstants.RADIO_COVER_PREFIX , "");
				return RadioConstants.ALIYUN_SAVE_RADIO_COVER + str;
			}
			return RadioConstants.ALIYUN_SAVE_RADIO_COVER + cover;
		}
		return coverKey;
	}
	public String getU_thumbKey() {
		if(!StringUtil.isEmptyString(u_thumb)){
			if(u_thumb.startsWith(RadioConstants.RADIO_U_THUMB_PREFIX)){
				String str = u_thumb.replace(RadioConstants.RADIO_U_THUMB_PREFIX, "");
				return RadioConstants.ALIYUN_SAVE_RADIO_UTHUMB + str;
			}
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public List<RadioTag> getTags() {
		return tags;
	}
	public void setTags(List<RadioTag> tags) {
		this.tags = tags;
	}
	public String getTagName() {
		if(tags!= null){
			String str = "";
			for (RadioTag tag : tags) {
				if(tag == null){
					continue;
				}
				str += tag.getName() + ";";
			}
			return str;
		}
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	@Override
	public String toString() {
		return "Radio [name=" + name + ", band=" + band + ", user_name=" + user_name + "]";
	}
}
