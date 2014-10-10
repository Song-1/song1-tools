/**
 * 
 */
package com.songone.www.base.db.mybatis;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Jelly.Liu
 *
 */
public interface BaseMybatisDao<T> {
	/**
	 * 新增操作记录数据对象
	 * 
	 * @param model
	 * @throws Exception
	 */
	public void add(T model) throws Exception;

	/**
	 * 更新操作记录数据对象
	 * 
	 * @param model
	 * @throws Exception
	 */
	public void update(T model) throws Exception;

	/**
	 * 获取model
	 * 
	 * @param band
	 * @return
	 * @throws Exception
	 */
	public T queryModel(Map<String, Object> map) throws Exception;

	/**
	 * 查询model集合,可分叶
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<T> queryModelByPage(Map<String, Object> map) throws Exception;

}
