package com.photo.grap.photograp.tasker;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.Logger;

import com.photo.grap.photograp.tool.ExcelReaderTool;
import com.photo.grap.photograp.util.MysqlConnector;

public class FileTasker extends Thread{

	private static Logger logger = Logger.getLogger(FileTasker.class);
	private static BlockingDeque<String> fileDeque = new LinkedBlockingDeque<String>();

	public void run() {
		logger.info("启动解析文件任务*****");
		String filename;
		try {
			while (true) {
				filename = fileDeque.take();
				List<String> productIdList = ExcelReaderTool
						.readFirstColumn(filename);
				addToProductList(productIdList);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 插入商品
	 * 
	 * @param productIdList
	 */
	private void addToProductList(List<String> productIdList) {

		for (String productId : productIdList) {
			MysqlConnector.insertPhoto(productId);
		}

	}

	public static void addFile(String filePath) {
		try {
			fileDeque.put(filePath);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static boolean isEmpty() {
		return fileDeque.size() > 0 ? false : true;
	}
}
