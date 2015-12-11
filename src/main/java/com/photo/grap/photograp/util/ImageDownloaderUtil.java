package com.photo.grap.photograp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.photo.grap.photograp.component.TaoBaoRequest;

public class ImageDownloaderUtil {

	private static Logger logger = Logger.getLogger(ImageDownloaderUtil.class);

	public static void downPictureToInternet(String filePath, String strUr1)
			throws Exception {

		File file = new File(filePath);
		if (file.exists()) {
			return;
		}
		HttpClient client = null;
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20*000).setConnectTimeout(20*000).build();
			client = HttpClients.custom().build();
		HttpGet get = new HttpGet(strUr1);
		get.setConfig(requestConfig);
		get.addHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		get.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36");
		HttpResponse response = client.execute(get);
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream fstream = response.getEntity().getContent();
			int b = 0;
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			while ((b = fstream.read()) != -1) {
				fos.write(b);
			}
			fstream.close();
			fos.close();
			logger.info("【下载图片】成功，图片:" + filePath);
		} else {
			logger.error("【下载图片】失败，图片:" + filePath);
		}
		get.releaseConnection();
	}

	public static void main(String[] args) {
		try {
			ImageDownloaderUtil
					.downPictureToInternet(
							"d://test.jpg",
							"http://g-search3.alicdn.com//img/bao//uploaded//i4//i1//TB1fYlCJXXXXXXIaFXXXXXXXXXX_!!0-item_pic.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
