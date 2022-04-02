package com.lagou.edu.comment.service;

import com.lagou.edu.comment.entity.CourseComment;
import org.springframework.data.domain.Page;

public interface ICourseCommentService {
    Page<CourseComment> getCourseCommentList(Integer courseId,  int pageNum, int pageSize);


    boolean saveCourseComment(CourseComment courseComment);


    boolean updateCommentDelStatusByIdAndUserId(String commentId, Integer userId);




    /**
     * 更新点赞数量 flag true 增加点赞  false去掉点赞
     * @param commentId
     * @param flag
     */
    void updateNumOfLike(String commentId,Boolean flag);
}
