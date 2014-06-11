/**
 * 
 */
package com.upload.aliyun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.aliyun.openservices.ClientConfiguration;
import com.aliyun.openservices.oss.OSSClient;

/**
 * @author Administrator
 *
 */
public class NetWorkUtil_Bak {

	private static final String ENCODE = "UTF-8";
	private static ScriptEngineManager manager = new ScriptEngineManager();
	private static ScriptEngine engine = manager.getEngineByName("javascript");
	private static String listUrl = "http://localhost:8080/song1/api/book/v1/list";
	private static int limitSize = 100;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String url = "http://localhost:8080/song1/api/book/v1/list?limit=100&start=100";
		String content = getHTML(url, ENCODE);
		System.out.println(content);
		// String content = "{'message':'操作成功'}";
		doJsonByJs(content);
	}

	public static String getHTML(String tempurl, String code) throws IOException {
		URL url = null;
		BufferedReader breader = null;
		InputStream is = null;
		StringBuffer resultBuffer = new StringBuffer();
		try {
			url = new URL(tempurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			is = conn.getInputStream();
			breader = new BufferedReader(new InputStreamReader(is, code));
			String line = "";
			while ((line = breader.readLine()) != null) {
				resultBuffer.append(line);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} finally {
			breader.close();
			is.close();
		}
		return resultBuffer.toString();
	}

	public static void doJsonByJs(String jsonDataStr) throws Exception {
		File file = findFile();
		if (file != null) {
			FileReader reader = new FileReader(file); // 执行指定脚本
			engine.eval(reader);
			if (engine instanceof Invocable) {
				Invocable invoke = (Invocable) engine;
				int listCount = (Integer) invoke.invokeFunction("doTheJsonGetCount", getListCount());
				int pageSize = listCount / limitSize;
				if (pageSize > 0) {
					ClientConfiguration config = new ClientConfiguration();
					OSSClient client = new OSSClient(MusicConstants.PROTOCOL + MusicConstants.ALIYUN_IMAGE_HOST, MusicConstants.ALIYUN_ACCESSKEYID, MusicConstants.ALIYUN_ACCESSKEYSECRET, config);
					for (int i = 0; i <= pageSize; i++) {
						int start = limitSize * i;
						String content = getListData(limitSize, start);
						String[] strs = (String[]) invoke.invokeFunction("doTheJson", content);
						getDBFileURL(client, strs);
					}
				}
				System.out.println(MusicConstants.UPLOAD_FILE_LIST.size());
			}

		}
	}

	public static String getListCount() throws Exception {
		int limit = 1;
		int start = 0;
		return getListData(limit, start);
	}

	public static String getListData(int limit, int start) throws Exception {
		String url = listUrl + "?limit=" + limit + "&start=" + start;
		String content = getHTML(url, ENCODE);
		return content;
	}

	private static File findFile() {
		String path = MusicConstants.class.getResource("/").toString();
		path = path.replace("file:/", "");
		path += "config/doJson.js";
		System.out.println(path);
		// /
		File file = new File(path);
		if (file.exists()) {
			return file;
		} else {
			return null;
		}
	}

	public static void getDBFileURL(OSSClient client, String[] strs) {
		if (strs == null) {
			return;
		}
		for (String s : strs) {

			if (s == null || "".equals(s.trim())) {
				continue;
			}
			s = encodeString(s);
			String buket = new String(s.substring(s.indexOf(MusicConstants.PROTOCOL) + MusicConstants.PROTOCOL.length(), s.indexOf(".")));

			int firstIndex = s.indexOf(MusicConstants.ALIYUN_IMAGE_HOST) + MusicConstants.ALIYUN_IMAGE_HOST.length() + 1;
			String key = new String(s.substring(firstIndex));

			int index = key.lastIndexOf("/") + 1;
			String fileName = key.substring(index);

//			boolean flag = OSSMultipartSample.isObjectExist(client, buket, key);
//			if (!flag) {
//				// FileUploadInfoModel model = new FileUploadInfoModel();
//				// model.setBuket(buket);
//				// model.setKey(key);
//				// model.setName(fileName);
//				//
//				// MusicConstants.UPLOAD_FILE_LIST.add(model);
//				System.out.println(key);
//			}

		}

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
			String theDefenc = "UTF-8";
			str = URLDecoder.decode(str, theDefenc);
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
				params.append(element.getKey().toString());
				params.append("=");
				params.append(URLEncoder.encode(element.getValue().toString(), recvEncoding));
				params.append("&");
			}

			if (params.length() > 0) {
				params = params.deleteCharAt(params.length() - 1);
			}
			URL url = new URL(reqUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(10000);// （单位：毫秒）jdk
			// 1.5换成这个,连接超时
			conn.setReadTimeout(10000);// （单位：毫秒）jdk 1.5换成这个,读操作超时
			conn.setDoOutput(true);
			byte[] b = params.toString().getBytes();
			out = conn.getOutputStream();
			out.write(b, 0, b.length);
			out.flush();
			out.close();
			///// 
			return getPostResponse(conn, recvEncoding);
		} catch (IOException e) {
			e.printStackTrace();
			doPost(reqUrl, parameters, recvEncoding);
		} finally {
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
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
	public static String getPostResponse(HttpURLConnection conn, String recvEncoding) {
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
