/**
 * 
 */
package com.songone.www.base.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.text.ParseException;
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

	/**
	 * 解码UTF-8编码
	 * 
	 * @param str
	 * @return
	 */
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

	/**
	 * 按指定的日期格式，格式化时间日期
	 * 
	 * @param date
	 * @param parrten
	 * @return
	 */
	public static String getFormateDate(Date date, String parrten) {
		if (date == null) {
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
		} else if ("".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否为空,str == null or "" return true.<br>
	 * 
	 * <pre>
	 * 		1.进行去除空格处理判断
	 * </pre>
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmptyStringStrict(String str) {
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

	/**
	 * 解析文件的大小
	 * 
	 * @param size
	 * @return
	 */
	public static String getFileSize(long size) {
		if (size <= 0) {
			return size + "";
		} else if (size < 1024L) {
			return size + "b";
		} else if (size < 1048576L) {
			return sub(size, 1024L) + "KB";
		} else if (size < 1073741824L) {
			return sub(size, 1048576L) + "MB";
		} else if (size >= 1073741824L) {
			return sub(size, 1073741824L) + "GB";
		} else {
			return size + "";
		}
	}

	/**
	 * 保留两位小数
	 * 
	 * @param size
	 * @param l
	 * @return
	 */
	public static double sub(long size, long l) {
		double d = (double) size / l;
		BigDecimal b = new BigDecimal(d);
		return b.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 格式化字符串编码
	 * 
	 * @param text
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String formateStrEncod(String text) {
		if (isEmptyString(text)) {
			return text;
		}
		try {
			byte[] bytes = text.getBytes("UTF-8");
			ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
			int i = 0;
			int lastI = 0;
			while (i < bytes.length) {
				lastI = i;
				short b = bytes[i];
				if (b > 0) {
					buffer.put(bytes[i++]);
					continue;
				}
				b += 256;
				if ((b ^ 0xC0) >> 4 == 0) {
					buffer.put(bytes, i, 2);
					i += 2;
				} else if ((b ^ 0xE0) >> 4 == 0) {
					buffer.put(bytes, i, 3);
					i += 3;
				} else if ((b ^ 0xF0) >> 4 == 0) {
					i += 4;
				}
				if (i == lastI) {
					return "";
				}
			}
			buffer.flip();
			return new String(buffer.array(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return text;

	}

	public static String formateAliyunKey(String str) {
		if (isEmptyString(str)) {
			return str;
		}
		StringBuilder strb = new StringBuilder(200);
		String[] strs = str.split("/");
		for (String s : strs) {
			s = formateSpecialChar(s);
			strb.append(s.trim() + "/");
		}
		String key = strb.toString();
		if (key.endsWith("/")) {
			key = new String(key.substring(0, key.length() - 1));
		}
		return key;
	}

	public static String formateSpecialChar(String str) {
		if (isEmptyString(str)) {
			return str;
		}
		try {
			byte[] bytes = str.getBytes("utf-8");
			if (bytes == null) {
				return str;
			}
			for (byte b : bytes) {
				if (b == 0) {
					b = 32;
				}
			}
			return new String(bytes, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 转换时间字符串的显示格式
	 * @param str
	 * @param oldpattern
	 * @param newPattern
	 * @return
	 */
	public static String convertDateStrPattern(String str, String oldpattern, String newPattern) {
		if (isEmptyString(str)) {
			return str;
		}
		SimpleDateFormat formate = new SimpleDateFormat(oldpattern);
		try {
			Date date = formate.parse(str);
			formate.applyPattern(newPattern);
			String newStr = formate.format(date);
			return newStr;
		} catch (ParseException e) {
			return str;
		}
	}
	
	/**
	 * 按指定的时间字符串格式转换时间
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static Date getDateFromStr(String str,String pattern){
		Date date = null;
		if (isEmptyString(str)) {
			return date;
		}
		SimpleDateFormat formate = new SimpleDateFormat(pattern);
		try {
			date = formate.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	// /// test
	public static void main(String[] args) throws Exception {
		String str = "Various Artists - Candlelight (Fireplace Session Mix)....MP3";
		String reg = "^(.+)-(.+)[.]+";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		if (m.find()) {
			String seatStr = m.group(1).trim();
			String songname = m.group(2).trim();
			System.out.println(seatStr );
			System.out.println(songname);
		}
	}
}
