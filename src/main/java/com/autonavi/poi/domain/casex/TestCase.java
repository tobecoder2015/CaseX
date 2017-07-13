package com.autonavi.poi.domain.casex;

import com.autonavi.poi.domain.contant.CaseLevelEnum;
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
@Document(collection = "casex")
public class TestCase extends BaseCase {
    @Id
    protected  String id;
    protected String groupName;
    protected String moduleName;
    protected String projectName;

    protected String caseName;
    protected String caseAuthor;
    protected String caseDesc;
    protected Integer caseLevel= CaseLevelEnum.一般.value();
    protected Integer casePriority= 5;

    protected Integer caseType= CaseTypeEnum.接口用例.value();

    protected Date createTime=new Date();
    protected Date updateTime=new Date();

    @JsonIgnoreProperties(ignoreUnknown = true)
    protected CaseContent caseContent;

    @JsonIgnoreProperties(ignoreUnknown = true)
    protected VerifyContent verifyContent;

    protected boolean active=true;
    protected Integer deleteFlag=0;

    //是否可以并行88
    protected boolean parallel=false;


}
