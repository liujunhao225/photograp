package com.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.photo.grap.photograp.component.FileTasker;
import com.photo.grap.photograp.util.MysqlConnector;
import com.photo.grap.photograp.util.SystemConfig;

public class ProxyIpSettingServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(ProxyIpSettingServlet.class
			.getName());
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("记录日志");
		logger.info("测试 记录");
		// 保存文件
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletContext servletContext = this.getServletConfig()
				.getServletContext();
		File repository = (File) servletContext
				.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);
		ServletFileUpload upload = new ServletFileUpload(factory);
		String filename = "";
		
		//清空ip表数据
		
		MysqlConnector.truncateProxyIp();
		
		try {
			List<FileItem> items = upload.parseRequest(request);
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {

				} else {
					InputStream in = item.getInputStream();
					BufferedReader reader =  new BufferedReader(new InputStreamReader(in));
					String proxyIpPort = "";
					List<String> proxyIpPortList = new ArrayList<String>();
					while((proxyIpPort=reader.readLine())!=null){
						proxyIpPortList.add(proxyIpPort);
					}
					MysqlConnector.insertProxyIpPort(proxyIpPortList);
					in.close();
					item.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 解析文件
		// 返回
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.println("Finished uploading files!");
		writer.close();
	}
}
