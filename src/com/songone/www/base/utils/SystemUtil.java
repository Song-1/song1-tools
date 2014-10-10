/**
 * 
 */
package com.songone.www.base.utils;

import com.songone.www.base.db.mybatis.MybatisUtil;
import com.songone.www.lizhi.json.constans.ThreadConstans;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class SystemUtil {

	public static void main(String[] args) {
	}
	
	/** 
	 * 系统初始化执行的操作
	 */
	public static void init(){
		//// 初始化数据源
		//BasicDataSourceUtil.init(); 
		//// 初始化sql语句配置
		//DBSQLUtils.init();
		//// 初始化,获取sqlSessionFactory实例
		BaseConstants.init();
		MybatisUtil.init();
		ThreadConstans.init();
	}
	
	/**
	 * 系统退出执行的操作
	 * @param status
	 */
	public static void exit(int status){
		//// 销毁数据源
		//BasicDataSourceUtil.destroy();
		ThreadConstans.closeThreadPool();
		System.exit(status);
	}

}
