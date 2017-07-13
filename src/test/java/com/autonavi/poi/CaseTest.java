package com.autonavi.poi;

import com.autonavi.poi.core.CaseManager;
import com.autonavi.poi.core.ReflectionVisitor;
import com.autonavi.poi.core.ReflectionVisitorImpl;
import com.autonavi.poi.domain.casex.TestCase;
import com.autonavi.poi.runner.CaseRunner;
import com.autonavi.poi.runner.imp.ApiCaseRunner;
import com.autonavi.poi.runner.imp.FlowCaseRunner;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by qingshan.wqs on 2016/11/24.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DemoApplication.class)
public class CaseTest {


    @org.junit.Test
    public void test2() throws Exception {
        CaseManager o = new CaseManager();  //依赖于ObjectStructure
        //实例化具体元素
        TestCase apiCase=new TestCase();
        apiCase.setCaseDesc("测试Mock接口");
        o.attach(apiCase);


        CaseRunner apiCaseRunner = new ApiCaseRunner();
        ReflectionVisitor visitor = new ReflectionVisitorImpl(apiCaseRunner);
        o.execute(visitor);

        CaseRunner flowCaseRunner = new FlowCaseRunner();
        visitor = new ReflectionVisitorImpl(flowCaseRunner);
        o.execute(visitor);
    }

}
