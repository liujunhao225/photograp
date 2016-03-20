package com.action;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import com.photo.grap.photograp.tool.ExcelWriterTool;
import com.photo.grap.photograp.util.MysqlConnector;
import com.photo.grap.photograp.util.SystemConfig;

public class MakeFileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		String taskId = request.getParameter("taskId");

		JSONObject job = MysqlConnector.getTaskBaseTaskId(taskId);
		String fileName = job.getAsString("fileName");
		// 更新任务状态为文件生成中
		MysqlConnector.updateTask(taskId, SystemConfig.TASK_FILE_MAKING);

		if (fileName != null && fileName.length() > 0) {
			String result = ExcelWriterTool.makeExcel(fileName);
			if (result.length() < 1) {
				MysqlConnector.updateTask(taskId,
						SystemConfig.TASK_FILE_MAKING_ERROR);
			}
			MysqlConnector.updateTask(taskId, SystemConfig.TASK_FILE_MAKED);
		}
		// 更新任务状态为已生成文件
		MysqlConnector.updateTask(taskId, SystemConfig.TASK_FILE_MAKED);

	}

}
