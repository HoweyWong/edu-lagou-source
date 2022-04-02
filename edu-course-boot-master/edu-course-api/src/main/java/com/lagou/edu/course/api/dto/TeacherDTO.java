package com.lagou.edu.course.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class TeacherDTO implements Serializable {

    private Integer id;
    /**
     * 课程Id
     */
    private Integer courseId;

    /**
     * 讲师姓名
     */
    private String teacherName;

    /**
     * 讲师头像url地址
     */
    private String teacherHeadPicUrl;

    /**
     * 讲师职位
     */
    private String position;

    /**
     * 讲师描述
     */
    private String description;

}
