package com.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.photo.grap.photograp.util.MysqlConnector;
import com.photo.grap.photograp.util.SystemConfig;

public class DodownloadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		String taskId = request.getParameter("taskId");
		
		net.minidev.json.JSONObject job = MysqlConnector.getTaskBaseTaskId(taskId);
		
		JSONObject returnJob = new JSONObject();
		File file = new File(SystemConfig.DOWNLOAD_FILE_PATH+job.getAsString("fileName"));
		if (file.exists()) {
			returnJob.put("flag", true);
			returnJob.put("fileName", job.getAsString("fileName"));
		} else {
			returnJob.put("flag", false);
		}
		response.getWriter().print(returnJob.toString());
	}
}
