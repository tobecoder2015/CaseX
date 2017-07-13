package com.autonavi.poi;

import com.autonavi.poi.http.BaseHttpSender;
import com.autonavi.poi.http.RequestType;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by qingshan.wqs on 2016/11/24.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DemoApplication.class)
public class HttpTest {

    @Resource
    CloseableHttpClient httpClient;

    @Resource
    CloseableHttpAsyncClient asyncClient;

    @Resource
    BaseHttpSender baseHttpSender;

    @Test
    public void contextLoads2() throws Exception{
        try {
            String result=baseHttpSender.invoke("http://www.apache.org/", RequestType.Get);
            log.info("Response: " + result);
        }finally {
        }

        try {
            String result=baseHttpSender.invokeAnysc("http://www.apache.org/", RequestType.Get);
            log.info("Response: " + result);
        }finally {
            httpClient.close();
        }
        log.info("Done");
    }


}
