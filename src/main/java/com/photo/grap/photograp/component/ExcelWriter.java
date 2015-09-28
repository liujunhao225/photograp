package com.photo.grap.photograp.component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.photo.grap.photograp.util.MysqlConnector;
import com.photo.grap.photograp.util.SystemConfig;

public class ExcelWriter {

	private static Logger logger = Logger.getLogger(ExcelWriter.class
			.getCanonicalName());

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static String makeExcel(String fileName) {
		// 打开文件
//		BasicConfigurator.configure();
		logger.info("【导出文件】打开原始文件");
		Workbook workbook = null;
		try {
//			workbook = WorkbookFactory.create(new File(
//					SystemConfig.SAVE_FILE_PATH + fileName));
			workbook =  new XSSFWorkbook(OPCPackage.open(SystemConfig.SAVE_FILE_PATH + fileName));
			Sheet sheet = workbook.getSheetAt(0);
			sheet.setDefaultRowHeight((short) 1000);
			int maxRowNum = sheet.getLastRowNum();
			int columnCount = sheet.getRow(0).getLastCellNum();
			sheet.getRow(0).createCell(columnCount);
			sheet.getRow(0).getCell(columnCount).setCellValue("photo");
			sheet.getRow(0).getCell(columnCount).getCellStyle()
					.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
			sheet.getRow(0).createCell(columnCount + 1, Cell.CELL_TYPE_STRING);
			sheet.getRow(0).getCell(columnCount + 1).setCellValue("note");
			sheet.getRow(0).getCell(columnCount + 1).getCellStyle()
					.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
			for (int i = 1; i < maxRowNum; i++) {
				Row row = sheet.getRow(i);
				// 检查行
				if (!checkRow(row)) {
					break;
				}
				// row.
				row.createCell(columnCount);
				row.createCell(columnCount + 1, Cell.CELL_TYPE_STRING);
				// 取得 id对应的图片
				JSONObject job = MysqlConnector.findPhoto(row.getCell(0)
						.getStringCellValue());
				if (job == null)
					continue;
				String photoName = job.getAsString("selectPhoto");

				if (photoName == null || photoName.length()<1) {
					continue;
				} else {
//					photoName = photoName
//							.substring(photoName.lastIndexOf("/") + 1);
					logger.info("【导出文件】插入图片开始");
					InputStream inputStream = new FileInputStream(
							SystemConfig.IMAGE_FILE_PATH + photoName);

					byte[] imageBytes = IOUtils.toByteArray(inputStream);

					int pictureureIdx = workbook.addPicture(imageBytes,
							Workbook.PICTURE_TYPE_JPEG);
					inputStream.close();
					CreationHelper helper = workbook.getCreationHelper();
					Drawing drawing = sheet.createDrawingPatriarch();
					ClientAnchor anchor = helper.createClientAnchor();
					anchor.setCol1(columnCount);
					anchor.setCol2(columnCount + 1);
					anchor.setRow1(i);
					anchor.setRow2(i + 1);
					drawing.createPicture(anchor, pictureureIdx);
					logger.info("【导出文件】插入图片结束");
				}
			}
			FileOutputStream fileOut = new FileOutputStream(
					SystemConfig.DOWNLOAD_FILE_PATH + fileName);
			workbook.write(fileOut);
			fileOut.close();
		} catch (InvalidFormatException e) {
			logger.error("【导出文件】excel文件格式 不正确");
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			logger.error("【导出文件】excel文件IO异常");
			e.printStackTrace();
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
//			logger.info("【导出文件】关闭文件");
//			if (workbook != null)
//				try {
//					workbook.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
		}
		return SystemConfig.DOWNLOAD_FILE_PATH + fileName;// 返回excel路径
	}

	public static void main(String[] args) {
		System.out.println(makeExcel("测试抓图.xlsx"));
	}

	private static boolean checkRow(Row row) {
		try {
			row.getCell(0).getStringCellValue();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
