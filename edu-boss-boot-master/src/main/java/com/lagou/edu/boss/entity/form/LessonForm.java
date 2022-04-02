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
 * Date:     2020/6/27 18:39
 * Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("课时信息")
public class LessonForm implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("课时Id")
    private Integer id;
    @ApiModelProperty("课程id")
    private Integer courseId;

    @ApiModelProperty("章节id")
    private Integer sectionId;

    @ApiModelProperty("课时主题")
    private String theme;

    @ApiModelProperty("课时时长(分钟)")
    private Integer duration;

    @ApiModelProperty("是否免费")
    private Boolean isFree;


    @ApiModelProperty("排序字段")
    private Integer orderNum;

    @ApiModelProperty("课时状态,0-隐藏，1-未发布，2-已发布")
    private Integer status;


}
