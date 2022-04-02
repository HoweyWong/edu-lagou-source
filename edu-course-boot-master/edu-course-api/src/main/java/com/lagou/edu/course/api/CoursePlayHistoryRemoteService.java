package com.lagou.edu.course.api;

import com.lagou.edu.course.api.dto.CoursePlayHistoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author mkp
 */
@FeignClient(name = "${remote.feign.edu-course-boot.name:edu-course-boot}", path = "/course/coursePlayHistory")
public interface CoursePlayHistoryRemoteService {

    /**
     * 保存播放历史
     * @param playHistoryDTO
     * @return
     */
    @PostMapping(value = "/saveCourse",consumes = "application/json")
    void saveCourseHistoryNode(@RequestBody CoursePlayHistoryDTO playHistoryDTO);


    /**
     * 获取播放的课程
     * @param userId
     * @param courseId
     * @return
     */
    @GetMapping(value = "/hasStudyLessons",consumes = "application/json")
    List hasStudyLessons(@RequestParam("userId") Integer userId,
                         @RequestParam("courseId") Integer courseId);

    /**
     * 获取课程播放节点
     * @param lessonId
     * @return
     */
    @GetMapping(value = "/getByLessonId",consumes = "application/json")
    CoursePlayHistoryDTO getByLessonId(@RequestParam("lessonId") Integer lessonId,@RequestParam("userId") Integer userId);
}
