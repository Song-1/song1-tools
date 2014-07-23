/**
 * 
 */
package com.upload.aliyun.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 *
 */
public class StringUtil {

	/**
	 * 获取字符串里面的数字
	 * 
	 * @param str
	 * @return
	 */
	public static String getIntFormStr(String str) {
		if (str == null || "".equals(str.trim())) {
			return "0";
		}
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static String encodeURL(String str) {
		if (str == null) {
			return "";
		}
		String theDefenc = "UTF-8";
		try {
			str = URLEncoder.encode(str, theDefenc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		str = str.replace("%2F", "/");
		str = str.replace("%3A", ":");
		return str;
	}
	public static String decodeURL(String str) {
		if (str == null) {
			return "";
		}
		String theDefenc = "UTF-8";
		try {
			str = URLDecoder.decode(str, theDefenc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String getFormateDate(String parrten) {
		if (parrten == null) {
			return getFormateDate();
		}
		Date date = new Date();
		SimpleDateFormat formate = new SimpleDateFormat(parrten);
		String str = formate.format(date);
		return str;
	}

	public static String getFormateDate() {
		Date date = new Date();
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
		String str = formate.format(date);
		return str;
	}

	public static boolean isEmptyString(String str) {
		return (str == null) || (str.trim().equals(""));
	}
	
	/**
	 * 判断是否匹配指定的正则表达式
	 * @param str
	 * @param reg
	 * @return
	 */
	public static boolean isMatchReg(String str,String reg){
		if(isEmptyString(str)){
			return false;
		}
		Pattern p = Pattern.compile(reg);  
		Matcher m = p.matcher(str); 
		return m.find();
	}

	public static void main(String[] args) throws Exception {
		String url = "http://testupload2.oss-cn-hangzhou.aliyuncs.com/test/状元听书/状元听书/职场培训/职场提升/如何成为一个成功的职业经理人/如何成为一个成功的职业经理人（高品质）/如何成为一个成功的职业经理人1.mp3";
		FileDoUtil.outLog(encodeURL(url));
		url = "http://cherrytime.oss-cn-hangzhou.aliyuncs.com/test/%E7%8A%B6%E5%85%83%E5%90%AC%E4%B9%A6/%E8%81%8C%E5%9C%BA%E5%9F%B9%E8%AE%AD/%E8%81%8C%E5%9C%BA%E6%8F%90%E5%8D%87/%E8%81%8C%E5%9C%BA%E5%A5%B3%E4%BA%BA%5B%E5%8F%88%E5%90%8D%E6%B9%BF%E5%9C%B0%5D/%E8%81%8C%E5%9C%BA%E5%A5%B3%E4%BA%BA6.mp3?OSSAccessKeyId=ndm6c0zcwyz1x6n5hqe66rig&Signature=ShAdqdmrQuCGi6g7fRSiCeOe0Q8%3D&Expires=1406146941";
		System.out.println(decodeURL(url));
	}
}
