package com.autonavi.poi.domain.contant;

/**
 * Created by qingshan.wqs on 2016/11/13.
 */
public enum CaseResultEnum implements IEnum {
    未执行(0),
    通过(1),
    失败(2),
    异常(3);
    int value;
    CaseResultEnum(int value){
        this.value=value;
    }


    @Override
    public int value() {
        return value;
    }
}
