package com.photo.grap.photograp.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * check if the title contains product id
 * 
 * @author liujunhao
 *
 */
public class CheckTool {

	/**
	 * 
	 * @param taobao
	 *            's picture title
	 * @param productid
	 * @return
	 */
	protected static boolean checkTitle(String source, String text) {
		Pattern pattern = Pattern
				.compile("[a-zA-Z]{0,20}\\d{1,20}[\\s\\-]{0,10}");
		// Pattern pattern = Pattern.compile("[a-zA-Z\\d\\s\\-]{4,20}");
		Matcher matcher = pattern.matcher(source);
		if (text.equals("Q38635")) {
			System.out.println("kaldjfalkdfj ");
		}
		String tempValue = "";
		while (matcher.find()) {
			tempValue = matcher.group().trim();
			if (text.equals(tempValue)) {
				return true;
			}

		}
		if (source.contains(text)) {
			return true;
		}
		return false;
	}
}
