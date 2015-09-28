package com.photo.grap.photograp.component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.photo.grap.photograp.util.LogUtil;

public class CheckTool {
	protected static boolean checkTitle(String source, String text) {
		Pattern pattern = Pattern.compile("[a-zA-Z\\d\\s\\-]{8,20}");
		Matcher matcher = pattern.matcher(source);
		String tempValue = "";
		if (matcher.find()) {
			tempValue = matcher.group().trim();
		}
		if (text.equals(tempValue)) {
			return true;
		}
		return false;
	}


	public static void main(String[] args) {

		System.out.println(System.currentTimeMillis());
		Logger logger = new LogUtil(CheckTool.class.getCanonicalName())
				.getLogger();
		logger.info("这是测试例子");
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis());

	}

}
