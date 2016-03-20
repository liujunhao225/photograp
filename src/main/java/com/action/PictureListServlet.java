package com.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.photo.grap.photograp.util.MysqlConnector;

import net.minidev.json.JSONObject;

public class PictureListServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2727916780354520667L;
	
	private static final Logger logger = Logger.getLogger(PictureListServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		String pageSize = request.getParameter("rows");

		String page = request.getParameter("page");
		
		String taskId = request.getParameter("taskId");
		
		String state = request.getParameter("state");
		
		String productid = request.getParameter("productid");
		
		logger.info("state is "+state);
		
		int pageInt = 0;
		int pageSizeInt = 0;
		try {
			pageInt = Integer.parseInt(page);
			pageSizeInt = Integer.parseInt(pageSize);
		} catch (Exception e) {
			pageInt = 0;
			pageSizeInt = 15;
		}
		List<JSONObject> listjob = MysqlConnector.getPhotolist(pageSizeInt, pageInt,productid,state);
		int count = MysqlConnector.getListCount(productid,state);

		int totalPage = (int) Math.ceil(count / (double)pageSizeInt);
		JSONObject job = new JSONObject();
		job.put("page", page);
		job.put("total", totalPage);
		job.put("records", count);
		job.put("rows", listjob);

		if (listjob != null) {
			try {
				response.getWriter().write(job.toString());
			} catch (IOException e) {
			}
		}
	}
}
