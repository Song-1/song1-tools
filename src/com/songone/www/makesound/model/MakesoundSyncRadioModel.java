/**
 * 
 */
package com.songone.www.makesound.model;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class MakesoundSyncRadioModel {
	private int id;
	private String band;
	private int audios;
	private int programListId;
	private int programCounts;
	private String programListName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProgramListName() {
		return programListName;
	}
	public void setProgramListName(String programListName) {
		this.programListName = programListName;
	}
	public String getBand() {
		return band;
	}
	public void setBand(String band) {
		this.band = band;
	}
	public int getAudios() {
		return audios;
	}
	public void setAudios(int audios) {
		this.audios = audios;
	}
	public int getProgramListId() {
		return programListId;
	}
	public void setProgramListId(int programListId) {
		this.programListId = programListId;
	}
	
	public int getProgramCounts() {
		return programCounts;
	}
	public void setProgramCounts(int programCounts) {
		this.programCounts = programCounts;
	}
	@Override
	public String toString() {
		return "MakesoundSyncRadioModel [id=" + id + ", band=" + band + ", audios=" + audios + ", programListId=" + programListId + ", programCounts=" + programCounts + ", programListName=" + programListName + "]";
	}
	
}
