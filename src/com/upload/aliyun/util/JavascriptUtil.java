/**
 * 
 */
package com.upload.aliyun.util;

import java.io.File;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.upload.aliyun.MusicConstants;

/**
 * @author Administrator
 *
 */
public class JavascriptUtil {
	private static ScriptEngine engine = null;
	private static Invocable invoke = null;

	public static void main(String[] args) {

	}

	public static void init() {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			engine = manager.getEngineByName("javascript");
			File file = FileDoUtil.findFile(MusicConstants.PATH_CONFIG_DOJSON_JS);
			if (file != null) {
				FileReader reader = new FileReader(file); // 执行指定脚本
				engine.eval(reader);
				if (engine instanceof Invocable) {
					invoke = (Invocable) engine;
				}
			}
		} catch (Exception e) {
			System.out.println("调用Javascript脚本初始化发生异常..........");
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取保存书籍时返回的json数据里面的bookId
	 * @param returnJsonStr
	 * @return
	 */
	public static String getSaveBookResponse(String returnJsonStr){
		String bookid = null;
		try {
			bookid = (String) invoke.invokeFunction("getDoSaveResponseId", returnJsonStr);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return bookid;
	}

}
