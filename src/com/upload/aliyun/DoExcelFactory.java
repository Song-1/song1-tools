/**
 * 
 */
package com.upload.aliyun;

import java.util.HashMap;
import java.util.Map;

import com.upload.aliyun.runnable.book.BookDataGetFromExcel;
import com.upload.aliyun.runnable.music.MusicDataGetFromExcel;
import com.upload.aliyun.util.POIUtil;

/**
 * @author Administrator
 *
 */
public class DoExcelFactory {
	
	private static Map<String,POIUtil> keyObjetMap = new HashMap<String, POIUtil>();
	static{
		keyObjetMap.put("book", new BookDataGetFromExcel());
		keyObjetMap.put("music", new MusicDataGetFromExcel());
	}
	
	public static POIUtil getBean(){
		String key  = MusicConstants.DO_TYPE;
		for (Map.Entry<String,POIUtil> entry : keyObjetMap.entrySet()) {
			if(key.equalsIgnoreCase(entry.getKey())){
				return entry.getValue();
			}
		}
		return null;
	}

}
