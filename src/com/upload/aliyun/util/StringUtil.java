/**
 * 
 */
package com.upload.aliyun.util;

import java.io.UnsupportedEncodingException;
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

	public static void main(String[] args) throws Exception {
		String url = "http://testupload2.oss-cn-hangzhou.aliyuncs.com/test/状元听书/状元听书/职场培训/职场提升/如何成为一个成功的职业经理人/如何成为一个成功的职业经理人（高品质）/如何成为一个成功的职业经理人1.mp3";
		FileDoUtil.outLog(encodeURL(url));
	}
}
