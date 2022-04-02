package com.lagou.edu.course.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PageResultDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<T> records;
    private long total;
    private long size;
    private long current;
    private long pages;


}
