package com.autonavi.poi.core;


import com.autonavi.poi.domain.casex.BaseCase;
import com.autonavi.poi.runner.CaseRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by qingshan.wqs on 2016/11/10.
 */
@Slf4j
@Component
public class ReflectionVisitorImpl implements ReflectionVisitor {
    //用组合/聚合的方式代替继承
    private CaseRunner caseRunner;

    public ReflectionVisitorImpl(CaseRunner caseRunner){
        if(caseRunner == null){
            throw new NullPointerException("无用例执行器");
        }
        this.caseRunner = caseRunner;
    }

    public void visit(BaseCase baseCase) {
        // 判断是否是null
        if (baseCase == null) {
            throw new NullPointerException("执行的用例对象为NULL");
        }
        // 组装class数组，即调用动态方法的时候参数的类型
        Class[] classes = new Class[] { baseCase.getClass() };
        // 组装与class数组相对应的值
        Object[] objects = new Object[] { baseCase };
        try {
            // 查找方法
            Method m = caseRunner.getClass().getMethod("visit", classes);
            // 调用该方法
            m.invoke(caseRunner, objects);
        } catch (NoSuchMethodException e) {
            // 没有找到相应的方法
            log.error("执行器"+getClass().getName()+"未实现" + baseCase.getClass().getName() + "的执行方法",e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
