package com.photo.grap.photograp.manage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;

import com.photo.grap.photograp.tasker.ClawerTasker;
import com.photo.grap.photograp.tasker.FileTasker;
import com.photo.grap.photograp.tasker.XCProxyCollectorTask;
import com.photo.grap.photograp.tasker.YDLProxyCollectorTask;
import com.photo.grap.photograp.util.ProxyContainer;

public class AllTaskerSwitch extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static {

		String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		Layout layout = new PatternLayout(PATTERN);
		
		try {
//			FileAppender fileappender = new FileAppender(layout, "d://log.txt");
			ConsoleAppender console = new ConsoleAppender(); // create appender
			
			console.setLayout(new PatternLayout(PATTERN));
			console.setThreshold(org.apache.log4j.Level.INFO);
			console.activateOptions();
//			fileappender.setThreshold(org.apache.log4j.Level.INFO);
//			fileappender.activateOptions();
			BasicConfigurator.configure(console);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//config  log4j end
		
		// http去日志 start
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
		//http 去日志 end

		//清空代理表
//		ProxyContainer.removeAllProxy();
		//抓取西刺代理
//		ExecutorService service3 = Executors.newFixedThreadPool(1);
//		 service3.submit(new XCProxyCollectorTask());
		 //抓取有代理网站
//		 ExecutorService service4 = Executors.newFixedThreadPool(1);
//		 service4.submit(new YDLProxyCollectorTask());

//		 try {
//			Thread.sleep(2*60*1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		 //抓取任务
//		ExecutorService service1 = Executors.newFixedThreadPool(1);
//		ClawerTasker clawerTasker = new ClawerTasker();
//		service1.submit(clawerTasker);
//
//		//解析文件
//		ExecutorService service2 = Executors.newFixedThreadPool(1);
//		FileTasker fileTasker = new FileTasker();
//		service2.submit(fileTasker);
		//
		// PictureDownloadTasker pictureDownloadTasker = new
		// PictureDownloadTasker();
		// service3.submit(pictureDownloadTasker.new Handler());
		// FileMarkerTasker fileMarkerTasker = new FileMarkerTasker();
		// fileMarkerTasker.start();
		
		System.out.println("加载任务启动配置完成");
	}
}
