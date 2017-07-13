package com.autonavi.poi.domain;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by qingshan.wqs on 2016/11/13.
 */
@Data
public class CheckResult {
    private String title;
    private  boolean success=true;
    private  Object refData;
    private Object checkData;
   private String msg;
    private String exceptionMsg;

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        if(StringUtils.isNotBlank(title))
            sb.append("\n-主题 :"+title);
        if(StringUtils.isNotBlank(msg))
            sb.append("\n-信息 :"+msg);
        if(StringUtils.isNotBlank(exceptionMsg))
            sb.append("\n-异常信息 :"+exceptionMsg);
        return sb.toString() + "\n-参考值 :"+this.getRefData()+" \n-目标值 :"+this.getCheckData();
    }
}
