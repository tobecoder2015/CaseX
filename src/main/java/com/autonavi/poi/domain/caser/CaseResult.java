package com.autonavi.poi.domain.caser;

import com.autonavi.poi.domain.contant.CaseLevelEnum;
import com.autonavi.poi.domain.contant.CaseResultEnum;
import com.autonavi.poi.domain.contant.CaseTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by qingshan.wqs on 2017/1/15.
 */
@Data
@Document(collection = "caseResult")
public class CaseResult {
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
