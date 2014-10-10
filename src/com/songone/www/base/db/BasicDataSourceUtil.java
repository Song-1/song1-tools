package com.songone.www.base.db;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.utils.FileDoUtil;

/**
 * 数据源辅助类
 * 
 * @author Jelly.Liu
 *
 */
public class BasicDataSourceUtil {
	private static final Logger logger = LogManager.getLogger(BasicDataSourceUtil.class);
	/**
	 * 数据源
	 */
	public static DataSource dataSource = null;

	/**
	 * 初始化,建立数据源
	 */
	public static void init() {
		setupDataSource();
	}

	/**
	 * 销毁数据源
	 */
	public static void destroy() {
		shutdownDataSource();
	}

	/**
	 * 创建数据源
	 */
	private static void setupDataSource() {
		Properties p = loadProperties();
		if (p != null) {
			try {
				dataSource = BasicDataSourceFactory.createDataSource(p);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 关闭数据源
	 */
	private static void shutdownDataSource() {
		if (dataSource == null) {
			return;
		}
		BasicDataSource bd = (BasicDataSource) dataSource;
		try {
			bd.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			bd = null;
			dataSource = null;
		}
	}

	/**
	 * 加载数据库连接配置
	 * 
	 * @return
	 */
	private static Properties loadProperties() {
		Properties p = new Properties();
		FileInputStream fileInputStream = null;
		File file = FileDoUtil.findFile("db.properties");
		if (file != null && file.exists()) {
			try {
				fileInputStream = new FileInputStream(file);
				p.load(fileInputStream);
				fileInputStream.close();
				return p;
			} catch (Exception e) {
				fileInputStream = null;
				logger.error(e.getMessage(), e);
			}
		} else {
			logger.error(" 找不到数据库连接配置文件 ");
		}
		return null;
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		Connection conn = null;
		if (dataSource != null) {
			conn = dataSource.getConnection();
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param conn
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				conn = null;
			}
		}
	}
}