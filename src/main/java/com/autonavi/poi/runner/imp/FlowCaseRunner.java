package com.autonavi.poi.runner.imp;

import com.autonavi.poi.domain.casex.TestCase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by qingshan.wqs on 2016/11/10.
 */
@Slf4j
@Data
public class FlowCaseRunner extends BaseCaseRunnerImpl {

    @Override
    public void visit(TestCase flowCase) {
        log.info("流程用例执行");
    }
}
