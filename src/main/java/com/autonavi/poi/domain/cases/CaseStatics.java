package com.autonavi.poi.domain.cases;

import com.autonavi.poi.domain.contant.CaseResultEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by qingshan.wqs on 2017/1/15.
 */
@Data
@Document(collection = "caseStatics")
public class CaseStatics {
    @Id
    protected  String id;
    protected  String caseId;
    protected String caseName;
    protected String runner;
    protected String version;
    protected Integer caseResult= CaseResultEnum.未执行.value();
    protected Date runTime=new Date();
    private Long lastTime;
    private String msg;
    private String exception;

}
