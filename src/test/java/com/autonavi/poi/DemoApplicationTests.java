package com.autonavi.poi;

import com.autonavi.poi.http.BaseHttpSender;
import com.autonavi.poi.http.RequestType;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DemoApplicationTests {

	@Resource
	CloseableHttpClient httpClient;

	@Resource
	CloseableHttpAsyncClient asyncClient;

	@Resource
	BaseHttpSender baseHttpSender;

	@Test
	public void contextLoads() throws Exception{
		try {
			HttpGet request = new HttpGet("http://www.apache.org/");
			Future<HttpResponse> future = asyncClient.execute(request, null);
			HttpResponse response = future.get();// 获取结果
			log.info("Response: " + response.getStatusLine());
			log.info("Shutting down");
		}finally {
			httpClient.close();
		}

		try {
			HttpGet request = new HttpGet("http://www.apache.org/");
			Future<HttpResponse> future = asyncClient.execute(request, null);
			HttpResponse response = future.get();// 获取结果
			log.info("Response: " + response.getStatusLine());
			log.info("Shutting down");
		}finally {
			httpClient.close();
		}
		log.info("Done");
	}




}
