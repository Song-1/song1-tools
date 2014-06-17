/**
 * 
 */
package com.upload.aliyun;

import java.io.File;
import java.util.List;

import com.upload.aliyun.runnable.book.BooKListThread;
import com.upload.aliyun.runnable.music.MusicListThread;

/**
 * @author Administrator
 *
 */
public class DoSaveFactory {
	public static Runnable getBean(String name,List<File> file){
		String key  = MusicConstants.DO_TYPE;
		if("book".equalsIgnoreCase(key)){
			return new BooKListThread(name, file);
		}
		else if("music".equalsIgnoreCase(key)){
			return new MusicListThread(name, file);
		}
		return null;
	}

}
