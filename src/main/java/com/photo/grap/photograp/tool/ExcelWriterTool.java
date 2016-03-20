package com.photo.grap.photograp.tool;

import java.io.File;
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
/**
 * write picture on the excel
 * 
 * @author liujunhao
 *
 */
public class ExcelWriterTool {

	private static Logger logger = Logger.getLogger(ExcelWriterTool.class
			.getCanonicalName());

	/**
	 * 
	 * @param file name
	 * @return
	 */
	public static String makeExcel(String fileName) {
		// 打开文件
		// BasicConfigurator.configure();
 		logger.info("【导出文件】打开原始文件");
		Workbook workbook = null;
		try {
			// workbook = WorkbookFactory.create(new File(
			// SystemConfig.SAVE_FILE_PATH + fileName));
			workbook = new XSSFWorkbook(
					OPCPackage.open(SystemConfig.SAVE_FILE_PATH + fileName));
			Sheet sheet = workbook.getSheetAt(0);
//			sheet.setDefaultRowHeight((short) 2500);
//			sheet.setDefaultColumnWidth(2000);
			sheet.setDefaultRowHeight((short) (8 * 256)); //设置默认行高，表示2个字符的高度
			sheet.setDefaultColumnWidth(17);  
			int maxRowNum = sheet.getLastRowNum();
			int columnCount = sheet.getRow(0).getLastCellNum();
			int addColumn1 = columnCount;
//			sheet.setColumnWidth(addColumn1+1, 2000);
//			sheet.setDefaultColumnWidth(2000);
//			int addColumn2 = columnCount + 1;
//			int addColumn3 = columnCount + 2;
			sheet.getRow(0).createCell(addColumn1);// 增加第一列
			sheet.getRow(0).getCell(addColumn1).setCellValue("照 片");
//			sheet.getRow(0).createCell(addColumn2);// 增加第二列
//			sheet.getRow(0).getCell(addColumn2).setCellValue("照 片 2");
//			sheet.getRow(0).createCell(addColumn3);// 增加第三列
//			sheet.getRow(0).getCell(addColumn3).setCellValue("照 片 3");
			// sheet.getRow(0).getCell(columnCount).setCellStyle(Cell.VERTICAL_CENTER);
			sheet.getRow(0).getCell(addColumn1).getCellStyle()
					.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
//			sheet.getRow(0).getCell(addColumn2).getCellStyle()
//					.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
//			sheet.getRow(0).getCell(addColumn3).getCellStyle()
//					.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
//			sheet.getRow(0).createCell(columnCount + 3, Cell.CELL_TYPE_STRING);
//			sheet.getRow(0).getCell(columnCount + 3).setCellValue("note");
//			sheet.getRow(0).getCell(columnCount + 3).getCellStyle()
//					.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
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
				String selectPhotoName = job.getAsString("selectPhoto");
				if (selectPhotoName == null || selectPhotoName.length() < 1) {
					selectPhotoName = job.getAsString("photoname1");
				}
//				String photo2 = job.getAsString("photoname2");
//				String photo3 = job.getAsString("photoname3");

				if (selectPhotoName == null || selectPhotoName.length() < 1) {
					continue;
				} else {
					// photoName = photoName
					// .substring(photoName.lastIndexOf("/") + 1);
					logger.info("【导出文件】插入图片开始");
					// photo1 start
					if(selectPhotoName !=null && selectPhotoName.length()>1 && new File(SystemConfig.IMAGE_FILE_PATH + selectPhotoName).exists()){
						InputStream inputStream = new FileInputStream(
								SystemConfig.IMAGE_FILE_PATH + selectPhotoName);

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
					}
					// photo1 end
					
					
					// photo2 start
//					if(photo2 !=null &&photo2.length()>1&&new File(SystemConfig.IMAGE_FILE_PATH + photo2).exists()){
//						InputStream inputStream2 = new FileInputStream(
//								SystemConfig.IMAGE_FILE_PATH + photo2);
//
//						byte[] imageBytes2 = IOUtils.toByteArray(inputStream2);
//
//						int pictureureIdx2 = workbook.addPicture(imageBytes2,
//								Workbook.PICTURE_TYPE_JPEG);
//						inputStream2.close();
//						CreationHelper helper2 = workbook.getCreationHelper();
//						Drawing drawing2 = sheet.createDrawingPatriarch();
//						ClientAnchor anchor2 = helper2.createClientAnchor();
//						anchor2.setCol1(addColumn2);
//						anchor2.setCol2(addColumn2 + 1);
//						anchor2.setRow1(i);
//						anchor2.setRow2(i + 1);
//						drawing2.createPicture(anchor2, pictureureIdx2);
//					}
					
					// photo2 end
					
					// photo3 start
//					if(photo3 !=null && photo3.length()>1 && new File(SystemConfig.IMAGE_FILE_PATH + photo3).exists()){
//						InputStream inputStream3 = new FileInputStream(
//								SystemConfig.IMAGE_FILE_PATH + photo3);
//						byte[] imageBytes3 = IOUtils.toByteArray(inputStream3);
//						int pictureureIdx3 = workbook.addPicture(imageBytes3,
//								Workbook.PICTURE_TYPE_JPEG);
//						inputStream3.close();
//						CreationHelper helper3 = workbook.getCreationHelper();
//						Drawing drawing3 = sheet.createDrawingPatriarch();
//						ClientAnchor anchor3 = helper3.createClientAnchor();
//						anchor3.setCol1(addColumn3);
//						anchor3.setCol2(addColumn3 + 1);
//						anchor3.setRow1(i);
//						anchor3.setRow2(i + 1);
//						drawing3.createPicture(anchor3, pictureureIdx3);
//					}
					// photo3 end
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
		}
		return SystemConfig.DOWNLOAD_FILE_PATH + fileName;// 返回excel路径
	}

	public static void main(String[] args) {
		System.out.println(makeExcel("25.xlsx"));
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
