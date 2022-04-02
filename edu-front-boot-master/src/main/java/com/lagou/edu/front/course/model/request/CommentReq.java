package com.lagou.edu.front.course.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Author:   mkp
 * Date:     2020/7/9 12:26
 * Description: 课程评论
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("课程评论")
public class CommentReq   implements Serializable {
    @ApiModelProperty("课程Id")
    private Integer courseId;
    @ApiModelProperty("课程评论")
    private String comment;
}
