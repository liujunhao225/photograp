package com.photo.grap.photograp.component;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.Logger;

import net.minidev.json.JSONObject;

import com.photo.grap.photograp.util.MysqlConnector;

public class ClawerTasker {

	private static BlockingDeque<String> idsDeque = new LinkedBlockingDeque<String>();

	private static Logger logger = Logger.getLogger(ClawerTasker.class);

	public class Handler extends Thread {

		public void run() {
			logger.info("启动抓取任务*****");
			while (true) {
				String productId = "";
				try {
					productId = idsDeque.take();
					JSONObject job = MysqlConnector.findPhoto(productId);
					if (job != null) {
						Object picture1 = job.get("photourl1");
						// 没有图片
						String[] pictures = {};
						if (picture1 == null || "".equals(picture1.toString())) {
							logger.info("【抓取任务】开始抓取ID:" + productId);
							pictures = TaoBaoPictureTool.getPicture(productId);
							if(pictures ==null){
								continue;
							}
							logger.info("【抓取任务】完成抓取ID:" + productId);
							boolean existFlag = false;
							for (String ss : pictures) {
								if (ss != null && ss.length() > 0) {
									existFlag = true;
									break;
								}
							}
							if("709009-664".equals(productId)){
								System.out.println("**********************");
							}
							if (existFlag) {
								
								if (pictures != null && pictures.length > 0) {
									MysqlConnector.updatePhotoAddress(pictures, productId);
									PictureDownloadTasker.addToPhoto(pictures);
								}
							}
						}
					}
				} catch (InterruptedException e) {
					logger.error("【抓取任务】产品号:" + productId + "出现异常");
				}
			}

		}
	}

	public static void addIdsToDeque(List<String> productList) {
		try {
			for (String productid : productList) {
				idsDeque.put(productid);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static boolean isEmpty() {
		return idsDeque.size() > 0 ? false : true;
	}

	public static int size() {
		return idsDeque.size();
	}

}
