package com.autonavi.poi.http;


import java.util.List;
import java.util.Map;

/**
 * 
 * @author qingshan.wqs
 *
 */
public interface IHttpSender {

	String invoke(String sUrl, RequestType requestType)
			throws Exception;

	String invokeAnysc(String sUrl, RequestType requestType)
			throws Exception;
}
