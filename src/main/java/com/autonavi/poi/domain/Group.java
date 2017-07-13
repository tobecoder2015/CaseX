package com.autonavi.poi.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qingshan.wqs on 2016/11/13.
 */
@Data
@Document
public class Group {

    @Id
    private String id;
    private String groupName;
    private String groupDesc;

    //基本地址 对于ApiCase 将作为基本地址进行请求  对于FlowCase 将作为方法调用的服务请求地址
    private String baseUrl;
    //补充基本参数，如果用例参数中没有，会补充到用例参数中
    private Map<String,Object> groupMap=new HashMap<>();

    //字典参数
    private Map<String,Object> dictionMap=new HashMap<>();;

    //排除参数
    private List<String> abandonList=new ArrayList<>();

    private String moduleName;
    private String projectName;

}
