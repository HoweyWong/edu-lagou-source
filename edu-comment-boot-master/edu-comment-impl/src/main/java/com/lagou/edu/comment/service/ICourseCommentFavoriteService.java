package com.lagou.edu.comment.service;

import com.lagou.edu.comment.entity.CourseCommentFavorite;

import java.util.List;

public interface ICourseCommentFavoriteService {
    List<CourseCommentFavorite> getCommentFavoriteRecordList(Integer userId, List<String> parentIds);
}
