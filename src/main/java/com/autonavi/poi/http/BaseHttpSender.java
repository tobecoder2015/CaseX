package com.autonavi.poi.http;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author qingshan.wqs
 *
 */
@Slf4j
@Component
@Scope("prototype")
public  class BaseHttpSender implements IHttpSender {

	@Resource
	CloseableHttpClient httpClient;

	@Resource
	CloseableHttpAsyncClient asyncClient;

	protected Map<String, String> params = new HashMap<String, String>();
	protected Map<String, String> allParams = new HashMap<String, String>();
	protected Map<String, String> fileParams = new HashMap<String, String>();
	protected Map<String, String> headerMap = new HashMap<String, String>();

	protected List<String> routeParams = new ArrayList<>(); // for MVC pattern route param

	protected SenderContext senderContext; 
	


    public static boolean tranferCode=true;

	public static boolean proxyMode =true;


	public static  void setProxyMode(boolean enableProxyMode){
		proxyMode=enableProxyMode;
		log.info((enableProxyMode?"开启":"关闭")+"代理");
	}



	public SenderContext getSenderContext() {
		return senderContext;
	}

	public void setSenderContext(SenderContext senderContext) {
		this.senderContext = senderContext;
	}



	public void putParam(String key, String value) {
		if (value != null) {
			this.params.put(key, value);
		}
	}

	public void putHeader(String key, String value) {
		if (value != null) {
			headerMap.put(key, value);
		}
	}


	public void putFile(String key, String filepath) {
		if (filepath != null) {
			this.fileParams.put(key, filepath);
		}
	}

	/**
	 * Put route parameters
	 * 
	 */
	public void putRouteParam(String routeParam) {
		routeParams.add(routeParam);
	}






	/**
	 *
	 * @param url  请求url
	 * @param requestType  请求类型
	 * @return
	 * @throws Exception
	 */
	public String invoke(String url, RequestType requestType) throws Exception {


		if (params.size() != 0)
			allParams.putAll(params);


		//添加基本地址
		if(!url.startsWith("http"))
		url = senderContext.getBaseUrl() + url;

		//处理url 路由参数
		url = addRouteParams(url);

		// 发送请求
		String resultString = null;




			switch (requestType) {
				case Post:
					HttpPost httpPost = new HttpPost(url);

					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					if (allParams != null) {
						for (Entry<String, String> entry : allParams.entrySet()) {
							nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
						}
					}

					if (fileParams != null) {
						for (Entry<String, String> entry : allParams.entrySet()) {
							nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
						}
					}

					//设置参数到请求对象中
					httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));


					if (senderContext!=null&&senderContext.getCookie() != null)
						httpPost.setHeader("cookie", senderContext.getCookie());

					if (headerMap != null) {
						for (Entry<String, String> entry : headerMap.entrySet()) {
							httpPost.setHeader(entry.getKey(), entry.getValue());
						}
					}

					try {
						//执行请求操作，并拿到结果（同步阻塞）
						CloseableHttpResponse response = httpClient.execute(httpPost);
						//获取结果实体
						HttpEntity entity = response.getEntity();
						if (entity != null) {
							//按指定编码转换结果实体为String类型
							resultString = EntityUtils.toString(entity, "UTF-8");
						}
						EntityUtils.consume(entity);
						//释放链接
						response.close();
					} finally {
						httpPost.releaseConnection();
					}
					break;

				case Get:
					String paramString = this.getQuery(allParams, tranferCode);

