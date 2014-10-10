/**
 * 
 */
package com.songone.www.lizhi.json.runnable;

import com.songone.www.lizhi.json.service.UploadToAliyunDataModelService;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class SaveUploadDataRuannble implements Runnable{
	
	private String key;
	private String url;
	public SaveUploadDataRuannble(String key,String url){
		this.key = key;
		this.url = url;
	}
	public void run() {
		UploadToAliyunDataModelService uploadToAliyunDataModelService = new UploadToAliyunDataModelService();
		uploadToAliyunDataModelService.save(url, key);
	}
	
	

}
