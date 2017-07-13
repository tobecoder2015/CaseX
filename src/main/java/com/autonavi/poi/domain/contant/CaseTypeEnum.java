package com.autonavi.poi.domain.contant;

/**
 * Created by qingshan.wqs on 2016/11/13.
 */
public enum CaseTypeEnum implements IEnum {
    接口用例(0),
    流程用例(1);
    int value;
    CaseTypeEnum(int value){
        this.value=value;
    }


    @Override
    public int value() {
        return value;
    }
}
