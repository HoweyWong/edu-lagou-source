package com.lagou.edu.comment.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCommentFavoriteDTO {

    private String id;

    private Integer userId;

    private String commentId;

    private Boolean isDel;

    private Date createTime;

    private Date updateTime;
}