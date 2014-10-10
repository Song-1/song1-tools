/**
 * 
 */
package com.songone.www.base.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.songone.www.base.db.mybatis.MybatisUtil;
import com.songone.www.base.excecptions.SqlSessionNullPointException;
import com.songone.www.base.excecptions.ValidateExecuteMethodException;
import com.songone.www.base.utils.StringUtil;

/**
 * 
 * @author Jelly.Liu
 *
 */
public abstract class BaseService<T> {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(BaseService.class);
	private Map<String, Method> daoMethodMap = new HashMap<String, Method>();

	/**
	 * init
	 */
	public BaseService() {
		eachDaoMethods();
	}

	/**
	 * 设置MYBATIS DAO CLASS
	 * 
	 * @return
	 */
	public abstract <E> Class<E> getRealDaoClass();
	
	/**
	 * 获取具体的DAO
	 * @param session
	 * @return
	 */
	public abstract <E> E getDao(SqlSession session);

	/**
	 * 获取MYBATIS DAO具体实现对象
	 * 
	 * @param session
	 * @return
	 */
	public <E> E getDaoBySession(SqlSession session) {
		if (session != null) {
			Class<E> classes = getRealDaoClass();
			return session.getMapper(classes);
		}
		return null;
	}

	/**
	 * 获取session
	 * 
	 * @return
	 * @throws SqlSessionNullPointException
	 */
	public SqlSession getSqlSession() throws SqlSessionNullPointException {
		SqlSession session = MybatisUtil.getSqlSession();
		if (session == null) {
			throw new SqlSessionNullPointException("[SQLSESSION_NULL]get the SqlSession from mybatis SqlSessionFactory is Null");
		}
		return session;
	}

	/**
	 * 遍历指定DAO class 的public method
	 */
	private void eachDaoMethods() {
		Class<?> classes = getRealDaoClass();
		if (classes == null) {
			return;
		}
		Method[] methods = classes.getMethods();
		if (methods != null) {
			for (Method method : methods) {
				daoMethodMap.put(method.getName(), method);
			}
		}
	}

	/**
	 * 校验指定的方法名称是否存在方法,并且检验方法参数是否匹配
	 * @param methodName
	 * @param params
	 * @return true
	 * @throws ValidateExecuteMethodException
	 */
	private boolean validateExecuteMethod(String methodName, Object... params) throws ValidateExecuteMethodException {
		if (StringUtil.isEmptyString(methodName)) {
			throw new ValidateExecuteMethodException("[METHODNAME_EMPTY] input method name is empty");
		}
		Method method = daoMethodMap.get(methodName);
		if (method == null) {
			throw new ValidateExecuteMethodException("[METHOD_NULL] the Dao Object not has the method that name:" + methodName);
		}
		int paramsCount = params == null ? 0 : params.length;
		if (paramsCount != method.getParameterCount()) {
			throw new ValidateExecuteMethodException("[METHOD_PARAM_NOT_EQUAL]input param counts not equal method param counts");
		}
		if (params != null && paramsCount > 0) {
			Class<?>[] paramTypes = method.getParameterTypes();
			int i = 0;
			if (paramTypes != null) {
				for (Class<?> type : paramTypes) {
					Object obj = params[i++];
					boolean testFlag = false;
					if (obj == null) {
						continue;
					}else if(obj instanceof Integer){
						testFlag = type.equals(int.class);
					}else if(obj instanceof Long){
						testFlag = type.equals(long.class);
					}else{
						testFlag = type.equals(obj.getClass());
					}
					if (!testFlag) {
						throw new ValidateExecuteMethodException("[METHOD_PARAM_NOT_EQUAL]input param class type not equal method param class type");
					}
				}
			}
		}
		return true;
	}

	/**
	 * 按指定的方法名称执行DAO 方法
	 * @param methodName
	 * @param params
	 * @return
	 * @throws ValidateExecuteMethodException
	 * @throws SqlSessionNullPointException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public Object executeDao(String methodName, Object... params) throws ValidateExecuteMethodException, SqlSessionNullPointException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object result = null;
		boolean validateFlag = validateExecuteMethod(methodName, params);
		if (!validateFlag) {
			return result;
		}
		SqlSession session = null;
		try {
			session = getSqlSession();
			Object dao = getDaoBySession(session);
			Method method = daoMethodMap.get(methodName);
			result = method.invoke(dao, params);
		} finally {
			if (session != null)
				session.close();
		}
		return result;
	}

}
