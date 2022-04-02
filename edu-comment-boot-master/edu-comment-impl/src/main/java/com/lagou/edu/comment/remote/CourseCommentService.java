package com.lagou.edu.comment.remote;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.comment.api.dto.CourseCommentDTO;
import com.lagou.edu.comment.api.param.CourseCommentParam;
import com.lagou.edu.comment.entity.CourseComment;
import com.lagou.edu.comment.entity.CourseCommentFavorite;
import com.lagou.edu.comment.service.ICourseCommentFavoriteService;
import com.lagou.edu.comment.service.ICourseCommentService;
import com.lagou.edu.comment.util.EmojiCharacterConvertUtil;
import com.lagou.edu.user.api.UserRemoteService;
import com.lagou.edu.user.api.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CourseCommentService {

    @Autowired
    private ICourseCommentService iCourseCommentService;
    @Autowired
    private ICourseCommentFavoriteService iCourseCommentFavoriteService;
    @Autowired
    private UserRemoteService userRemoteService;

    /**
     * 获取课程或课时下的用户评论,
     *
     * @return
     */
    public List<CourseCommentDTO> getCourseCommentList(CourseCommentParam courseCommentParam) {
        log.info("获取评论 参数courseCommentParam：{}", JSON.toJSONString(courseCommentParam));
        Integer userId = courseCommentParam.getUserId();
        Integer courseId = courseCommentParam.getCourseId();
        int pageNum = courseCommentParam.getPageNum();
        int pageSize = courseCommentParam.getPageSize();
        Page<CourseComment> courseCommentList = iCourseCommentService.getCourseCommentList(courseId, pageNum, pageSize);
        List<CourseComment> courseComments = courseCommentList.getContent();
        if (courseComments == null || courseComments.isEmpty()) {
            return Collections.emptyList();
        }

        //获取一级留言的ID集合
        List<String> parentIds = new LinkedList<>();
        for(CourseComment comment:courseComments){
            parentIds.add(comment.getId());
        }


        //批量获取用户点赞的帖子
        Map<String, Boolean> favoriteMapping = getFavoriteCourseCommentMap(userId,parentIds);


        List<CourseCommentDTO> userCommentList = new LinkedList<CourseCommentDTO>();

        //遍历获取用户留言
        for (CourseComment comment : courseComments) {
            CourseCommentDTO commentDTO = new CourseCommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);

            commentDTO.setComment(convertCommentCharacter(comment.getComment()));
            if(favoriteMapping != null && !favoriteMapping.isEmpty()){
                Boolean favoriteTag = favoriteMapping.get(comment.getId());
                commentDTO.setFavoriteTag(favoriteTag == null ? Boolean.FALSE:Boolean.TRUE);
            }
            if(Objects.equals(userId,comment.getUserId())){
                commentDTO.setOwner(Boolean.TRUE);
            }
            UserDTO userDTO = userRemoteService.getUserById(comment.getUserId());
            if(userDTO != null){
                commentDTO.setNickName(userDTO.getName());
            }
            userCommentList.add(commentDTO);
        }
        log.info("返回的评论 userCommentList：{}",JSON.toJSONString(userCommentList));
        return userCommentList;
    }


    /**
     *
     * @param parentIds
     * @return
     */
    private Map<String, Boolean> getFavoriteCourseCommentMap(Integer userId, List<String> parentIds) {
        if (CollectionUtils.isEmpty(parentIds)) {
            return Collections.emptyMap();
        }

        List<CourseCommentFavorite> favoriteRecords = iCourseCommentFavoriteService.getCommentFavoriteRecordList(userId, parentIds);
        if (CollectionUtils.isEmpty(favoriteRecords)) {
            return Collections.emptyMap();
        }

        Map<String, Boolean> favoriteMapping = new HashMap<>(favoriteRecords.size());
        for (CourseCommentFavorite record : favoriteRecords) {
            favoriteMapping.put(record.getCommentId(), Boolean.TRUE);
        }
        return favoriteMapping;
    }


    private String convertCommentCharacter(String comment) {
        try {
            return EmojiCharacterConvertUtil.emojiRecovery(comment);
        } catch (Exception e) {
            log.error("转换评论字符失败,comment={}", comment, e);
            return comment;
        }
    }

    public boolean saveCourseComment(CourseCommentDTO comment) {
        if (!checkParams(comment)) {
            log.info("comment 含有为空的字段");
            return false;
        }

        //组装待保存的courseComment
        CourseComment courseComment = new CourseComment();
        courseComment.setCourseId(comment.getCourseId());
        courseComment.setUserId(comment.getUserId());
        courseComment.setComment(comment.getComment());
        courseComment.setLikeCount(0);
        Date nowDate = new Date();
        courseComment.setCreateTime(nowDate);
        courseComment.setUpdateTime(nowDate);
        courseComment.setIsDel(Boolean.FALSE);
        log.info("保存的留言对象={}", JSON.toJSONString(courseComment));
        return iCourseCommentService.saveCourseComment(courseComment);
    }
    /**
     * 检查留言不为空
     * @param comment
     * @return
     */
    private boolean checkParams(CourseCommentDTO comment) {
        if (comment == null) {
            log.info("comment 为 null");
            return false;
        }

        Integer userId = comment.getUserId();
        if (userId == null) {
            log.info("留言中的 userId 为 null");
            return false;
        }

        Integer courseId = comment.getCourseId();
        if (courseId == null) {
            log.info("留言中的 courseId 为 null");
            return false;
        }


        //
        String commentContent = comment.getComment();
        if (StringUtils.isBlank(commentContent)) {
            log.info("留言中的内容 为 null");
            return false;
        }

        return true;
    }
    public boolean deleteCourseComment(String commentId, Integer userId) {
        if (commentId == null || userId == null) {
            log.info("参数为空,commentId={},userId={}", commentId, userId);
            return false;
        }

        boolean deleteFlag = iCourseCommentService.updateCommentDelStatusByIdAndUserId(commentId, userId);
        if (!deleteFlag) {
            return false;
        }
        return true;
    }

}
