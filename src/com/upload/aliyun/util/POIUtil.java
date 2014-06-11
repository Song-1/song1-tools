/**
 * 
 */
package com.upload.aliyun.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.omg.CORBA.BooleanHolder;

import com.upload.aliyun.runnable.BookDataInfo;

/**
 * @author Administrator
 *
 */
public class POIUtil {

	private static CopyOnWriteArrayList<CellRangeAddress> CellRangeAddressList = new CopyOnWriteArrayList<CellRangeAddress>();
	private static boolean istheSameBook = false;
	private static BookDataInfo model = null;
	public static List<BookDataInfo> bookList = new ArrayList<BookDataInfo>();

	public static void main(String[] args) {
		String path = "E:/职场培训 5.21.xls";
		File file = new File(path);
		doExcel(file);
	}
	
	public static void doExcel(File file){
		if(file == null){
			return;
		}
		if (file.exists()) {
			FileInputStream fIn;
			try {
				fIn = new FileInputStream(file);
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fIn);
				int sheetCount = hssfWorkbook.getNumberOfSheets();
				for (int i = 0; i < sheetCount; i++) {
					CellRangeAddressList.clear();
					HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
					getMergedRegions(sheet);
					doSheet(sheet);
				}

			} catch (Exception e) {
				System.out.println("读取Excel["+file.getAbsolutePath()+"]文件发生异常.............");
				e.printStackTrace();
			}
			
		}
	}

	public static void doSheet(HSSFSheet sheet) {
		if (sheet == null) {
			return;
		}
		istheSameBook = false;
		model = null;
		int rows = sheet.getPhysicalNumberOfRows();
		for (int rowIndex = 1; rowIndex < rows; rowIndex++) {
			HSSFRow row = sheet.getRow(rowIndex);
			int cells = row.getPhysicalNumberOfCells();
			for (int cellIndex = 0; cellIndex < cells; cellIndex++) {
				BooleanHolder isFirstFlag = new BooleanHolder(false);
				boolean flag = isMergedRegion(rowIndex, cellIndex, isFirstFlag);
				if (flag) {
					flag = isFirstFlag.value;
					if (flag) {
						if (model == null) {
							model = new BookDataInfo();
						}
						istheSameBook = true;
						HSSFCell cell = row.getCell(cellIndex);
						String value = getCellValue(cell);
						setModelValue(cellIndex, value);
					}
				}
				// else {
				// HSSFCell cell = row.getCell(cellIndex);
				// String value = getCellValue(cell);
				// if(istheSameBook)model.addBook(value);
				// }
			}
		}
	}

	public static void setModelValue(int index, String str) {
		if (model == null || str == null) {
			return;
		}
		switch (index) {
		case 0:
			model.setId(str);
			break;
		case 1:
			model.setTypeLevelOne(str);
			break;
		case 3:
			model.setTypeLevelTwo(str);
			break;
		case 5:
			model.setName(str);
			break;
		case 6:
			model.setBookInfo(str);
			break;
		case 7:
			model.setImg(str);
			break;
		case 8:
			model.setDesc(str);
			break;

		default:
			break;
		}

	}

	public static void getMergedRegions(HSSFSheet sheet) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = (CellRangeAddress) sheet.getMergedRegion(i);
			CellRangeAddressList.add(ca);
		}
	}

	/**
	 * 判断是否合并单元格,如果是合并单元格,其起始行和列不在其中，可以取其值作为合并单元格的值
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public static boolean isMergedRegion(int row, int column, BooleanHolder isFirstflag) {
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
				if (model != null && model.getUuid() != null) {
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

	public static String getCellValue(HSSFCell cell) {
		if (cell == null) {
			return null;
		}
		String value = null;
		switch (cell.getCellType()) {

		case HSSFCell.CELL_TYPE_FORMULA:
			value = cell.getCellFormula();
			break;

		case HSSFCell.CELL_TYPE_NUMERIC:
			value = "" + cell.getNumericCellValue();
			break;

		case HSSFCell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;

		default:
		}
		return value;
	}
	
	public static BookDataInfo getObjFormList(String bookName){
		if(bookList != null){
			for (BookDataInfo book : bookList) {
				if(book != null && bookName.equals(book.getName())){
					return book;
				}
			}
		}
		return new BookDataInfo();
	}

}
