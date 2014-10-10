/**
 * 
 */
package com.songone.www.lizhi.json.model;

import java.util.List;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class RadioDataListBtTag {

	private String model;
	private int p;
	private RadioList radio_list;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public RadioList getRadio_list() {
		return radio_list;
	}

	public void setRadio_list(RadioList radio_list) {
		this.radio_list = radio_list;
	}

	public List<Radio> getRadios() {
		if (radio_list != null) {
			return radio_list.getRadios();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private class RadioList {
		private int cnt;
		private List<Radio> radios;

		public int getCnt() {
			return cnt;
		}

		public void setCnt(int cnt) {
			this.cnt = cnt;
		}

		public List<Radio> getRadios() {
			return radios;
		}

		public void setRadios(List<Radio> radios) {
			this.radios = radios;
		}
	}

}
