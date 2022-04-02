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
@Document(collection = "course_comment")
public class CourseComment {

    @Id
    private String id;

    private Integer courseId;




    @Indexed
    private Integer userId;



    private Integer likeCount;


    private Date createTime;

    private Date updateTime;

    private Boolean isDel;


    private String comment;

}