package com.lagou.edu.course.api;

import com.lagou.edu.course.api.dto.TeacherDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Author:   mkp
 * Date:     2020/7/6 19:11
 * Description:
 */
@FeignClient(name = "${remote.feign.edu-course-boot.name:edu-course-boot}", path = "/teacher")
public interface TeacherRemoteService {
    /**
     * 通过课程Id获取老师信息
     * @param courseId
     * @return
     */
    @GetMapping(value = "/getTeacherByCourseId",consumes = "application/json")
    TeacherDTO getTeacherByCourseId(Integer courseId);
}
