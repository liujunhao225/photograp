package com.photo.grap.photograp.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


public class TaoBaoRequest {

	static String url = "https://s.taobao.com/search?q=%s";
	private static Logger logger = Logger.getLogger(TaoBaoRequest.class);

	protected static String getPhotoUrl(String param) {

		CloseableHttpClient client = null;
		logger.info("没有使用代理 ");
		client = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(10 * 1000).setConnectTimeout(10 * 1000)
				.build();
		String tempUrl = String.format(url, param);
		HttpGet get = new HttpGet(tempUrl);
		get.setConfig(requestConfig);
		get.addHeader(":host", "s.taobao.com");
		get.addHeader(":method", "GET");
		get.addHeader("accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		get.addHeader("accept-language", "zh-CN,zh;q=0.8");
		get.addHeader(":scheme", "https");
		get.addHeader("cache-control", "max-age=0");
		get.addHeader("user-agent",
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
			}
		} catch (ClientProtocolException e) {
			logger.error("【采集网页】客户端异常:" + tempUrl);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("【采集网页】IO异常:" + tempUrl);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("【采集网页】未处理异常:" + tempUrl);
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


	public static synchronized String[] getProxyConfig() {
		String params[] = { "", "" };

		while (true) {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet get = new HttpGet(
					"http://vxer.daili666api.com/ip/?tid=559627164132075&num=1&protocol=https&filter=on");
			try {
				HttpResponse response = client.execute(get);
				if (response.getStatusLine().getStatusCode() == 200) {
					String ss = EntityUtils.toString(response.getEntity(),
							"utf-8");
					params = ss.split(":");
					if (testProxyIp(params[0],Integer.parseInt(params[1]))) {
						break;
					}
				}
			} catch (ClientProtocolException e) {
				System.out.println("【获取代理 】获取连接失败");
			} catch (IOException e) {
				System.out.println("【获取代理 】获取IP IO错误");
			}
		}
		return params;
		// String params[] ={"",""};
		// while(true){
		// Random rnd = new Random();
		// int randInt = rnd.nextInt(proxyList.size());
		// params[0] = proxyList.get(randInt).getAsString("ip");
		// params[1] = proxyList.get(randInt).getAsString("port");
		// if(testProxyIp(params[0],Integer.parseInt(params[1]))){
		// break;
		// }
		// }
		// return params;
	}

	/**
	 * test proxy ip port is suitable now
	 * 
	 * @param ip
	 * @param port
	 * @return
	 */
	private static boolean testProxyIp(String ip, int port) {
		Socket socket = new Socket();
		SocketAddress endpoint = new InetSocketAddress(ip, port);
		try {
			socket.connect(endpoint, 3 * 1000);
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		String[] params =getProxyConfig();
		System.out.println("get proxy ip is "+params[0]+" and port is "+params[1]);
	}

}
