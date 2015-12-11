package com.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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

public class DouploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(DouploadServlet.class
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
		try {
			List<FileItem> items = upload.parseRequest(request);
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {

				} else {
					filename = new String(item.getName().getBytes(), "utf-8");
					filename = filename.substring(
							filename.lastIndexOf("\\") + 1, filename.length());
					File uploadFile = new File(SystemConfig.SAVE_FILE_PATH
							+ "/" + filename);
					InputStream in = item.getInputStream();
					FileOutputStream fos = new FileOutputStream(uploadFile);
					int len;
					byte[] buffer = new byte[1024];
					while ((len = in.read(buffer)) > 0)
						fos.write(buffer, 0, len);
					fos.close();
					in.close();
					item.delete();
					MysqlConnector.insertFileTask(filename,SystemConfig.TASK_INIT);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 解析文件
		FileTasker.addFile(filename);
		// 返回
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.println("Finished uploading files!");
		writer.close();
	}
}