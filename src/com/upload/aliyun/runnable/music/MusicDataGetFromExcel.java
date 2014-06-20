/**
 * 
 */
package com.upload.aliyun.runnable.music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.omg.CORBA.BooleanHolder;

import com.upload.aliyun.util.POIUtil;

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
		String path = "E:/樱桃时光/熬夜/熬夜 终.xls";
		File file = new File(path);
		new MusicDataGetFromExcel().doExcel(file);
		for(MusicDataInfo model : bookList){
			System.out.println(model);
		}
	}

	public void doTheCell(int rowIndex, int cellIndex, HSSFRow row) {
		BooleanHolder isFirstFlag = new BooleanHolder(false);
		boolean flag = isMergedRegion(rowIndex, cellIndex, isFirstFlag);
		if (flag) {
			flag = isFirstFlag.value;
			if (flag) {
				if (model == null) {
					model = new MusicDataInfo();
				}
				istheSameBook = true;
				HSSFCell cell = row.getCell(cellIndex);
				String value = getCellValue(cell);
				setModelValue(cellIndex, value);
			}
		}
	}

	public void setModelValue(int index, String str) {
		if (model == null || str == null) {
			return;
		}
		switch (index) {
			case 1: model.setListName(str);break;
			case 3: model.setEnverionment(str);break;
			case 4: model.setCategory(str);break;
			case 7: model.setDesc(str);break;
			default:break;
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
				// System.out.println("remove" + row + ":" + column);
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
	public void initValue(HSSFSheet sheet) {
		CellRangeAddressList = getMergedRegions(sheet);
	}

}