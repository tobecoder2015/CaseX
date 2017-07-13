package com.autonavi.poi.domain.casex;

import com.autonavi.poi.http.RequestType;
import lombok.Data;

import java.util.Map;

/**
 * Created by qingshan.wqs on 2017/1/15.
 */
@Data
public class ApiCaseContent extends CaseContent {
    private String paraMap;
    private String fileMap;
    private String headMap;
    private String requstType;
    private String routeParaList;
    private String  path;
}
