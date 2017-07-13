package com.autonavi.poi.controller.model;

import lombok.Data;

/**
 * Created by qingshan.wqs on 2016/12/27.
 */
@Data
public class Response {
    private boolean success=true;
    private String message="";
    private Object data;
}
