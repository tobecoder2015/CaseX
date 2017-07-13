package com.autonavi.poi.runner.imp;


import com.autonavi.poi.core.InvokeApi;
import com.autonavi.poi.core.ParaHandler;
import com.autonavi.poi.domain.CaseResult;
import com.autonavi.poi.domain.casex.TestCase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by qingshan.wqs on 2016/11/10.
 */
@Slf4j
@Data
@Component
public class ApiCaseRunner extends BaseCaseRunnerImpl {


    @Override
    public void visit(TestCase apiCase) {
        log.info("简单用例执行");
        CaseResult caseResult=new CaseResult();
        caseResult.setRunner("WQS");

        try {
            ParaHandler.preTreat(apiCase);
            caseResult.setStartTime(new Date());
            Object object = InvokeApi.invokeApi(apiCase);
//            simpleCheck.eq(apiCase.getVerifyMap(), object);

        } catch (Exception e) {
            log.error("用例执行失败", e);
        }
    }
}
