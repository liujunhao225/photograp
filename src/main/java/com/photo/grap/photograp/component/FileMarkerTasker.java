package com.photo.grap.photograp.component;

import java.util.List;

import net.minidev.json.JSONObject;

import com.photo.grap.photograp.util.MysqlConnector;
import com.photo.grap.photograp.util.SystemConfig;


public class FileMarkerTasker extends Thread {

	@Override
	public void run() {

		while (true) {
			boolean fileflag = FileTasker.isEmpty();

			boolean clawerflag = ClawerTasker.isEmpty();

			boolean pictureflag = PictureDownloadTasker.isEmpty();

			boolean prepareflag = FileTasker.isPrepareFlag();

			if (fileflag && clawerflag && pictureflag && prepareflag) {

				List<JSONObject> tasks = MysqlConnector.getFileList(SystemConfig.TASK_INIT);
				for (JSONObject task : tasks) {
					// File file = new
					// File(SystemConfig.DOWNLOAD_FILE_PATH+fileName);
					// if(file.exists()){
					MysqlConnector.updateTask(task.getAsString("taskId"),SystemConfig.TASK_FINISHED_DOWNLOAD);
					// }else{
					// ExcelWriter.makeExcel(fileName);
					// }
				}
			} else {
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
