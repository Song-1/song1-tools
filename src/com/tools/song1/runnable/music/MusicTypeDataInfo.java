/**
 * 
 */
package com.tools.song1.runnable.music;

/**
 * @author Administrator
 *
 */
public class MusicTypeDataInfo {
	
	private String time;
	private String enverionment;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getEnverionment() {
		return enverionment;
	}
	public void setEnverionment(String enverionment) {
		this.enverionment = enverionment;
	}
	@Override
	public String toString() {
		return "MusicTypeDataInfo [time=" + time + ", enverionment=" + enverionment + "]";
	}
	

}
