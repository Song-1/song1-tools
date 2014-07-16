/**
 * 
 */
package com.upload.aliyun.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public class NetWorkUtil {

	public static String ENCODE = "UTF-8";
	public static int TIME_OUT = 30000; // 超时时长(毫秒)

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
	}

	/**
	 * UTF-8解码
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeString(String str) {
		try {
			if (str == null) {
				return "";
			}
			str = URLDecoder.decode(str, ENCODE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * post提交表单数据
	 * 
	 * @param reqUrl
	 * @param parameters
	 * @param recvEncoding
	 * @return
	 */
	public static String doPost(String reqUrl, Map<String, String> parameters, String recvEncoding) {
		HttpURLConnection conn = null;
		String responseContent = null;
		OutputStream out = null;
		try {
			StringBuffer params = new StringBuffer();
			for (Map.Entry<String, String> element : parameters.entrySet()) {
				params.append(element.getKey());
				params.append("=");
				String value = element.getValue();
				if(value != null){
					params.append(URLEncoder.encode(element.getValue(), recvEncoding));
				}
				params.append("&");
			}

			if (params.length() > 0) {
				params = params.deleteCharAt(params.length() - 1);
			}
			URL url = new URL(reqUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(TIME_OUT);// （单位：毫秒）jdk
			// 1.5换成这个,连接超时
			conn.setReadTimeout(TIME_OUT);// （单位：毫秒）jdk 1.5换成这个,读操作超时
			conn.setDoOutput(true);
			byte[] b = params.toString().getBytes();
			out = conn.getOutputStream();
			out.write(b, 0, b.length);
			out.flush();
			out.close();
			// ///
			return getPostResponse(conn, recvEncoding);
		} catch (IOException e) {
			e.printStackTrace();
			doPost(reqUrl, parameters, recvEncoding);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					out = null;
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return responseContent;
	}

	/**
	 * 获取post提交表单数据服务器返回的数据
	 * 
	 * @param conn
	 * @param recvEncoding
	 * @return
	 */
	private static String getPostResponse(HttpURLConnection conn, String recvEncoding) {
		String responseContent = null;
		InputStream in = null;
		BufferedReader rd = null;

		try {
			in = conn.getInputStream();
			rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rd != null)
				try {
					rd.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					rd = null;
				}
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					in = null;
				}
		}
		return responseContent;
	}
}
