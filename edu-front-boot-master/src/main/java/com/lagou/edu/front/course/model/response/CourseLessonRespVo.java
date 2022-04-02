package com.lagou.edu.front.course.model.response;

import com.lagou.edu.course.api.enums.CourseLessonStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CourseLessonRespVo {
    @ApiModelProperty("课时ID")
    private Integer id;
    @ApiModelProperty("课程ID")
    private Integer courseId;
    @ApiModelProperty("章节ID")
    private Integer sectionId;
    @ApiModelProperty("课时主题")
    private String theme;
    private String duration;

    @ApiModelProperty("课时是否可播放")
    private boolean canPlay=false;

    //视频媒体信息
    @ApiModelProperty("视频媒体信息")
    private CourseMediaRespVo videoMediaDTO;
    @ApiModelProperty("文本内容")
    private String textContent;
    //课时状态
    @ApiModelProperty("课时状态:UNRELEASE-待更新,RELEASE-已发布")
    private Integer status;

    @ApiModelProperty("是否有视频 v1.2")
    private boolean hasVideo = false;

    @ApiModelProperty("是否学习过 v1.2")
    private boolean hasLearned = false;

    @ApiModelProperty("文稿页URL v1.2")
    private String textUrl;
}
