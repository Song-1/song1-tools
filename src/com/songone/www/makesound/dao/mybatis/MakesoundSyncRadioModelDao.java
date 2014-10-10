/**
 * 
 */
package com.songone.www.makesound.dao.mybatis;

import java.util.List;
import java.util.Map;

import com.songone.www.makesound.model.MakesoundSyncRadioModel;

/**
 * 同步荔枝FM电台数据到音乐一号造音社节目单数据操作记录接口
 * 
 * @author Jelly.Liu
 *
 */
public interface MakesoundSyncRadioModelDao {
	/**
	 * 新增操作记录数据对象
	 * @param model
	 * @throws Exception
	 */
	public void add(MakesoundSyncRadioModel model) throws Exception;
	
	/**
	 * 更新操作记录数据对象
	 * @param model
	 * @throws Exception
	 */
	public void update(MakesoundSyncRadioModel model) throws Exception;

	/**
	 * 根据band值查询
	 * @param band
	 * @return
	 * @throws Exception
	 */
	public MakesoundSyncRadioModel selectByBand(String band) throws Exception;
	
	public List<MakesoundSyncRadioModel> selectForSyncAudio(Map<String,Integer> map) throws Exception; 
}
