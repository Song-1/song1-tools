/**
 * 
 */
package com.songone.www.base.action;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.songone.www.aliyun.service.AliyunMappingFileModelService;
import com.songone.www.base.utils.ImageDoUtil;
import com.songone.www.base.utils.StringUtil;

/**
 * 
 * @author Jelly.Liu
 *
 */
public abstract class BaseFileEachAction {

	public String basePath = "";
	public String createDate = "";
	public String createUser = "";
	protected AliyunMappingFileModelService aliyunMappingFileModelService = new AliyunMappingFileModelService();
	/**
	 * 设置基本路径
	 * 
	 * @param basePath
	 */
	public void setBasePath(String basePath) {
		if (StringUtil.isEmptyString(basePath)) {
			return;
		} else if (!basePath.endsWith(File.separator)) {
			basePath += File.separator;
		}
		this.basePath = basePath;
	}

	/**
	 * 解析创建时间和创建人
	 * 
	 * @param name
	 */
	public void setCreateInfo(String name) {
		if (StringUtil.isEmptyString(name)) {
			return;
		}
		String[] strs = name.split("-");
		if (strs != null && strs.length == 2) {
			this.createDate = strs[0];
			this.createUser = strs[1];
		}
	}

	/**
	 * 获取阿里云的key值
	 * 
	 * @param path
	 * @return
	 */
	public String getAliyunKey(String path,String aliyunBaseKey) {
		if (StringUtil.isEmptyString(path)) {
			return path;
		}
		String key = path.replace(basePath, "");
		String separator = File.separator;
		if (key.startsWith(separator)) {
			key = new String(key.substring(separator.length()));
		} else if (key.startsWith("/")) {
			key = new String(key.substring(1));
		}
		key = key.replace(separator, "/");
		key = aliyunBaseKey + key;
		return key;
	}
	
	public abstract String getAliyunKey(String path);
	
	
	/**
	 * 校验文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean valiDateFile(File file) {
		if (file == null) {
			return false;
		} else if (!file.exists()) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 判断是否歌手或专辑的图片
	 * @param file
	 * @return
	 */
	public static boolean isImage(File file){
		boolean flag = false;
		if(!valiDateFile(file)){
			return flag;
		}
		String name = file.getName();
		String stffix = new String(name.substring(name.lastIndexOf(".")));
		String parentName = file.getParentFile().getName() + stffix;
		if (name.equals(parentName)) {
			flag = true;
		}
		return flag;
	}
	/**
	 * 判断是否歌手或专辑的图片
	 * @param file
	 * @return
	 */
	public static boolean isDescText(File file){
		boolean flag = false;
		if(!valiDateFile(file)){
			return flag;
		}
		String name = file.getName();
		String stffix = new String(name.substring(name.lastIndexOf(".")));
		String parentName = file.getParentFile().getName() + stffix;
		if (name.equals(parentName)) {
			flag = true;
		}
		return flag;
	}
	
	public String getDesc(File f){
		String result = null;
		if(!valiDateFile(f)){
			return result;
		}
		try {
			result= FileUtils.readFileToString(f, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 判断是否歌手或专辑的小图
	 * @param file
	 * @return
	 */
	public static boolean isIcon(File file){
		boolean flag = false;
		if(!valiDateFile(file)){
			return flag;
		}
		String name = file.getName();
		String stffix = new String(name.substring(name.lastIndexOf(".")));
		String iconName = file.getParentFile().getName() + "(300x300)" + stffix;
		if (name.equals(iconName)) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 根据大图生成小图
	 * @param file
	 * @param foreignKeyId
	 * @param dataType
	 * @return
	 */
	public String createIcon(File file,int foreignKeyId,int dataType) {
		String path = file.getAbsolutePath();
		if (!ImageDoUtil.isImageFile(path)) {
			return "";
		}else if(!isImage(file)){
			return "";
		}
		String name = file.getName();
		String stffix = new String(name.substring(name.lastIndexOf(".")));
		String iconName = file.getParentFile().getName() + "(300x300)" + stffix;
		path = path.replace(name, iconName);
		File f = new File(path);
		if (f.exists()) {
			return getAliyunKey(path);
		}
		String iconPath = ImageDoUtil.cutImage(file);
		if(StringUtil.isEmptyString(iconPath)){
			return "";
		}
		String key = getAliyunKey(iconPath);
		aliyunMappingFileModelService.saveByFile(path, key, dataType, foreignKeyId);
		return key;
	}

}
