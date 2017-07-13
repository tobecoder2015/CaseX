package com.autonavi.poi.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WebConfig {

    @Value("${httpclient.connectTimeout}")
    private int connectTimeout;

    @Value("${httpclient.readTimeout}")
    private int readTimeout;

    @Value("${httpclient.maxConnection}")
    private int maxConnection;

    @Value("${httpclient.maxConnectionPerHost}")
    private int maxConnectionPerHost;

    @Bean(name = "asyncClient")
    public CloseableHttpAsyncClient asyncHttpClient() throws Exception {
        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT));
        cm.setMaxTotal(maxConnection);
        cm.setDefaultMaxPerRoute(maxConnectionPerHost);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(readTimeout)
                .build();
        CloseableHttpAsyncClient build = HttpAsyncClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(config)
                .build();
        build.start();
        return build;
    }


    @Bean(name = "httpClient")
    public CloseableHttpClient httpClient() throws Exception {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxConnection);
        cm.setDefaultMaxPerRoute(maxConnectionPerHost);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(readTimeout)
                .build();
        CloseableHttpClient build = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(config)
                .build();
        return build;
    }
}
