/**
 * 
 */
package com.upload.aliyun.runnable.music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.omg.CORBA.BooleanHolder;

import com.upload.aliyun.MusicConstants;
import com.upload.aliyun.util.FileDoUtil;
import com.upload.aliyun.util.POIUtil;
import com.upload.aliyun.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class MusicDataGetFromExcel extends POIUtil {

	private  CopyOnWriteArrayList<CellRangeAddress> CellRangeAddressList = new CopyOnWriteArrayList<CellRangeAddress>();
	private  boolean istheSameBook = false;
	private  MusicDataInfo model = null;
	public static  List<MusicDataInfo> bookList = new ArrayList<MusicDataInfo>();

	public static void main(String[] args) {
		String path = "E:/樱桃时光/每日上传/20140707周一/cherrytime乐库/场景/冲凉/Lounge/relax/relax.xls";
		File file = new File(path);
		new MusicDataGetFromExcel().doExcel(file);
		for(MusicDataInfo model : bookList){
			FileDoUtil.outLog(model.toString());
		}
	}

	public void doTheCell(int rowIndex, int cellIndex, Row row) {
		BooleanHolder isFirstFlag = new BooleanHolder(false);
		boolean flag = isMergedRegion(rowIndex, cellIndex, isFirstFlag);
		if (flag) {
			flag = isFirstFlag.value;
			if (flag) {
				if (model == null) {
					model = new MusicDataInfo();
				}
				istheSameBook = true;
				Cell cell = row.getCell(cellIndex);
				String value = getCellValue(cell);
				setModelValue(cellIndex, value);
			}
		}
	}

	public void setModelValue(int index, String str) {
		if (model == null || str == null) {
			return;
		}
		if(index == MusicConstants.getVlaueFromMusicLoadExcelCellMaping("listname") ){
			model.setListName(str);
		}else if(index == MusicConstants.getVlaueFromMusicLoadExcelCellMaping("enverionment")){
			model.setEnverionment(str);
		}else if(index == MusicConstants.getVlaueFromMusicLoadExcelCellMaping("category")){
			model.setCategory(str);
		}else if(index == MusicConstants.getVlaueFromMusicLoadExcelCellMaping("desc")){
			model.setDesc(str);
		}
	}

	public boolean isMergedRegion(int row, int column, BooleanHolder isFirstflag) {
		for (CellRangeAddress ca : CellRangeAddressList) {
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			if (row == firstRow && column == firstColumn) {
				isFirstflag.value = true;
				return true;
			}
			if (row == lastRow && column == lastColumn) {
				// FileDoUtil.outLog("remove" + row + ":" + column);
				CellRangeAddressList.remove(ca);
				istheSameBook = (istheSameBook ? false : istheSameBook);
				if (model != null && model.getListName() != null) {
					bookList.add(model);
				}
				model = null;
				return true;
			}
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}

		}
		return false;
	}

	public static MusicDataInfo getObjFormList(String bookName) {
		if(StringUtil.isEmptyString(bookName)){
			//FileDoUtil.outLog("歌单名称不能为空");
			return null;
		}
		if (bookList != null) {
			for (MusicDataInfo book : bookList) {
				if (book != null && bookName.equals(book.getListName())) {
					return book;
				}
			}
		}
		return new MusicDataInfo();
	}

	@Override
	public void clearValues() {
		istheSameBook = false;
		model = null;
	}

	@Override
	public void initValue(Sheet sheet) {
		CellRangeAddressList = getMergedRegions(sheet);
	}
}
