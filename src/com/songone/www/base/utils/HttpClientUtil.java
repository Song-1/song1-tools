/**
 * 
 */
package com.songone.www.base.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * http 连接的辅助类
 * 
 * @author Administrator
 */
public class HttpClientUtil {
	private static final Logger logger = LogManager.getLogger(HttpClientUtil.class);

	public static String doGet(String url) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		// 创建Get方法实例
		HttpGet httpgets = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpgets);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instreams = entity.getContent();
			String str = convertStreamToString(instreams);
			return str;
		}
		return null;
	}

	public static String doPost(String url, Map<String, String> map) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		// 创建POST方法实例
		HttpPost post = new HttpPost(url);
		if (map != null) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Set<String> keySet = map.keySet();
			for (String key : keySet) {
				nvps.add(new BasicNameValuePair(key, map.get(key)));
			}
			post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		}
		HttpResponse response = httpclient.execute(post);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instreams = entity.getContent();
			String str = convertStreamToString(instreams);
			return str;
		}
		return null;
	}

	public static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 根据URL下载文件到本地
	 * 
	 * @param url
	 * @param filePath
	 * @return
	 */
	public static String saveFileByURL(String url, String filePath) {
		return download(url, filePath);
	}

	/**
	 * 根据URL下载文件到本地
	 * 
	 * @param url
	 * @param filePath
	 * @return
	 */
	public static String download(String url, String filePath) {
		if (StringUtil.isEmptyString(url)) {
			return null;
		}
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			logger.debug("开始下载文件:::" + url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			StatusLine statusLine = httpResponse.getStatusLine();
			if (statusLine.getStatusCode() != 200) {
				return null;
			}
			String path = filePath;
			if (StringUtil.isEmptyString(path)) {
				path = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
			}
			path += new String(url.substring(url.lastIndexOf("/") + 1));
			File file = new File(path);
			FileDoUtil.mkDirs(file);
			logger.debug("文件下载到:::" + path);
			FileOutputStream outputStream = new FileOutputStream(file);
			InputStream inputStream = httpResponse.getEntity().getContent();
			byte b[] = new byte[1024];
			int j = 0;
			while ((j = inputStream.read(b)) != -1) {
				outputStream.write(b, 0, j);
			}
			outputStream.flush();
			outputStream.close();
			logger.debug("文件下载完成:::" + url);
			return path;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return null;
	}

}
