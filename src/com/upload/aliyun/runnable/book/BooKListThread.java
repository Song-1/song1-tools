/**
 * 
 */
package com.upload.aliyun.runnable.book;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.upload.aliyun.MusicConstants;
import com.upload.aliyun.StartMain;
import com.upload.aliyun.runnable.music.GetMusicTypeFromExcel;
import com.upload.aliyun.util.FileDoUtil;
import com.upload.aliyun.util.ImageFileUtil;
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
	private String cover;
	private List<File> bookFiles;
	/**
	 * 排序
	 */
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
		threadName = "BooKListThread_" + bookName;
		_add_ ="";
	}

	@Override
	public void run() {
		saveBook();
		if(saveTheBook()){
//			saveBook();
		}
	}

	public void saveBook() {
		if (bookFiles == null) {
			return;
		}
		int sort = 0;
		for (File file : bookFiles) {
			String key = file.getAbsolutePath();
			String basePath = "";
			if(MusicConstants.BASE_FILE_PATH.endsWith("/")){
				basePath = MusicConstants.BASE_FILE_PATH;
				basePath = new String(basePath.substring(0,basePath.length() - 1)) + File.separator;
			}else if(MusicConstants.BASE_FILE_PATH.endsWith(File.separator)){
				basePath = MusicConstants.BASE_FILE_PATH;
			}else{
				basePath = MusicConstants.BASE_FILE_PATH + File.separator;
			}
			key = key.replace(basePath, "");
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
				FileDoUtil.outLog(key + "\t此文件已被移动......");
			}
		}
	}

	public void saveData(String name,String key,String sort,File file) {
//		FileDoUtil.outLog("需要保存的是："+key);
		if (!key.startsWith(MusicConstants.SERVER_PATH_ROOT)) {
			key = MusicConstants.SERVER_PATH_ROOT + key;
		}
		FileDoUtil.outLog("需要保存的是（具体路径）："+key);
		boolean flag = OSSUploadUtil.isObjectExist(MusicConstants.BUKET_NAME, key);
		if (!flag) {
			String key_temp = key.substring(0, key.lastIndexOf("/"));
			String fileName = key.substring(key.lastIndexOf("/") + 1);
			String key_1 = key_temp + key_temp.substring(key_temp.lastIndexOf("/"))  + "（高品质）/" + fileName;
			flag = OSSUploadUtil.isObjectExist(MusicConstants.BUKET_NAME, key_1);
		}
		
		if (!flag) {
			String key_temp = key.substring(0, key.lastIndexOf("/"));
			String fileName = key.substring(key.lastIndexOf("/") + 1);
			String key_1 = key_temp + key_temp.substring(key_temp.lastIndexOf("/"))  + "(高品质)/" + fileName;
			flag = OSSUploadUtil.isObjectExist(MusicConstants.BUKET_NAME, key_1);
		}
//		String suffix = key.substring(key.lastIndexOf(".")+1);
		String url = MusicConstants.getUrl(key);
		FileDoUtil.outLog(url);
		if (!flag) {
			FileDoUtil.outLog(threadName+"\t\n" + key + "此文件不存在");
		}
		//url = StringUtil.encodeURL(url);
		if(ImageFileUtil.isImageFile(key)){
			imgUrl = url;
			if (!flag) {
				try {
					FileDoUtil.outLog("上传文件中。。。:"+file.getAbsolutePath());
					OSSUploadUtil.uploadObject(MusicConstants.BUKET_NAME, key, file);
					FileDoUtil.outLog("上传完毕:"+file.getAbsolutePath());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return;
		}
		if (isNumber(name)) {
			String[] split = url.split("/");
			name = split[3] + "-" + name;
		}
		String rus = saveBookFile(name, url, sort);
		String bookId = JavascriptUtil.getSaveBookResponse(rus);
		FileDoUtil.outLog("书集上传成功:"+rus + ":"+bookName+"\t\n");
		addBookId(bookId);
	}
	
	 public boolean isNumber(String str){
	        java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("^[0-9]+$");
	        java.util.regex.Matcher match=pattern.matcher(str);
	        if(match.matches()==false){
	             return false;
	        }else{
	             return true;
	        }
	    }
	private void addBookId(String bookId){
		_add_+= bookId+ ",";
	}
	
	private String saveBookFile(String name,String url,String sort){
		Map<String, String> m = new HashMap<String, String>();
		m.put("name",name);
		m.put("url", url);
		m.put("seat", sort);
		return NetWorkUtil.doPost(MusicConstants.URL_SAVE_DATA_BOOK, m, NetWorkUtil.ENCODE);
	}
	
	private boolean saveTheBook(){
		Map<String, String> m = new HashMap<String, String>();
		BookDataInfo book = BookDataGetFromExcel.getObjFormList(bookName);
		m.put("cover", cover);
		m.put("name",bookName);
		m.put("sort", bookType);
		m.put("_add_", _add_);
		m.put("img", imgUrl);
		m.put("desc", book.getDesc());
		String author = book.getAuthor();
		String player = book.getPlayer();
		m.put("author", author);
		m.put("player", player);
		String res = NetWorkUtil.doPost(MusicConstants.URL_SAVE_DATA_BOOK_LIST, m, NetWorkUtil.ENCODE);
		String bookId = JavascriptUtil.getSaveBookResponse(res);
		if (StringUtil.isEmptyString(author) || StringUtil.isEmptyString(player)) {
			FileDoUtil.debugLog("书单作者或者演播为空:" + bookId  + ":"+book.toString()+"\t\n");
		}
		if("success".equalsIgnoreCase(bookId)){
			return true;
		}
		FileDoUtil.debugLog("书单上传失败:"+bookId + ":"+book.toString()+"\t\n");
		return false;
	}
	
	public static void main(String[] args) {
		try {
			// // 初始化操作
			// /// 加载配置文件
			MusicConstants.loadConfig();
			OSSUploadUtil.init();
//			String key = "test/樱桃时光/熬夜/思念的/回忆里的青春/song.mp3";
//			String url = OSSUploadUtil.generateAliyunURL(MusicConstants.BUKET_NAME, key, 10000L);
//			FileDoUtil.outLog(url);
			JavascriptUtil.init();
			
			//
			if("music".equals(MusicConstants.DO_TYPE)){
				File file = new File(MusicConstants.MUSIC_TIME_TYPE_MAPPING_FILE_PATH);
				if(file.exists()){
					String name = file.getName();
					if(name.endsWith(".xls") ||name.endsWith(".xlsx")){
						new GetMusicTypeFromExcel().doExcel(file);
					}
				}
			}
			///
			StartMain.eachFiles();
			StartMain.doFile();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

}
