package com.autonavi.poi.http;

public enum RequestMethod {

	GET("GET"), POST("POST"),PUT("PUT"), DELETE("DELETE");
	private String value;
	public String toString() {
		return value;
	};
	private RequestMethod(String value) {
		this.value=value;
	}

}
