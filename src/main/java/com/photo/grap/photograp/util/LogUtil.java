package com.photo.grap.photograp.util;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class LogUtil {
	private Logger logger = null;
	static {
		System.out.println("运行一次");
		BasicConfigurator.configure();
	}

	public LogUtil(String className) {
		logger = Logger.getLogger(className);
	}

	public Logger getLogger() {
		return this.logger;
	}

}
