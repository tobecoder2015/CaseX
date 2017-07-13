package com.autonavi.poi.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by qingshan.wqs on 2016/11/10.
 */
@Data
@Document
public  class CaseResult {

    private String id;
    private String caseId;
    private String runner;
    private String version;
    private Date startTime;
    private Date finishTime;
    private Long lastTime;
    private boolean success;
    private String msg;
    private String exception;


}
