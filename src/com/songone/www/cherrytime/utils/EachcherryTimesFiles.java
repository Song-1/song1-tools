/**
 * 
 */
package com.songone.www.cherrytime.utils;

import java.io.File;

import com.songone.www.cherrytime.constants.CherryTimeConstants;
import com.songone.www.enjoycd.constants.EnjoyCDConstants;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class EachcherryTimesFiles {
	
	public static void main(String[] args) {
		
	}
	
	public static void begainEachFiles(){
		File file = new File(CherryTimeConstants.CHERRY_TIME_LOCAL_FILE_PATH);
		if(file == null || !file.exists() || !file.isDirectory()){
			return;
		}
		File[] files = file.listFiles();
		if(files == null){
			return ;
		}
		for (File f : files) {
			
		}
	}
	
	private static int eachFiles(File file){
		int result = 0;
		if(file == null || !file.exists()){
			return result;
		}else if(file.isDirectory()){
			File[] files = file.listFiles();
			if(files == null){
				return result;
			}
			int counts = 0;
			for (File f : files) {
				counts += eachFiles(f);
			}
			result = counts;
		}else if(file.isFile()){
			result = 1;
		}
		return result;
	}

}
