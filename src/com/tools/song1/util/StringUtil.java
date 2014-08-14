/**
 * 
 */
package com.tools.song1.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
		if (isEmptyString(str)) {
			return "0";
		}
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * URL转成UTF-8编码
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeURL(String str) {
		if (isEmptyString(str)) {
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

	/**
	 * 获取当前时间的格式化字符串
	 * 
	 * @param parrten 格式字符串
	 * @return
	 */
	public static String getFormateDate(String parrten) {
		if (isEmptyString(parrten)) {
			return getFormateDate();
		}
		Date date = new Date();
		SimpleDateFormat formate = new SimpleDateFormat(parrten);
		String str = formate.format(date);
		return str;
	}

	/**
	 * 获取默认格式:[yyyy-MM-dd hh:mm:ss:SSS]格式化当前时间的字符串
	 * 
	 * @return
	 */
	public static String getFormateDate() {
		Date date = new Date();
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
		String str = formate.format(date);
		return str;
	}
	
	public static String getFormateDate(Date date,String parrten) {
		if(date == null){
			return "";
		}
		if (isEmptyString(parrten)) {
			return getFormateDate();
		}
		SimpleDateFormat formate = new SimpleDateFormat(parrten);
		String str = formate.format(date);
		return str;
	}

	/**
	 * 判断字符串是否为空,str == null or "" return true.<br>
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmptyString(String str) {
		if (str == null) {
			return true;
		} else if ("".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否匹配指定的正则表达式
	 * 
	 * @param str
	 * @param reg
	 * @return
	 */
	public static boolean isMatchReg(String str, String reg) {
		if (isEmptyString(str)) {
			return false;
		}
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		return m.find();
	}
	
	public static String getFileSize(long size){
		if(size <= 0){
			return size + "";
		}else if(size < 1024L){
			return size + "b";
		}else if(size < 1048576L){
			return sub(size, 1024L)+"KB";
		}else if(size < 1073741824L){
			return sub(size, 1048576L)+"MB";
		}else if(size >= 1073741824L){
			return sub(size, 1073741824L)+"GB";
		}else{
			return size + "";
		}
	}
	
	public static double sub(long size,long l){
		double d = (double)size / l;
		BigDecimal b = new BigDecimal(d);
		return b.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	// /// test
	public static void main(String[] args) throws Exception {
		String url = "http://testupload2.oss-cn-hangzhou.aliyuncs.com/test/状元听书/状元听书/职场培训/职场提升/如何成为一个成功的职业经理人/如何成为一个成功的职业经理人（高品质）/如何成为一个成功的职业经理人1.mp3";
		FileDoUtil.outLog(encodeURL(url));
	}
}
