package com.lagou.edu.comment.api;

import com.lagou.edu.comment.api.dto.CourseCommentDTO;
import com.lagou.edu.comment.api.param.CourseCommentParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "${remote.feign.edu-comment-boot.name:edu-comment-boot}", path = "/comment")
public interface CourseCommentRemoteService {



    /**
     * 获取课程或课时下的用户评论,
     * @return
     */
    @PostMapping(value = "/getCourseCommentList",consumes = "application/json")
    List<CourseCommentDTO> getCourseCommentList(@RequestBody CourseCommentParam courseCommentParam);

    /**
     * 获取我的留言
     *
     * @param userId
     * @param courseId 课程ID
     * @param lessonId 课时ID
     * @return
     */
    @GetMapping(value = "/getMyCommentList")
    List<CourseCommentDTO> getMyCommentList(@RequestParam("userId") Integer userId,
                                            @RequestParam("courseId") Integer courseId,
                                            @RequestParam("lessonId")Integer lessonId);
    /**
     * 保存课程评论
     *
     * @param comment
     * @return
     */
    @PostMapping(value = "/saveCourseComment",consumes = "application/json")
    boolean saveCourseComment(@RequestBody CourseCommentDTO comment);
    /**
     * 逻辑删除课程评论
     *
     * @param commentId
     * @param userId
     * @return
     */
    @GetMapping("/deleteCourseComment")
    boolean deleteCourseComment(@RequestParam("commentId") String commentId, @RequestParam("userId") Integer userId);



    @GetMapping("/getUserById")
    List<CourseCommentDTO> getUserById(@RequestParam("userId") Integer userId);


}
