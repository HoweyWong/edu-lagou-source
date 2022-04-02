package com.lagou.edu.comment.controller;


import com.lagou.edu.comment.api.dto.CourseCommentDTO;
import com.lagou.edu.comment.api.param.CourseCommentParam;
import com.lagou.edu.comment.remote.CourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment/")
public class CommentController {

    @Autowired
    private CourseCommentService courseCommentService;

    /**
     * 获取课程或课时下的用户评论,
     *
     * @return
     */
    @PostMapping("/getCourseCommentList")
    List<CourseCommentDTO> getCourseCommentList(@RequestBody CourseCommentParam courseCommentParam){

        return courseCommentService.getCourseCommentList(courseCommentParam);
    }




    @PostMapping(value = "/saveCourseComment")
    boolean saveCourseComment(@RequestBody CourseCommentDTO commentDTO){
        return this.courseCommentService.saveCourseComment(commentDTO);
    }

    /**
     * 逻辑删除课程评论
     *
     * @param commentId
     * @param userId
     * @return
     */
    @GetMapping("/deleteCourseComment")
    boolean deleteCourseComment(@RequestParam("commentId") String commentId,
                                @RequestParam("userId") Integer userId){
        return courseCommentService.deleteCourseComment(commentId,userId);
    }

}
