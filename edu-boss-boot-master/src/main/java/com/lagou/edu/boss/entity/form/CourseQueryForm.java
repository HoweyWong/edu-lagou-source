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
 * Date:     2020/6/21 9:56
 * Description: 课程请求对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("课程查询对象")
public class CourseQueryForm  implements Serializable {
    @ApiModelProperty("课程Id")
    private String id;
    @ApiModelProperty("课程名")
    private String courseName;
    @ApiModelProperty("课程状态，0-草稿，1-上架")
    private Integer status;

}
