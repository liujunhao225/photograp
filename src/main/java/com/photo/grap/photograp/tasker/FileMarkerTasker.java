package com.photo.grap.photograp.tasker;

import java.util.List;

import org.apache.log4j.Logger;

import net.minidev.json.JSONObject;

import com.photo.grap.photograp.util.MysqlConnector;
import com.photo.grap.photograp.util.SystemConfig;


public class FileMarkerTasker extends Thread {
	
	private static Logger logger = Logger.getLogger(FileMarkerTasker.class);

	@Override
	public void run() {

		while (true) {
			boolean fileflag = FileTasker.isEmpty();


			boolean pictureflag = PictureDownloadTasker.isEmpty();



				List<JSONObject> tasks = MysqlConnector.getFileList(SystemConfig.TASK_INIT);
				for (JSONObject task : tasks) {
					logger.info("【下载图片完成后】更新任务状态为已下载--开始");
					MysqlConnector.updateTask(task.getAsString("taskId"),SystemConfig.TASK_FINISHED_DOWNLOAD);
					logger.info("【下载图片完成后】更新任务状态为已下载--结束");
				
				}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
