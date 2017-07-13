package com.autonavi.poi.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



/**
 * Created by qingshan.wqs on 2016/11/13.
 */
@Data
@Document
public class Project {

    @Id
    private  String id;
    private String projectName;
    private String projectDesc;
    private String owner;

}
