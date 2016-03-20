package com.photo.grap.photograp.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLConnection;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.photo.grap.photograp.util.MysqlConnector;
import com.photo.grap.photograp.util.ProxyContainer;

/**
 * tao bao product request
 */
public class TaoBaoRequestTool {

	static String url = "https://s.taobao.com/search?q=%s";
	private static Logger logger = Logger.getLogger(TaoBaoRequestTool.class);

	protected static String getPhotoUrl(String productid) {
		
		CloseableHttpClient client = null;
		int sign = MysqlConnector.getProxyAutoId(productid);
		Map<String, String> map = ProxyContainer.getProxy(sign);
		if(map==null )
			return "";
		logger.info("获取代理ip:"+map.get("ip")+"获取端口:"+map.get("port"));;
		HttpHost proxy = new HttpHost(map.get("ip"), Integer.valueOf(map
				.get("port")));
		
		client = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(10*1000).
				setConnectTimeout(10*1000).setSocketTimeout(10*1000).setProxy(proxy).build();
		
		String tempUrl = String.format(url, productid);
		HttpGet get = new HttpGet(tempUrl);
		get.setConfig(requestConfig);
		
		get.addHeader(":host", "s.taobao.com");
		get.addHeader(":method", "GET");	
		get.addHeader("accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		get.addHeader("accept-language", "zh-CN,zh;q=0.8");
		get.addHeader(":scheme", "https");
		get.addHeader("cache-control", "max-age=0");
		get.addHeader(
				"user-agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36");
		String returnBack = "";
		StringReader sreader = null;
		long startTime = System.currentTimeMillis();
		try {
			logger.info("执行开始时间 ：" + startTime);
			HttpResponse resposne = client.execute(get);

			if (resposne.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(resposne.getEntity(),
						"utf-8");
				sreader = new StringReader(result);
				BufferedReader breader = new BufferedReader(sreader);
				String line = "";
				while ((line = breader.readLine()) != null) {
					if (line.contains("g_page_config ")) {
						line = line.trim();
						returnBack = line.substring(line.indexOf("=") + 1,
								line.length() - 1);
						break;
					}
				}
			}else{
				MysqlConnector.updateProductStatus("A",productid);
			}
		} catch (Exception e) {
			MysqlConnector.updateProductStatus("A",productid);
			e.printStackTrace();
			
		} finally {
			logger.info("执行结束时间：" + (System.currentTimeMillis() - startTime)
					/ 1000.0);
			if (sreader != null) {
				sreader.close();
				get.completed();
			}
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnBack;
	}

	public static void main(String[] args) {
		getPhotoUrl("642199-505");
	}

}
