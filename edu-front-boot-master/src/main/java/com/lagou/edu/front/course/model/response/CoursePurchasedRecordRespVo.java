package com.lagou.edu.front.course.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoursePurchasedRecordRespVo implements Serializable {
    @ApiModelProperty(value = "更新进度")
    private String updateProgress;
    @ApiModelProperty(value = "更新提醒")
    private String updateTips;
    @ApiModelProperty(value = "课时更新数")
    private int lessonUpdateNum;
    @ApiModelProperty(value = "课程Id")
    private Integer id;
    @ApiModelProperty(value = "课程名称")
    private String name;
    @ApiModelProperty(value = "最近学习的课时名称")
    private String lastLearnLessonName;
    @ApiModelProperty(value = "图片链接")
    private String image;
    @ApiModelProperty("课程预览第一个字段")
    String previewFirstField ;
    @ApiModelProperty("课程预览第二个字段")
    String previewSecondField;
}
