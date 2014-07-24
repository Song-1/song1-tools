/**
 * 
 */
package com.upload.aliyun.runnable.book;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tools.song1.aliyun.OSSUploadUtil;
import com.tools.song1.data.api.BaseResultBean;
import com.tools.song1.data.api.PageDataModel;
import com.upload.aliyun.util.FileDoUtil;
import com.upload.aliyun.util.HttpClientUtil;
import com.upload.aliyun.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class BookUtil {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private static String ALIYUNURLBASE = "oss-cn-hangzhou.aliyuncs.com/";

	public static void main(String[] args) throws Exception {
		String baseUrl = "http://localhost:8080/song1/api/book/v1/list";
		String bucket = "testupload2";
		start(bucket, baseUrl);
		
	}

	public static void start(String bucket, String baseUrl) {
		if(StringUtil.isEmptyString(bucket)){
			System.out.println("阿里云BUCKET不能为空!");
		}else if(StringUtil.isEmptyString(baseUrl)){
			System.out.println("服务器获取书籍列表URL不能为空!");
		}
		try {
			int j = baseUrl.lastIndexOf("?");
			if(j > 0){
				baseUrl = new String(baseUrl.substring(0,j));
			}
			int strat = 0;
			int pageSize = 100;
			int counts = 0;
			do {
				String url = baseUrl + "?start=" + strat + "&limit=" + pageSize;
				String content = HttpClientUtil.doGet(url);
				PageDataModel<BookResponseDataModel> pageData = doData(content);
				if (pageData != null) {
					strat += pageSize;
					counts = pageData.getRecordCount();
					List<BookResponseDataModel> books = pageData.getListPageObject();
					if (books != null) {
						for (BookResponseDataModel bookResponseDataModel : books) {
							if (bookResponseDataModel != null) {
								String key = bookResponseDataModel.getUrl();
								key = StringUtil.decodeURL(key);
								int i = key.indexOf(ALIYUNURLBASE);
								if (i > 0) {
									key = new String(key.substring(i + ALIYUNURLBASE.length()));
								}
								i = key.lastIndexOf("?");
								if (i > 0) {
									key = new String(key.substring(0, i));
								}
								boolean flag = OSSUploadUtil.isObjectExist(bucket, key);
								if (!flag) {
									FileDoUtil.outLog(key + " [ 阿里云不存在此文件 ]");
								}
								System.out.println(key + " [ 阿里云存在此文件 ]");
							}
						}
					}
				}
			} while (counts > strat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean updateBookDataURLStuffixForDB(String baseURL){
		if(StringUtil.isEmptyString(baseURL)){
			System.out.println("服务器更新数据书籍URL的API地址不能为空!");
			return false;
		}
		String stuffix = ".mp3";
		try{
			String url = baseURL + "?stuffix=" + stuffix;
			String content = HttpClientUtil.doGet(url);
			Gson gson = new Gson();
			Type type = new TypeToken<BaseResultBean<String>>(){}.getType();
			BaseResultBean<String> bean = gson.fromJson(content,type);
			if (bean != null) {
				if("1000".equals(bean.getStatus())){
					if ("success".equals(bean.getData())) {
						return true;
					}else{
						System.out.println(bean.getData());
						return false;
					}
				}else{
					System.out.println(bean.getMessage());
					return false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
		
		
		
	}

	public static PageDataModel<BookResponseDataModel> doData(String content) {
		if (!StringUtil.isEmptyString(content)) {
			Gson gson = new Gson();
			Type type = new TypeToken<BaseResultBean<PageDataModel<BookResponseDataModel>>>(){}.getType();
			BaseResultBean<PageDataModel<BookResponseDataModel>> bean  = gson.fromJson(content,type);
			if (bean != null) {
				if("1000".equals(bean.getStatus())){
					PageDataModel<BookResponseDataModel> pageDate = bean.getData();
					if (pageDate != null) {
						return pageDate;
					}
				}else{
					System.out.println(bean.getMessage());
				}
			}
		}
		return null;
	}

}
