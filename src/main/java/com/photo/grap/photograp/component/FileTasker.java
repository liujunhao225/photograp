package com.photo.grap.photograp.component;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;

import com.photo.grap.photograp.util.MysqlConnector;

public class FileTasker {

	private static Logger logger = Logger.getLogger(FileTasker.class);
	private static BlockingDeque<String> fileDeque = new LinkedBlockingDeque<String>();

	private static boolean prepareFlag = false;

	public class Handler extends Thread {

		public void run() {
			logger.info("启动解析文件任务*****");
			String filename;
			try {
				while (true) {
					filename = fileDeque.take();
					prepareFlag = false;
					List<String> productIdList = ExcelReaderTool
							.readFirst(filename);
					addTaskProductMapTable(filename,productIdList);
					addToProductList(productIdList);
					ClawerTasker.addIdsToDeque(productIdList);
					prepareFlag = true;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 插入商品
	 * 
	 * @param productIdList
	 */
	private void addToProductList(List<String> productIdList) {
		
		for (String productId : productIdList) {
			if (MysqlConnector.findPhoto(productId) == null)
				MysqlConnector.insertPhoto(productId);
		}
		

	}
	/**
	 * 插入对应关系表
	 * @param fileName
	 * @param productIdList
	 */
	private void addTaskProductMapTable(String fileName,List<String> productIdList){
		JSONObject job = MysqlConnector.getTaskBaseFileName(fileName);
		String taskId = job.getAsString("taskId");
		for (String productId : productIdList) {
			if (MysqlConnector.findPhoto(productId) == null)
				MysqlConnector.insertTaskProMap(taskId,productId);
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

	public static boolean isPrepareFlag() {
		return prepareFlag;
	}

}
