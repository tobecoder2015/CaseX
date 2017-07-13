package com.autonavi.poi.controller.model;

import lombok.Data;

import java.util.List;

/**
 * @author zhongyi.sun
 * @since 2016/9/19
 */
@Data
public class Page<T> {

    private List<T> data;

    private long total;

    private int perPage;

    private int currentPage;

    private int lastPage;

    private int from;

    private int to;
}
