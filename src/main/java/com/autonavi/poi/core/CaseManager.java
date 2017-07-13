package com.autonavi.poi.core;

import com.autonavi.poi.domain.casex.BaseCase;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingshan.wqs on 2016/11/13.
 */
@Component
public class CaseManager {

    private List<BaseCase> elements = new ArrayList<BaseCase>();

    public void attach(BaseCase element){
        elements.add(element);
    }

    public void detach(BaseCase element){
        elements.remove(elements);
    }

    //遍历各种具体元素并执行他们的accept方法

    public void execute(ReflectionVisitor visitor){
        for(BaseCase p:elements){
            p.accept(visitor);
        }
    }
}
