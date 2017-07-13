package com.autonavi.poi.http.api;


import com.autonavi.poi.http.BaseHttpSender;
import com.autonavi.poi.http.RequestType;
import com.autonavi.poi.http.SenderContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 
 * @author qingshan.wqs
 *
 */
@Slf4j
@Component
public   class BaseApi implements IApi {


	public static  final RequestType isGet=RequestType.Get;
	public static  final RequestType isPost=RequestType.Post;
	public static  final RequestType isMulti=RequestType.Multipart;

	@Resource
	private BaseHttpSender httpSender;

	@Resource
	protected SenderContext senderContext;


	@Override
	public void setAppContext() {
		httpSender.setSenderContext(senderContext);
	}

	protected void setTransferCode(boolean transfer) {
			httpSender.tranferCode=transfer;
	}



	protected void putParam(String key, String value) {
		if (value != null) {
			httpSender.putParam(key, value);
		}
	}




	protected void putHeader(String key, String value) {
		if (value != null) {
			httpSender.putHeader(key, value);
		}
	}



	/**
	 *
	 * @param key
	 * @param filepath
	 */
	protected void putFile(String key, String filepath) {
		if (filepath != null) {
			httpSender.putFile(key, filepath);
		}
	}

	/**
	 * Put route parameters
	 * 
	 */
	protected void putRouteParam(String routeParam) {
		httpSender.putRouteParam(routeParam);
	}





}

