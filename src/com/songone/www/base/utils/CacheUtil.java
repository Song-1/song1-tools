/**
 * 
 */
package com.songone.www.base.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Jelly.Liu
 *
 */
public class CacheUtil {
	private static final Logger logger = LogManager.getLogger(CacheUtil.class);
	public static Cache getCache(){
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("songOneCache");
		return cache;
	}
	
	public static void addElement(String key ,Object value){
		logger.debug("add key = "+key+" mapping object to cache.................");
		Cache cache = getCache();
		Element element = new Element(key, value);
		cache.put(element);
	}
	
	public static Object getElement(String key){
		logger.debug("get the object from cache.................");
		Cache cache = getCache();
		try{
			Element element = cache.get(key);
			if(element != null){
				return element.getObjectValue();
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
