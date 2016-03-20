package com.photo.grap.photograp.tasker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.Logger;

import com.photo.grap.photograp.util.ImageDownloaderUtil;
import com.photo.grap.photograp.util.PhotoRenameTool;
import com.photo.grap.photograp.util.SystemConfig;

public class PictureDownloadTasker extends Thread {

	private static BlockingDeque<Map<String,String>> photoDeque = new LinkedBlockingDeque<Map<String,String>>();

	private static Logger logger = Logger
			.getLogger(PictureDownloadTasker.class);

	public void run() {
		logger.info("启动下载任务*****");
		while (true) {
			String fileName = "";
			try {
				Map<String,String> tempMap= photoDeque.take();
				fileName = tempMap.get("url").substring(tempMap.get("url").lastIndexOf("/"));
				logger.info("【下载任务】图片下载开始，图片名称：" + fileName);
				String md5FileName = PhotoRenameTool.getPhotoName(tempMap.get("url"));

				ImageDownloaderUtil.downPictureToInternet(
						SystemConfig.IMAGE_FILE_PATH + md5FileName, "http:"
								+ tempMap,Integer.valueOf(tempMap.get("sign")));
				logger.info("【下载任务】图片下载完成，图片名称：" + fileName);

			} catch (InterruptedException e) {
				logger.info("【下载任务】图片下载失败" + fileName);
				e.printStackTrace();
			} catch (Exception e) {
				logger.info("【下载任务】图片下载失败" + fileName);
				e.printStackTrace();
			}
		}

	}

	public static void addToPhoto(String[] photos,int sign) {
		for (String photo : photos) {
			try {
				Map<String,String> tempMap = new HashMap<String, String>();
				if (photo != null && photo.length() > 0) {
					tempMap.put("url", photo);
					tempMap.put("id", sign+"");
					photoDeque.put(tempMap);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isEmpty() {
		return photoDeque.size() > 0 ? false : true;
	}

}