					HttpGet httpGet = new HttpGet(url + "?" + paramString);
					try {
						if (senderContext!=null&&senderContext.getCookie() != null) {
							httpGet.addHeader("Cookie", senderContext.getCookie());
						}
						if (headerMap != null) {
							for (Entry<String, String> entry : headerMap.entrySet()) {
								httpGet.addHeader(entry.getKey(), entry.getValue());
							}
						}
						CloseableHttpResponse response = httpClient.execute(httpGet);

						Header cookie = response.getFirstHeader("Set-Cookie");
						if (senderContext!=null&&cookie != null && cookie.getValue() != null) {

							senderContext.setCookie(cookie.getValue().substring(0, cookie.getValue().indexOf(";")));

						}

						HttpEntity entity = response.getEntity();
						if (entity != null) {
							//按指定编码转换结果实体为String类型
							resultString = EntityUtils.toString(entity, "UTF-8");
						}
						EntityUtils.consume(entity);
						//释放链接
						response.close();

					} finally {
						httpGet.releaseConnection();
					}
					break;
				default:
					break;
			}

		tranferCode = true;
		routeParams.clear();
		return resultString;
	}


	/**
	 *
	 * @param url  请求url
	 * @param requestType  请求类型
	 * @return
	 * @throws Exception
	 */
	public String invokeAnysc(String url, RequestType requestType) throws Exception {


		if (params.size() != 0)
			allParams.putAll(params);


		//添加基本地址
		if(!url.startsWith("http"))
			url = senderContext.getBaseUrl() + url;

		//处理url 路由参数
		url = addRouteParams(url);

		// 发送请求
		String resultString = null;




		switch (requestType) {
			case Post:
				HttpPost httpPost = new HttpPost(url);

				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				if (allParams != null) {
					for (Entry<String, String> entry : allParams.entrySet()) {
						nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
					}
				}

				if (fileParams != null) {
					for (Entry<String, String> entry : allParams.entrySet()) {
						nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
					}
				}

				//设置参数到请求对象中
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));


				if (senderContext!=null&&senderContext.getCookie() != null)
					httpPost.setHeader("cookie", senderContext.getCookie());

				if (headerMap != null) {
					for (Entry<String, String> entry : headerMap.entrySet()) {
						httpPost.setHeader(entry.getKey(), entry.getValue());
					}
				}

				try {
					Future<HttpResponse> future = asyncClient.execute(httpPost,null);

					HttpResponse response =future.get();
					//获取结果实体
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						//按指定编码转换结果实体为String类型
						resultString = EntityUtils.toString(entity, "UTF-8");
					}
					EntityUtils.consume(entity);
					//释放链接
				} finally {
					httpPost.releaseConnection();
				}
				break;

			case Get:
				String paramString = this.getQuery(allParams, tranferCode);

				HttpGet httpGet = new HttpGet(url + "?" + paramString);
				try {
					if (senderContext!=null&&senderContext.getCookie() != null) {
						httpGet.addHeader("Cookie", senderContext.getCookie());
					}
					if (headerMap != null) {
						for (Entry<String, String> entry : headerMap.entrySet()) {
							httpGet.addHeader(entry.getKey(), entry.getValue());
						}
					}
					Future<HttpResponse> future = asyncClient.execute(httpGet, null);
					HttpResponse response = future.get();// 获取结果
					Header cookie = response.getFirstHeader("Set-Cookie");
					if (senderContext!=null&&cookie != null && cookie.getValue() != null) {
						senderContext.setCookie(cookie.getValue().substring(0, cookie.getValue().indexOf(";")));
					}

					//获取结果实体
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						//按指定编码转换结果实体为String类型
						resultString = EntityUtils.toString(entity, "UTF-8");
					}
					EntityUtils.consume(entity);
					//释放链接
				} finally {
					httpGet.releaseConnection();
				}
				break;
			default:
				break;
		}

		tranferCode = true;
		routeParams.clear();
		return resultString;
	}


	protected String addRouteParams(String url) {
		final String routeParamPlaceHolder = "\\{\\w*\\}";
		Pattern pattern = Pattern.compile(routeParamPlaceHolder);
		Matcher matcher = pattern.matcher(url);
		int paramIndex = 0;
		while (matcher.find()) {
			url = url.replace(matcher.group(), routeParams.get(paramIndex++));
		}
		return url;
	}


	private  <T> String serializeToJson(Map<String, T> map) throws JsonGenerationException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(map);

	}

	private String getQuery(Map<String, String> params,boolean transferEncode) {
		if(params==null||params.size()==0)
			return "";
		StringBuilder resultBuilder = new StringBuilder();
		int i = 0;
		for (String key : params.keySet()) {
			resultBuilder.append(key + "=");
			try {
				if (params == null) {
					log.info("params == null");
				}

				if(transferEncode)
				resultBuilder.append(URLEncoder.encode(params.get(key), "UTF-8"));
				else
					resultBuilder.append(params.get(key));

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (++i < params.size()) {
				resultBuilder.append("&");

			}
		}
		return resultBuilder.toString();
	}
}

