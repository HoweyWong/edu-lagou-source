package com.lagou.edu.boss.entity.form;

import com.lagou.edu.course.api.dto.TeacherDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Author:   mkp
 * Date:     2020/6/21 9:56
 * Description: 课程请求对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("课程更新对象")
public class CourseUpdateForm  implements Serializable {
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


    @ApiModelProperty("课程排序，用于后台保存草稿时用到")
    private Integer sortNum;


    @ApiModelProperty("课程预览第一个字段")
    private String previewFirstField;

    @ApiModelProperty("课程预览第二个字段")
    private String previewSecondField;
}
