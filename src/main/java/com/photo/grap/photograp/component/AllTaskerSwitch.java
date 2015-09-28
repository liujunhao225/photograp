package com.photo.grap.photograp.component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;

public class AllTaskerSwitch extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static {

		ConsoleAppender console = new ConsoleAppender(); // create appender
		// configure the appender
		String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		console.setLayout(new PatternLayout(PATTERN));
		console.setThreshold(org.apache.log4j.Level.INFO);
		console.activateOptions();
		BasicConfigurator.configure(console);

		// http去日志
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime",
				"true");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http.wire",
				"ERROR");

		System.out.println("加载任务启动配置开始");

		ExecutorService service1 = Executors.newFixedThreadPool(1);

		ClawerTasker clawerTasker = new ClawerTasker();
		service1.submit(clawerTasker.new Handler());

		ExecutorService service2 = Executors.newFixedThreadPool(1);
		FileTasker fileTasker = new FileTasker();
		service2.submit(fileTasker.new Handler());

		ExecutorService service3 = Executors.newFixedThreadPool(1);
		PictureDownloadTasker pictureDownloadTasker = new PictureDownloadTasker();
		service3.submit(pictureDownloadTasker.new Handler());

		FileMarkerTasker fileMarkerTasker = new FileMarkerTasker();
		fileMarkerTasker.start();

		System.out.println("加载任务启动配置完成");
	}
}
