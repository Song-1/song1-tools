/**
 * 
 */
package com.songone.www.lizhi.json.dao.mybatis;

import java.util.List;

import com.songone.www.base.db.mybatis.PageResultModel;
import com.songone.www.lizhi.json.model.Radio;

/**
 * 
 * @author Jelly.Liu
 *
 */
public interface RadioDao {
	
	public void add(Radio radio)throws Exception;
	
	public Radio queryModelById(long id) throws Exception;
	
	public Integer queryCurrentMaxBand() throws Exception;
	
	public int queryModelCounts() throws Exception;
	
	public List<Radio> queryModelByPage(PageResultModel<Radio> model) throws Exception;
	
	public int queryModelByPageForAudioCounts() throws Exception;
	
	public List<Radio> queryModelByPageForAudio(PageResultModel<Radio> model) throws Exception;
	
	public int queryModelByPageForRadioAudioCounts() throws Exception;
	
	
	public List<Radio> queryModelByPageForRadioAudio(PageResultModel<Radio> model) throws Exception;
	
	public void updateGetSaveAudioFlag(Radio radio)throws Exception;
	
	public void updateFlagAndAudios(Radio radio)throws Exception;
	
	/**
	 * 获取没有同步到音乐一号服务器的电台数据的总数
	 * @return
	 * @throws Exception
	 */
	public int queryModelByPageForSyncCounts(boolean syncFlag) throws Exception;
	
	/**
	 * 获取没有同步到音乐一号服务器的电台数据(分页)
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List<Radio> queryModelByPageForSync(PageResultModel<Radio> model) throws Exception;
	
	/**
	 * 获取已经同步到音乐一号服务器的电台数据(分页)
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List<Radio> queryModelByPageForSyncAudio(PageResultModel<Radio> model) throws Exception;
	
	public List<Radio> queryModelIDByPage(PageResultModel<Radio> model) throws Exception;
	
	/**
	 * 查询电台数据进行同步操作
	 * @param listCounts 
	 * @return
	 * @throws Exception
	 */
	public List<Radio> queryModelForSync(int listCounts) throws Exception;
	
	
	
}
