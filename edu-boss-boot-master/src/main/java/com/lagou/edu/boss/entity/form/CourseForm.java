package com.lagou.edu.boss.entity.form;

import com.lagou.edu.course.api.dto.ActivityCourseDTO;
import com.lagou.edu.course.api.dto.TeacherDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:   mkp
 * Date:     2020/6/21 9:56
 * Description: 课程请求对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("课程保存对象")
public class CourseForm implements Serializable {
    @ApiModelProperty("课程ID")
    private Integer id;
    @ApiModelProperty("课程名")
    private String courseName;

    @ApiModelProperty("课程一句话简介")
    private String brief;
    @ApiModelProperty("讲师信息")
    TeacherDTO teacherDTO;

    @ApiModelProperty("课程描述")
    private String courseDescriptionMarkDown;

    @ApiModelProperty("原价")
    private Double price;


    @ApiModelProperty("优惠价")
    private Double discounts;

    @ApiModelProperty("原价标签")
    private String priceTag;


    @ApiModelProperty("优惠标签")
    private String discountsTag;

    @ApiModelProperty("是否新品")
    private Boolean isNew;

    @ApiModelProperty("广告语")
    private String isNewDes;


    @ApiModelProperty("课程列表展示图片")
    private String courseListImg;

    @ApiModelProperty("解锁封面")
    private String courseImgUrl;


    @ApiModelProperty("课程排序，用于后台保存草稿时用到")
    private Integer sortNum;


    @ApiModelProperty("课程预览第一个字段")
    private String previewFirstField;

    @ApiModelProperty("课程预览第二个字段")
    private String previewSecondField;

    @ApiModelProperty("课程状态，0-草稿，1-上架")
    private Integer status;
    @ApiModelProperty("显示销量")
    private Integer sales;
    
    @ApiModelProperty("课程是否做秒杀活动 true是 false不是")
    private Boolean activityCourse;
    
    @ApiModelProperty("活动课程信息")
    private ActivityCourseDTO activityCourseDTO;

    @ApiModelProperty("自动上架时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date autoOnlineTime;

}


