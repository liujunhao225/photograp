package com.photo.grap.photograp.component;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;


public class TaoBaoPictureTool {
	private static Logger logger = Logger.getLogger(TaoBaoPictureTool.class
			.getCanonicalName());

	public static String[] getPicture(String param) {
		
		logger.info("【淘宝图片控制器】开始");
		List<Map<String, Object>> productList = JsonAnalyzerTool
				.deal(TaoBaoRequest.getPhotoUrl(param));
		String[] results = new String[50];
		int index = 0;
		if(productList ==null ||productList.size()<1){
			return null;
		}
		for (Map<String, Object> map : productList) {
			String title = Jsoup.parse(map.get("title").toString()).text();
			String raw_title = map.get("raw_title").toString();
			if (CheckTool.checkTitle(title, param)
					&& CheckTool.checkTitle(raw_title, param)) {
				String picture = map.get("pic_url").toString();
				results[index] = picture;
				index = index + 1;
			}
		}
		logger.info("【淘宝图片控制器】结束");
		return results;
	}

}
