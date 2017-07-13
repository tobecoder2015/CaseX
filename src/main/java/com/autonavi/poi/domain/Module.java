package com.autonavi.poi.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Created by qingshan.wqs on 2016/11/13.
 */
@Data
@Document
public class Module {

    @Id
    private String id;
    private String moduleName;
    private String moduleDesc;
    private String projectName;

}
