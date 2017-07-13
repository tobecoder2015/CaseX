package com.autonavi.poi.domain.contant;

/**
 * Created by qingshan.wqs on 2016/11/13.
 */
public enum CaseLevelEnum implements IEnum {
    冒烟(0),
    核心(1),
    一般(2),
    详细(3);
    int value;
    CaseLevelEnum(int value){
        this.value=value;
    }


    @Override
    public int value() {
        return value;
    }
}
