package com.lagou.edu.boss.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Author:   mkp
 * Date:     2020/6/27 18:07
 * Description: 章节内容
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("章节")
@Data
public class SectionForm implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("章节ID")
    private Integer id;
    @ApiModelProperty("课程id")
    private Integer courseId;
    @ApiModelProperty("课程名称")
    private String  courseName;

    @ApiModelProperty("章节名")
    private String sectionName;

    @ApiModelProperty("章节描述")
    private String description;
    @ApiModelProperty("排序字段")
    private Integer orderNum;


    @ApiModelProperty("状态，0:隐藏；1：待更新；2：已发布")
    private Integer status;

}
