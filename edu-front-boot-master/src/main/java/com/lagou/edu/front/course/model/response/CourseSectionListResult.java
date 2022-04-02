package com.lagou.edu.front.course.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CourseSectionListResult {
    @ApiModelProperty("是否已购买")
    private boolean hasBuy=false;
    @ApiModelProperty("课程名称")
    private String courseName;
    @ApiModelProperty("课程封面图")
    private String coverImage;
    @ApiModelProperty("课程章节列表")
    List<CourseSectionRespVo> courseSectionList;
}
