package com.photo.grap.photograp.tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.photo.grap.photograp.util.SystemConfig;

/**
 * the frist column is the product id,this tool is purpose to get product ids
 *
 * 
 * @author liujunhao
 *
 */
public class ExcelReaderTool {

	private static Logger logger = Logger.getLogger(ExcelReaderTool.class);

	/**
	 * @param fil path
	 * @return
	 */
	public static List<String> readFirstColumn(String path) {
		logger.info("【解析文件】解析excel开始");
		File file = new File(SystemConfig.SAVE_FILE_PATH + path);
		List<String> strList = new ArrayList<String>();
		if (file.exists()) {
			Workbook wb = null;
			try {
				wb = WorkbookFactory.create(file);
				Sheet sheet = wb.getSheetAt(0);
				int maxRowIndex = sheet.getLastRowNum();
				for (int i = 1; i <= maxRowIndex; i++) {
					Row tempRow = sheet.getRow(i);
					Cell tempCell = tempRow.getCell(0);
					if (tempCell == null)
						continue;
					tempCell.setCellType(Cell.CELL_TYPE_STRING);

					String value = tempCell.getStringCellValue();
					strList.add(value);
				}
				logger.info("【解析文件】解析excel结束");
			} catch (InvalidFormatException e) {
				logger.error("【解析文件】excel格式不正确");
				e.printStackTrace();
			} catch (IOException e) {
				logger.error("【解析文件】excel格式不正确");
				e.printStackTrace();
			} catch (Exception e) {
				logger.error("【解析文件】未处理异常");
				e.printStackTrace();
			} finally {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} else {
			logger.warn("【解析文件，文件不存在】");
			return null;
		}
		return strList;

	}
}