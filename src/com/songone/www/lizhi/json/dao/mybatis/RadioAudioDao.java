/**
 * 
 */
package com.songone.www.lizhi.json.dao.mybatis;

import java.util.List;

import com.songone.www.base.db.mybatis.PageResultModel;
import com.songone.www.lizhi.json.model.RadioAudio;

/**
 * 
 * @author Jelly.Liu
 *
 */
public interface RadioAudioDao {
	
	public void add(RadioAudio model) throws Exception;
	
	public void update(RadioAudio model) throws Exception;
	
	public RadioAudio queryModelById(long id)throws Exception;
	
	public int queryModelByBandCounts(int band) throws Exception;
	
	public List<RadioAudio> queryModelForSync(PageResultModel<RadioAudio> model) throws Exception;

}
