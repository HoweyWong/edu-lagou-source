package com.lagou.edu.comment.service.impl;

import com.lagou.edu.comment.entity.CourseComment;
import com.lagou.edu.comment.repository.CourseCommentRepository;
import com.lagou.edu.comment.service.ICourseCommentService;
import com.lagou.edu.comment.util.EmojiCharacterConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author:   mkp
 * Date:     2020/7/6 16:46
 * Description: 评论
 */
@Slf4j
@Service
public class ICourseCommentServiceImpl implements ICourseCommentService {

    @Autowired
    private CourseCommentRepository courseCommentRepository;
    @Override
    public  Page<CourseComment>  getCourseCommentList(Integer courseId, int pageNum, int pageSize) {

        if (courseId == null) {
            return null;
        }

        if (pageNum < 1) {
            pageNum = 0;
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.DESC,  "createTime", "likeCount");
        CourseComment courseComment = new CourseComment();
        courseComment.setCourseId(courseId);
        courseComment.setIsDel(Boolean.FALSE);
        //创建匹配器，组装查询条件
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("courseId", ExampleMatcher.GenericPropertyMatchers.exact());
        //创建实例
        Example<CourseComment> ex = Example.of(courseComment, matcher);
        return courseCommentRepository.findAll(ex, pageable);
    }

    @Override
    public boolean updateCommentDelStatusByIdAndUserId(String commentId, Integer userId) {
        CourseComment courseComment = new CourseComment();
        courseComment.setId(commentId);
        courseComment.setIsDel(Boolean.FALSE);
        courseComment.setUserId(userId);
        //创建匹配器，组装查询条件
        ExampleMatcher matcher = ExampleMatcher.matching();
        //创建实例
        Example<CourseComment> ex = Example.of(courseComment, matcher);
        List<CourseComment> courseComments = courseCommentRepository.findAll(ex);
        if(CollectionUtils.isEmpty(courseComments)){
            log.error("获取的评论为空 commentId:{} userId:{}",commentId,userId);
            return false;
        }
        CourseComment courseCommentNew = courseComments.get(0);
        courseCommentNew.setIsDel(Boolean.TRUE);
        courseCommentRepository.save(courseCommentNew);
        return Boolean.TRUE;
    }



    @Override
    public boolean saveCourseComment(CourseComment comment) {
        if (comment == null) {
            log.error("comment 参数为空");
            return false;
        }

        try {
            comment.setComment(EmojiCharacterConvertUtil.emojiConvertString(comment.getComment()));
            CourseComment insert = courseCommentRepository.insert(comment);
            if(insert != null){
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("保存课程信息异常,courseId = {},lessonId={}", comment.getCourseId(), e);
            return false;
        }

    }





    @Override
    public void updateNumOfLike(String commentId, Boolean flag) {
        CourseComment courseComment = new CourseComment();
        courseComment.setId(commentId);
        List<CourseComment> courseComments = courseCommentRepository.findAll(Example.of(courseComment, ExampleMatcher.matching()));
        if(CollectionUtils.isEmpty(courseComments)){
            log.error("评论id没有对应的数据 commentId:{}",commentId);
            return;
        }
        CourseComment comment = courseComments.get(0);
        Integer likeCount = comment.getLikeCount();
        if(flag){
            likeCount++;
        }else{
            likeCount--;
        }
        log.info("更新点赞 likeCount：{} flag:{} 以前点赞数量：{}",likeCount ,flag ,comment.getLikeCount());
        comment.setLikeCount(likeCount);
        courseCommentRepository.save(comment);
    }
}
