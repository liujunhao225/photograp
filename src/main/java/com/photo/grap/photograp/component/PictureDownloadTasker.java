package com.photo.grap.photograp.component;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.Logger;

import com.photo.grap.photograp.util.ImageDownloaderUtil;
import com.photo.grap.photograp.util.PhotoRenameTool;
import com.photo.grap.photograp.util.SystemConfig;

public class PictureDownloadTasker {

	private static BlockingDeque<String> photoDeque = new LinkedBlockingDeque<String>();

	private static Logger logger = Logger
			.getLogger(PictureDownloadTasker.class);

	public class Handler extends Thread {

		public void run() {
			logger.info("启动下载任务*****");
			String photourl;
			while (true) {
				String fileName = "";
				try {
					photourl = photoDeque.take();
					fileName = photourl.substring(photourl.lastIndexOf("/"));
					logger.info("【下载任务】图片下载开始，图片名称：" + fileName);
					String md5FileName = PhotoRenameTool.getPhotoName(photourl);
				
					ImageDownloaderUtil.downPictureToInternet(
							SystemConfig.IMAGE_FILE_PATH + md5FileName, "http:"
									+ photourl);
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
	}

	public static void addToPhoto(String[] photos) {
		for (String photo : photos) {
			try {
				if (photo != null && photo.length() > 0) {
					photoDeque.put(photo);
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
