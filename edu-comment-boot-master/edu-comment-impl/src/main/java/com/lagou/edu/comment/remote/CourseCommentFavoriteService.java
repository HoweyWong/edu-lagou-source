package com.lagou.edu.comment.remote;

import cn.hutool.core.bean.BeanUtil;
import com.lagou.edu.comment.api.CourseCommentFavoriteRemoteService;
import com.lagou.edu.comment.api.dto.CourseCommentFavoriteDTO;
import com.lagou.edu.comment.entity.CourseCommentFavorite;
import com.lagou.edu.comment.repository.CourseCommentFavoriteRepository;
import com.lagou.edu.comment.service.ICourseCommentService;
import com.lagou.edu.common.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CourseCommentFavoriteService {
    @Autowired
    private CourseCommentFavoriteRepository commentFavoriteRepository;
    @Autowired
    ICourseCommentService icourseCommentService;

    public List<CourseCommentFavoriteDTO> getUserById(Integer userId) {
        return null;
    }

    public CourseCommentFavoriteDTO favorite(Integer userId, String commentId) {
        CourseCommentFavorite comment = new CourseCommentFavorite();
        comment.setUserId(userId);
        comment.setCommentId(commentId);
        comment.setIsDel(false);

        List<CourseCommentFavorite> commentFavorites = this.commentFavoriteRepository.findAll(Example.of(comment, ExampleMatcher.matching()));
        if (CollectionUtils.isNotEmpty(commentFavorites)) {
            CourseCommentFavoriteDTO dto = new CourseCommentFavoriteDTO();
            BeanUtil.copyProperties(commentFavorites.get(0), dto);
            return dto;
        }
        CourseCommentFavorite favorite = new CourseCommentFavorite();
        favorite.setIsDel(false);
        favorite.setCommentId(commentId);
        Date nowDate = DateUtil.getNowDate();
        favorite.setCreateTime(nowDate);
        favorite.setUpdateTime(nowDate);
        favorite.setUserId(userId);
        CourseCommentFavorite insert = this.commentFavoriteRepository.insert(favorite);
        CourseCommentFavoriteDTO dto = new CourseCommentFavoriteDTO();
        BeanUtil.copyProperties(insert, dto);
        icourseCommentService.updateNumOfLike(commentId,Boolean.TRUE);
        log.info("用户点赞成功:{}", dto);
        return dto;
    }

    public boolean cancelFavorite(Integer userId, String commentId) {
        CourseCommentFavorite comment = new CourseCommentFavorite();
        comment.setUserId(userId);
        comment.setCommentId(commentId);
        comment.setIsDel(false);

        List<CourseCommentFavorite> commentFavorites = this.commentFavoriteRepository.findAll(Example.of(comment, ExampleMatcher.matching()));
        if (CollectionUtils.isNotEmpty(commentFavorites)) {
            commentFavorites.stream().forEach(courseCommentFavorite -> {
                courseCommentFavorite.setIsDel(true);
                courseCommentFavorite.setUpdateTime(DateUtil.getNowDate());
                commentFavoriteRepository.save(courseCommentFavorite);
                log.info("用户取消点赞:{}", courseCommentFavorite);
            });
        }
        icourseCommentService.updateNumOfLike(commentId,Boolean.FALSE);
        return true;
    }
}
