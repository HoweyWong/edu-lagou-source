package com.lagou.edu.front.course.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AllCoursePurchasedRecordRespVo {
    @ApiModelProperty(value = "模块标题")
    private String title;

    @ApiModelProperty(value = "专栏或视频课购买记录")
    private List<CoursePurchasedRecordRespVo> courseRecordList;
}
