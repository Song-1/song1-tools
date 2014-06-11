/**
 * 
 */
package com.upload.aliyun.runnable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.upload.aliyun.MusicConstants;
import com.upload.aliyun.util.JavascriptUtil;
import com.upload.aliyun.util.NetWorkUtil;
import com.upload.aliyun.util.OSSUploadUtil;
import com.upload.aliyun.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class BooKListThread implements Runnable {
	private String threadName;
	private String bookName;
	private String bookType;
	private List<File> bookFiles;
	private int count;
	private String  bookSort;
	private String _add_;
	private String imgUrl= "";

	public BooKListThread(String bookName, List<File> bookFiles) {
		if(bookName != null){
			int index = bookName.lastIndexOf(File.separator) + 1;
			this.bookName = new String(bookName.substring(index));
			this.bookType = new String(bookName.substring(0,index-1)).replace(File.separator, "/");
		}
		this.bookFiles = bookFiles;
		count = 0;
		threadName = "BooKListThread_" + bookName;
		_add_ ="";
	}

	@Override
	public void run() {
		saveBook();
		if(saveTheBook()){
			saveBook();
		}
	}

	public void saveBook() {
		if (bookFiles == null) {
			return;
		}
		int sort = 0;
		for (File file : bookFiles) {
			String key = file.getAbsolutePath();
			key = key.replace(MusicConstants.BASE_FILE_PATH + File.separator, "");
			key = key.replace(File.separator, "/");
			if (file.exists()) {
				String fileName = file.getName();
				sort++;
				bookSort = sort+"";
				int index = fileName.indexOf(".");
				if (index > 0) {
					fileName = new String(fileName.substring(0, index));
					bookSort = StringUtil.getIntFormStr(fileName);
				}
				saveData(fileName,key, bookSort, file);
			} else {
				System.out.println(key + "\t此文件已被移动......");
			}
		}
	}

	public void saveData(String name,String key,String sort,File file) {
		key = MusicConstants.SERVER_PATH_ROOT + key;
		System.out.println(key);
		boolean flag = OSSUploadUtil.isObjectExist(MusicConstants.BUKET_NAME, key);
		String url = MusicConstants.getUrl(key);
		System.out.println(url);
		url = StringUtil.encodeURL(url);
		if (!flag) {
			System.out.println(threadName+"\t" + file.getName() + "此文件不存在");
//			try {
//				OSSUploadUtil.uploadBigFile(MusicConstants.BUKET_NAME, key, file);
//			} catch (Exception e) {
//				e.printStackTrace();
//				count++;
//				System.out.println(threadName + " ::: 上传文件发生异常,再次上传............." + count);
//				saveData(name,key,sort,file);
//			}
		}
		if(key.endsWith(".jpg")){
			imgUrl = url;
			return;
		}
		String rus = saveBookFile(name, url, sort);
		String bookId = JavascriptUtil.getSaveBookResponse(rus);
		addBookId(bookId);
	}
	
	private void addBookId(String bookId){
		_add_+= bookId+ ",";
	}
	
	private String saveBookFile(String name,String url,String sort){
		Map<String, String> m = new HashMap<String, String>();
		m.put("name",name);
		m.put("url", url);
		m.put("sort", sort);
		return NetWorkUtil.doPost(MusicConstants.URL_SAVE_DATA_BOOK, m, NetWorkUtil.ENCODE);
	}
	
	private boolean saveTheBook(){
		Map<String, String> m = new HashMap<String, String>();
		m.put("name",bookName);
		m.put("sort", bookType);
		m.put("_add_", _add_);
		m.put("img", imgUrl);
		String res = NetWorkUtil.doPost(MusicConstants.URL_SAVE_DATA_BOOK_LIST, m, NetWorkUtil.ENCODE);
		String bookId = JavascriptUtil.getSaveBookResponse(res);
		if("success".equalsIgnoreCase(bookId)){
			return true;
		}
		return false;
	}

}
