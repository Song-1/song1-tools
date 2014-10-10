/**
 * 
 */
package com.songone.www.base.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import com.songone.www.base.excecptions.DBUtilException;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class DBUtil {

	/**
	 * 保存数据
	 * 
	 * @param sql
	 * @param rsh
	 * @return
	 * @throws DBUtilException
	 */
	public static void save(String sql, Object... params) throws DBUtilException {
		QueryRunner runner = new QueryRunner();
		Connection conn = null;
		try {
			conn = BasicDataSourceUtil.getConnection();
			runner.update(conn, sql, params);
			DbUtils.close(conn);
		} catch (SQLException e) {
			DBUtilException dbe = new DBUtilException(e);
			dbe.setErrorMsg("数据库插入数据异常");
			throw dbe;
		} finally {
			conn = null;
		}
	}

	/**
	 * 查询数据
	 * 
	 * @param sql
	 * @param rsh
	 * @param params
	 * @return
	 * @throws DBUtilException
	 */
	public static List<?> query(String sql, ResultSetHandler<?> rsh, Object... params) throws DBUtilException {
		List<?> result = null;
		Connection conn = null;
		QueryRunner runner = new QueryRunner();
		try {
			conn = BasicDataSourceUtil.getConnection();
			result = (List<?>) runner.query(conn, sql, rsh, params);
			DbUtils.close(conn);
		} catch (SQLException e) {
			DBUtilException dbe = new DBUtilException(e);
			dbe.setErrorMsg("数据库查询数据异常");
			throw dbe;
		} finally {
			conn = null;
		}
		return result;
	}

	/**
	 * 分页查询
	 * 
	 * @param sql
	 * @param rsh
	 * @param start
	 * @param pageSize
	 * @param params
	 * @return
	 * @throws DBUtilException
	 */
	public static List<?> queryBypage(String sql, ResultSetHandler<?> rsh, int start, int pageSize, Object... params) throws DBUtilException {
		List<?> result = null;
		Connection conn = null;
		QueryRunner runner = new QueryRunner();
		try {
			sql += "limit " + start + "," + (start + pageSize);
			conn = BasicDataSourceUtil.getConnection();
			result = (List<?>) runner.query(conn, sql, rsh, params);
			DbUtils.close(conn);
		} catch (SQLException e) {
			DBUtilException dbe = new DBUtilException(e);
			dbe.setErrorMsg("数据库查询数据异常");
			throw dbe;
		} finally {
			conn = null;
		}
		return result;
	}

}
