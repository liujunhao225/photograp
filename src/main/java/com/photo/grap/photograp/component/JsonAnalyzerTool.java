package com.photo.grap.photograp.component;

import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

public class JsonAnalyzerTool {

	protected static List<Map<String, Object>> deal(String json) {
		if(json==null || json.length()<1)
			return null;
		ReadContext ctx = JsonPath.parse(json);
		List<Map<String, Object>> list = ctx
				.read("$..itemlist.data.auctions.*");

		return list;
	}
}
