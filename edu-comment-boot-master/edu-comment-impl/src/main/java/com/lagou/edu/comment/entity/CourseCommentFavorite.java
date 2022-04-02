package com.lagou.edu.comment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "course_comment_favorite")
public class CourseCommentFavorite {

    @Id
    private String id;

    @Indexed
    private Integer userId;

    @Indexed
    private String commentId;

    private Boolean isDel;

    private Date createTime;

    private Date updateTime;
}