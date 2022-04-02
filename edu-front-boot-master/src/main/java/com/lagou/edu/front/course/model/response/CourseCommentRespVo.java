package com.lagou.edu.front.course.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCommentRespVo {
    @ApiModelProperty("评论IDS")
    private String id;
    @ApiModelProperty("课程Id")
    private Integer courseId;
    @ApiModelProperty("用户ID")
    private Integer userId;
    @ApiModelProperty("评论内容")
    private String comment;
    @ApiModelProperty("是否自己的评论")
    private boolean isOwner = false;
    @ApiModelProperty("用户昵称")
    private String nickName;
    @ApiModelProperty("是否是本人点赞")
    private boolean favoriteTag;
    @ApiModelProperty("点赞数量")
    private Integer likeCount;
}