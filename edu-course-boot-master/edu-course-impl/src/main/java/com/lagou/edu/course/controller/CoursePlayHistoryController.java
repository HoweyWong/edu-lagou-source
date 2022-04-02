package com.lagou.edu.course.controller;


import com.alibaba.fastjson.JSON;
import com.lagou.edu.course.api.dto.CoursePlayHistoryDTO;
import com.lagou.edu.course.remote.CoursePlayHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author leo
 * @since 2020-06-19
 */
@Slf4j
@RestController
@RequestMapping("/course/coursePlayHistory")
public class CoursePlayHistoryController {
    @Autowired
    private CoursePlayHistoryService coursePlayHistoryService;
    /**
     * 保存播放历史
     * @param playHistoryDTO
     * @return
     */
    @PostMapping(value = "/saveCourse",consumes = "application/json")
    void saveCourseHistoryNode(@RequestBody CoursePlayHistoryDTO playHistoryDTO){
        log.info("保存历史节点 playHistoryDTO:{}", JSON.toJSONString(playHistoryDTO));
        coursePlayHistoryService.saveCourseHistoryNode(playHistoryDTO);
    }

    /**
     * 获取播放的课程
     * @param userId
     * @param courseId
     * @return
     */
    @GetMapping(value = "/hasStudyLessons")
    List hasStudyLessons(@RequestParam("userId")Integer userId, @RequestParam("courseId")Integer courseId){
        return  coursePlayHistoryService.hasStudyLessons(userId,courseId);
    }
    /**
     * 获取课程播放节点
     * @param lessonId
     * @return
     */
    @GetMapping(value = "/getByLessonId",consumes = "application/json")
    CoursePlayHistoryDTO getByLessonId(@RequestParam("lessonId") Integer lessonId,@RequestParam("userId")Integer userId){
        return  coursePlayHistoryService.getByLessonId(lessonId,userId);
    }
}
