package com.photo.grap.photograp.tool;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

/**
 * taobao picture prerequest
 * 
 * @author liujunhao
 *
 */
public class TaoBaoPictureTool {
	private static Logger logger = Logger.getLogger(TaoBaoPictureTool.class
			.getCanonicalName());

	public static String[] getPicture(String productid) {
		logger.info("【淘宝图片控制器】开始");
		List<Map<String, Object>> productList = JsonAnalyzerTool
				.deal(TaoBaoRequestTool.getPhotoUrl(productid));
		String[] results = new String[50];
		int index = 0;
		if (productList == null || productList.size() < 1) {
			return null;
		}
		for (Map<String, Object> map : productList) {
			String title = Jsoup.parse(map.get("title").toString()).text();
			String raw_title = map.get("raw_title").toString();
			if (CheckTool.checkTitle(title, productid)
					&& CheckTool.checkTitle(raw_title, productid)) {
				String picture = map.get("pic_url").toString();
				results[index] = picture;
				index = index + 1;
			}
		}
		logger.info("【淘宝图片控制器】结束");
		return results;
	}

}
