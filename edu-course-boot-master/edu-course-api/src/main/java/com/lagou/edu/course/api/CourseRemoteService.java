package com.lagou.edu.course.api;

import com.lagou.edu.course.api.dto.CourseDTO;
import com.lagou.edu.course.api.dto.PageResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import com.lagou.edu.course.api.param.CourseQueryParam;
/**
 * @author mkp
 */
@FeignClient(name = "${remote.feign.edu-course-boot.name:edu-course-boot}", path = "/course")
public interface CourseRemoteService {
    /**
     * 获取选课列表
     * @param userId
     * @return
     */
    @GetMapping("/getAllCourses")
    List<CourseDTO> getAllCourses(@RequestParam(required = false, name = "userId") Integer userId);

    /**
     * 获取已购课程信息
     * @param userId
     * @return
     */
    @GetMapping("/getPurchasedCourse")
    List<CourseDTO> getPurchasedCourse(@RequestParam("userId") Integer userId);

    /**
     * 获取课程详情
     * @param courseId
     * @return
     */
    @GetMapping("/getCourseById")
    CourseDTO getCourseById(@RequestParam("courseId") Integer courseId,@RequestParam("userId") Integer userId);


    /**
     * 更新课程
     * @param courseDTO
     * @return
     */
    @PostMapping(value = "/saveOrUpdateCourse",consumes = "application/json")
    boolean saveOrUpdateCourse(@RequestBody CourseDTO courseDTO);


   @PostMapping(value = "/getQueryCourses",consumes = "application/json")
    PageResultDTO<CourseDTO> getQueryCourses( @RequestBody CourseQueryParam courseQueryParam);
}
