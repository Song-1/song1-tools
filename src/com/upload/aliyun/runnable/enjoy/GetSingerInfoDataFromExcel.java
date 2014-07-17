/**
 * 
 */
package com.upload.aliyun.runnable.enjoy;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omg.CORBA.BooleanHolder;

import com.tools.song1.util.FileDoUtil;
import com.tools.song1.util.POIUtil;
import com.tools.song1.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class GetSingerInfoDataFromExcel extends POIUtil{
	//// 歌手与所属地域映射数据存放
	public static final Map<String,String> SINGER_TYPE_MAPPING = new HashMap<String, String>();
	public static void main(String[] args) {
		String path = "E:/享CD/地域/地域艺人划分表格1.xlsx";
		File file = new File(path);
		new GetSingerInfoDataFromExcel().doExcel(file);
		for(Map.Entry<String, String> entry : SINGER_TYPE_MAPPING.entrySet()){
			System.out.println(entry.getKey() + ":::" + entry.getValue());
		}
	}
	
	/**
	 * 处理Excel
	 * @param file
	 */
	@SuppressWarnings("unused")
	public void doExcel(File file)  {
		if (file == null) {
			return;
		}
		if (file.exists()) {
			FileInputStream fIn = null;
			Workbook hssfWorkbook = null;
			try {
				try{
					fIn = new FileInputStream(file);
				    hssfWorkbook = new XSSFWorkbook(fIn);
				}catch(Exception e){
					e.printStackTrace();
					fIn = new FileInputStream(file);
					hssfWorkbook = new HSSFWorkbook(fIn);
				}
				if(hssfWorkbook == null){
					throw new Exception("获取Excel文件发生异常...........");
				}
				int sheetCount = hssfWorkbook.getNumberOfSheets();
				for (int i = 0; i < sheetCount; i++) {
					Sheet sheet = hssfWorkbook.getSheetAt(i);
					if (sheet == null) {
						continue;
					}
					String singerTypaName = sheet.getSheetName().trim();
					int rows = sheet.getPhysicalNumberOfRows();
					for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
						Row row = sheet.getRow(rowIndex);
						if(row ==null){
							continue;
						}
						Cell cell = row.getCell(1);
						String singerName = getCellValue(cell);
						addValue(singerName, singerTypaName);
					}
				}

			} catch (Exception e) {
				FileDoUtil.outLog("读取Excel[" + file.getAbsolutePath() + "]文件发生异常.............");
				e.printStackTrace();
			}finally{
				if(fIn != null){
					try{
						fIn.close();
					}catch(Exception e){
						FileDoUtil.outLog("关闭Excel[" + file.getAbsolutePath() + "]文件流发生异常.............");
						e.printStackTrace();
					}finally{
						fIn = null;
					}
				}
			}

		}
	}
	
	public static void addValue(String key,String value){
		if(StringUtil.isEmptyString(key)){
			return;
		}
		SINGER_TYPE_MAPPING.put(key, value);
	}
	
	public static String getMappingValue(String key){
		if(StringUtil.isEmptyString(key)){
			return null;
		}else{
			return SINGER_TYPE_MAPPING.get(key);
		}
	}

	@Override
	public boolean isMergedRegion(int row, int column, BooleanHolder isFirstflag) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void doTheCell(int rowIndex, int cellIndex, Row row) {
		// TODO Auto-generated method stub
		
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
