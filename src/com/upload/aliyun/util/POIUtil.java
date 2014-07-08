/**
 * 
 */
package com.upload.aliyun.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.omg.CORBA.BooleanHolder;

/**
 * @author Administrator
 *
 */
public abstract class POIUtil {

	public static void main(String[] args) {
//		String path = "E:/职场培训 5.21.xls";
//		File file = new File(path);
//		// doExcel(file);
	}

	/**
	 * 处理Excel
	 * @param file
	 */
	public void doExcel(File file) {
		if (file == null) {
			return;
		}
		if (file.exists()) {
			FileInputStream fIn;
			try {
				fIn = new FileInputStream(file);
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fIn);
				int sheetCount = hssfWorkbook.getNumberOfSheets();
				for (int i = 0; i < sheetCount; i++) {
					HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
					doSheet(sheet);
				}

			} catch (Exception e) {
				FileDoUtil.outLog("读取Excel[" + file.getAbsolutePath() + "]文件发生异常.............");
				e.printStackTrace();
			}

		}
	}

	/**
	 * 处理工作簿
	 * @param sheet
	 */
	public void doSheet(HSSFSheet sheet) {
		if (sheet == null) {
			return;
		}
		initValue(sheet);
		clearValues();
		int rows = sheet.getPhysicalNumberOfRows();
		for (int rowIndex = getRowStartIndex(); rowIndex < rows; rowIndex++) {
			HSSFRow row = sheet.getRow(rowIndex);
			if(row ==null){
				continue;
			}
			int cells = row.getPhysicalNumberOfCells();
			for (int cellIndex = getCellStartIndex(); cellIndex < cells; cellIndex++) {
				doTheCell(rowIndex, cellIndex, row);
			}
		}
	}

	/**
	 * 判断是否合并单元格,如果是合并单元格,其起始行和列不在其中，可以取其值作为合并单元格的值
	 * 
	 * <pre>
	 * 		操作时先调用getMergedRegions方法,获取工作簿的所有合并区域
	 * </pre>
	 * 
	 * @param row 行下标
	 * @param column  列下标
	 * @param isFirstflag 是否是合并区域的第一个单元格,此单元格的值为整个合并区域的值
	 * @return
	 */
	public abstract boolean isMergedRegion(int row, int column, BooleanHolder isFirstflag);

	/**
	 * 处理单元格的操作
	 * 
	 * @param rowIndex
	 * @param cellIndex
	 * @param row
	 */
	public abstract void doTheCell(int rowIndex, int cellIndex, HSSFRow row);

	/**
	 * 返回开始行
	 */
	public int getRowStartIndex(){
		return 1;
	}
	/**
	 * 返回开始列
	 */
	public int getCellStartIndex(){
		return 0;
	}
	/**
	 * 清理变量的值
	 */
	public abstract void clearValues();
	/**
	 * 
	 */
	public abstract void initValue(HSSFSheet sheet);

	/**
	 * 将单元格的值赋给数据对象
	 * 
	 * @param cellIndex
	 * @param value
	 */
	public abstract void setModelValue(int cellIndex, String value);

	/**
	 * 获取工作簿的所有合并单元
	 * 
	 * @param sheet
	 * @return
	 */
	public static CopyOnWriteArrayList<CellRangeAddress> getMergedRegions(HSSFSheet sheet) {
		CopyOnWriteArrayList<CellRangeAddress> cellRangeAddressList = new CopyOnWriteArrayList<CellRangeAddress>();
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = (CellRangeAddress) sheet.getMergedRegion(i);
			cellRangeAddressList.add(ca);
		}
		return cellRangeAddressList;
	}

	/**
	 * 获取单元格的数据
	 * 
	 * @param cell
	 * @return
	 */
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

}
