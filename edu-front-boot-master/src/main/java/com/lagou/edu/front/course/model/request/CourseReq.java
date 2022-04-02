package com.lagou.edu.front.course.model.request;

import com.lagou.edu.course.api.dto.LessonDTO;
import com.lagou.edu.course.api.dto.SectionDTO;
import com.lagou.edu.course.api.dto.TeacherDTO;
import com.lagou.edu.front.common.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Author:   mkp
 * Date:     2020/6/21 9:56
 * Description: 课程请求对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("课程请求对象")
public class CourseReq implements BaseBean {
    @ApiModelProperty("课程ID")
    private Integer id;
    @ApiModelProperty("课程名")
    private String courseName;

    @ApiModelProperty("课程一句话简介")
    private String brief;

    @ApiModelProperty("讲师id")
    private Integer teacherId;

    @ApiModelProperty("课时数")
    private Integer totalCourseTime;

    @ApiModelProperty("显示销量")
    private Integer sales;

    @ApiModelProperty("真实销量")
    private Integer actualSales;

    @ApiModelProperty("原价")
    private Double price;

    @ApiModelProperty("原价标签")
    private String priceTag;

    @ApiModelProperty("优惠价")
    private Double discounts;

    @ApiModelProperty("优惠标签")
    private String discountsTag;

    @ApiModelProperty("课程描述")
    private String courseDescription;

    @ApiModelProperty("课程分享图片url")
    private String courseImgUrl;

    @ApiModelProperty("是否新品")
    private Boolean isNew;

    @ApiModelProperty("广告语")
    private String isNewDes;

    @ApiModelProperty("最后操作者")
    private Integer lastOperatorId;

    @ApiModelProperty("记录创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    @ApiModelProperty("总时长(分钟)")
    private Integer totalDuration;




    @ApiModelProperty("分享title")
    private String shareTitle;

    @ApiModelProperty("分享描述")
    private String shareDescription;

    @ApiModelProperty("分享图title")
    private String shareImageTitle;


    @ApiModelProperty("课程列表展示图片")
    private String courseListImg;

    @ApiModelProperty("课程状态，0-草稿，1-上架")
    private Integer status;

    @ApiModelProperty("课程排序，用于后台保存草稿时用到")
    private Integer sortNum;



    @ApiModelProperty("最后课程最近通知时间")
    private LocalDateTime lastNoticeTime;

    @ApiModelProperty("课程预览第一个字段")
    private String previewFirstField;

    @ApiModelProperty("课程预览第二个字段")
    private String previewSecondField;

    @ApiModelProperty("分销海报图片URL")
    private String distributionPosterImage;


    @ApiModelProperty("课程中关模块")
    private List<SectionDTO> sectionDTOS;

    @ApiModelProperty("讲师")
    private TeacherDTO teacherDTO;


    @ApiModelProperty("播放地址")
    private String courseUrl;
    @ApiModelProperty("课时列表")
    private List<LessonDTO> topNCourseLesson;
    @ApiModelProperty("是否购买")
    private Boolean isBuy = false;

    @ApiModelProperty("课程更新数量")
    private Integer lessonUpdateCount;
    @ApiModelProperty("待比较时间  最近播放时间")
    private Date compareTime;
    @ApiModelProperty("最后学习的课时名称")
    private String lastLearnLessonName;
}
