/**
 * 
 */
package com.tools.song1.runnable.music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.omg.CORBA.BooleanHolder;

import com.tools.song1.util.FileDoUtil;
import com.tools.song1.util.POIUtil;
import com.tools.song1.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class GetMusicTypeFromExcel  extends POIUtil {

	public static  List<MusicTypeDataInfo> bookList = new ArrayList<MusicTypeDataInfo>();
	private String enverionmentTime = null;
	public static void main(String[] args) {
		String path = "E:/歌单分类.xls";
		File file = new File(path);
		new GetMusicTypeFromExcel().doExcel(file);
		for (MusicTypeDataInfo model : bookList) {
			FileDoUtil.outLog(model.toString());
		}
	}
	private int isTheSameRow = -1;
	private MusicTypeDataInfo model = null;
	public void doTheCell(int rowIndex, int cellIndex,Row row) {
		if(isTheSameRow != rowIndex){
			bookList.add(model);
			model = new MusicTypeDataInfo();
			isTheSameRow = rowIndex;
		}
		Cell cell = row.getCell(cellIndex);
		String value = getCellValue(cell);
		if(cellIndex == 0 && value != null){
			enverionmentTime = value;
		}else{
			model.setTime(enverionmentTime);
		}
		if(cellIndex == 1 && value != null ){
			model.setEnverionment(value);
		}
	}
	public int getRowStartIndex(){
		return 0;
	}

	public boolean isMergedRegion(int row, int column, BooleanHolder isFirstflag) {
		return false;
	}

	public static String getTimes(String bookName) {
		if(StringUtil.isEmptyString(bookName)){
			return "";
		}
		String str = "";
		if (bookList != null) {
			for (MusicTypeDataInfo book : bookList) {
				if (book != null && bookName.equals(book.getEnverionment())) {
					str += book.getTime()+";";
				}
			}
		}
		return str;
	}
	@Override
	public void clearValues() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void initValue(Sheet sheet) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setModelValue(int cellIndex, String value) {
		// TODO Auto-generated method stub
		
	}
}
