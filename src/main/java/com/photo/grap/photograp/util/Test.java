package com.photo.grap.photograp.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Test {

	// 账户 ：郸城吧务团队
	// 71011002

	private static Logger logger = Logger.getLogger(Test.class
			.getCanonicalName());

	public static void main(String[] args) {
		BasicConfigurator.configure();

		try {

			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("MYSheet");

			InputStream inputStream = new FileInputStream(
					"d:\\TB1iqBvIpXXXXcCXFXXXXXXXXXX_!!0-item_pic.jpg");

			byte[] imageBytes = IOUtils.toByteArray(inputStream);

			int pictureureIdx = workbook.addPicture(imageBytes,
					Workbook.PICTURE_TYPE_JPEG);

			inputStream.close();

			CreationHelper helper = workbook.getCreationHelper();

			Drawing drawing = sheet.createDrawingPatriarch();

			ClientAnchor anchor = helper.createClientAnchor();
			anchor.setCol1(0);
			anchor.setCol2(1);
			anchor.setRow1(0);
			anchor.setRow2(1);
			drawing.createPicture(anchor, pictureureIdx);

			FileOutputStream fileOut = null;
			fileOut = new FileOutputStream("d:\\output.xlsx");
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
