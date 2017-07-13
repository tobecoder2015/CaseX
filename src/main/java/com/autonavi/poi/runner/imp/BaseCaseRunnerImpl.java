package com.autonavi.poi.runner.imp;

import com.autonavi.poi.domain.casex.TestCase;
import com.autonavi.poi.runner.CaseRunner;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by qingshan.wqs on 2016/11/10.
 */
@Slf4j
public abstract class BaseCaseRunnerImpl implements CaseRunner {

    private  static  final String UnImplementedRunnerMsg="没有实现该用例类型执行器";
    @Override
    public void visit(TestCase apiCase){
        log.warn(UnImplementedRunnerMsg);
    }


}
