package com.autonavi.poi.controller.model;

import lombok.Data;

/**
 * @author zhongyi.sun
 * @since 2016/9/20
 */
@Data
public class SearchCondition {

    private String projectName;

    private String moduleName;

    private String groupName;
    private String caseAuthor;

    private Integer caseLevel;

    private Integer deleteFlag=0;

    private String sort;

    private boolean asc;

}
