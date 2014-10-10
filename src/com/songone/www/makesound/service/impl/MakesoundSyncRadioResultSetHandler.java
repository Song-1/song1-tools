/**
 * 
 */
package com.songone.www.makesound.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.ResultSetHandler;

import com.songone.www.makesound.model.MakesoundSyncRadioModel;


/**
 * 
 * @author Jelly.Liu
 *
 */
public class MakesoundSyncRadioResultSetHandler implements ResultSetHandler<List<MakesoundSyncRadioModel>>{

	@Override
	public List<MakesoundSyncRadioModel> handle(ResultSet rs) throws SQLException {
		List<MakesoundSyncRadioModel> list = new ArrayList<MakesoundSyncRadioModel>();
		if(rs != null){
			while(rs.next()){
				MakesoundSyncRadioModel model = new MakesoundSyncRadioModel();
				model.setBand(rs.getString("band"));
				model.setAudios(rs.getInt("audio_counts"));
				model.setProgramListId(rs.getInt("program_list_id"));
				list.add(model);
			}
		}
		return list;
	}

}
