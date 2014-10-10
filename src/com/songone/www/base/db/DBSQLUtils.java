/**
 * 
 */
package com.songone.www.base.db;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.utils.FileDoUtil;
import com.songone.www.base.utils.StringUtil;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class DBSQLUtils {
	private static final Logger logger = LogManager.getLogger(DBSQLUtils.class);

	private static Properties SQL_PROPERTIES = null;

	//// test
	public static void main(String[] args) {

	}

	/**
	 * 初始化
	 */
	public static void init() {
		loadProperties();
	}

	/**
	 * 通过key值获取value值
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		String value = null;
		if (SQL_PROPERTIES != null && !StringUtil.isEmptyString(key)) {
			value = SQL_PROPERTIES.getProperty(key);
			logger.debug("[ 获取sql语句,key=" + key + " ;sql=" + value + " ] ");
		}
		return value;
	}

	/**
	 * 加载数据库连接配置
	 * 
	 * @return
	 */
	private static void loadProperties() {
		Properties p = new Properties();
		FileInputStream fileInputStream = null;
		File file = FileDoUtil.findFile("db_sql.properties");
		if (file != null && file.exists()) {
			try {
				fileInputStream = new FileInputStream(file);
				p.load(fileInputStream);
				fileInputStream.close();
				SQL_PROPERTIES = p;
			} catch (Exception e) {
				fileInputStream = null;
				logger.error(e.getMessage(), e);
			}
		} else {
			logger.error(" 找不到数据库连接配置文件 ");
		}
	}

}
