package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.photo.grap.photograp.util.SystemConfig;

public class POITemplateTest {

	public static void main(String args[]) {

		try {

			Workbook wb = new XSSFWorkbook(OPCPackage.open("D:\\tb\\template.xlsx"));
			Sheet sheet = wb.getSheetAt(0);
			
			sheet.getRow(2).getCell(5).setCellValue("Changed value"); 
			
//			InputStream inputStream = new FileInputStream(
//					"d:\\tb\\test2.jpg");
//
//			byte[] imageBytes = IOUtils.toByteArray(inputStream);
//
//			int pictureureIdx = wb.addPicture(imageBytes,
//					Workbook.PICTURE_TYPE_JPEG);
//			inputStream.close();
//			CreationHelper helper = wb.getCreationHelper();
//			Drawing drawing = sheet.createDrawingPatriarch();
//			ClientAnchor anchor = helper.createClientAnchor();
//			anchor.setCol1(1);
//			anchor.setCol2(1 + 1);
//			anchor.setRow1(2);
//			anchor.setRow2(2 + 1);
//			drawing.createPicture(anchor, pictureureIdx);
			// All done
			FileOutputStream fileOut = new FileOutputStream("D:\\tb\\new.xls");
			wb.write(fileOut);
			fileOut.close();
//			wb.close();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
