package com.lagou.edu.front.course.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class CourseSectionRespVo {
    @ApiModelProperty("课程章节ID")
    private int id;
    @ApiModelProperty("课程ID")
    private Integer courseId;
    @ApiModelProperty("章节名称")
    private String sectionName;
    @ApiModelProperty("章节描述")
    private String description;
    @ApiModelProperty("0:隐藏；1：待更新；2：已发布")
    private Integer status;
    @ApiModelProperty("课程章节下的课时列表")
    private List<CourseLessonRespVo> courseLessons = new LinkedList<>();


}
