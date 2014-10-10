/**
 * 
 */
package com.songone.www.base.utils;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Jelly.Liu
 *
 */
@SuppressWarnings("resource")
public class ConsoleInputUtil {
	private static final Logger logger = LogManager.getLogger(ConsoleInputUtil.class);
	
	private static final String REINPUT_ERROR_TEXT = "[输入不合法,请重新输入]";
	/**
	 * 获取输入的字符串
	 * 
	 * @param text 输入说明
	 * @return
	 */
	public static String getString(String text) {
		printText(text);
		Scanner scanner = new Scanner(System.in);
		return scanner.nextLine();
	}

	/**
	 * 获取输入的数字
	 * 
	 * @param text 输入说明
	 * @return
	 */
	public static int getInt(String text) {
		printText(text);
		Scanner scanner = new Scanner(System.in);
		int result = -1;
		try{
		result = scanner.nextInt();
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			if(!text.startsWith(REINPUT_ERROR_TEXT)){
				text = REINPUT_ERROR_TEXT + text;
			}
			result = getInt(text);
		}
		return result;
	}

	private static void printText(String text) {
		if (!StringUtil.isEmptyString(text)) {
			System.out.print(text);
		}
	}

	// ////test main
	public static void main(String[] args) {
		String text = "请输入:";
		String str = getString(text);
		System.out.println(str);
		if(StringUtil.isEmptyString(str)){
			System.out.println(true);
		}else{
			System.out.println(false);
		}
		//System.out.println(getInt(text));
	}

}
