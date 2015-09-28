package com.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import com.photo.grap.photograp.util.MysqlConnector;

public class TaskListServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
//		
//		String pageSize = request.getParameter("rows");
//
//		String page = request.getParameter("page");
//		
//		// 前台传入taskId 
//		String taskId = request.getParameter("taskId");
//		
//		int pageInt = 0;
//		int pageSizeInt = 0;
//		try {
//			pageInt = Integer.parseInt(page);
//			pageSizeInt = Integer.parseInt(pageSize);
//		} catch (Exception e) {
//			pageInt = 0;
//			pageSizeInt = 15;
//		}
//		
		response.setHeader("Content-type", "text/html;charset=UTF-8"); 
		List<JSONObject> list = MysqlConnector.getFileList();
		JSONObject job = new JSONObject();
		job.put("page", 1);
		job.put("total", 1);
		job.put("records", list.size());
		job.put("rows", list);

		response.getWriter().print(job);
	}
}
