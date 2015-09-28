package com.photo.grap.photograp.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class TaoBaoRequest {
	public static HttpClient client = HttpClients.createDefault();
	static String url = "https://s.taobao.com/search?q=%s";

	private static Logger logger = Logger.getLogger(TaoBaoRequest.class);

	protected static String getPhotoUrl(String param) {
//		BasicConfigurator.configure();
		String tempUrl = String.format(url, param);
		HttpGet get = new HttpGet(tempUrl);
		get.addHeader(":host", "s.taobao.com");
		get.addHeader(":method", "GET");
		get.addHeader(":path", "/search?q=544976-014");
		get.addHeader("accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//		get.addHeader("accept-encoding", "gzip, deflate, sdch");
		get.addHeader("accept-language", "zh-CN,zh;q=0.8");
		get.addHeader(":scheme", "https");
		get.addHeader("cache-control", "max-age=0");
		get.addHeader(
				"cookie",
				"thw=cn; cna=R0tcDvPkmWsCAdrOyQNWayLZ; t=165be6f92e26867c30d628d79f3cd6be; JSESSIONID=0F48DB1936C5305A5C0FE8A6D5D55716; mt=ci%3D-1_0; l=Ajg4VjokVlu8XaAfIpnhEfk0iOjKoZwr; isg=6A20A0A901B20A056E0699F1AF50FCFE");
		get.addHeader("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36");
		String returnBack = "";
		StringReader sreader = null;
		try {
			HttpResponse resposne = client.execute(get);
			String result = EntityUtils.toString(resposne.getEntity(), "utf-8");
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
		} catch (ClientProtocolException e) {
			logger.error("【采集网页】客户端异常");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("【采集网页】IO异常");
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("【采集网页】未处理异常");
			e.printStackTrace();
		} finally {
			if (sreader != null) {
				sreader.close();
				get.completed();
			}
		}
		return returnBack;
	}
	
	public static void main(String[] args) {
		System.out.println(TaoBaoRequest.getPhotoUrl("709009-664"));
	}

}