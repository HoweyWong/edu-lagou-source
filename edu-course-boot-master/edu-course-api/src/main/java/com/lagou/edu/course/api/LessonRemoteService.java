package com.lagou.edu.course.api;

import com.lagou.edu.course.api.dto.LessonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author mkp
 */
@FeignClient(name = "${remote.feign.edu-course-boot.name:edu-course-boot}", path = "/course/lesson")
public interface LessonRemoteService {
    /**
     * 保存或者更新课程
     *
     * @param lessonDTO
     * @return
     */
    @PostMapping(value = "/saveOrUpdate", consumes = "application/json")
    boolean saveOrUpdate(@RequestBody LessonDTO lessonDTO);

    /**
     * 通过lessonId获取课时
     *
     * @param lessonId
     * @return
     */
    @GetMapping(value = "/getById")
    LessonDTO getById(@RequestParam("lessonId") Integer lessonId);

    /**
     * 通过lessonId获取对应课时名称，map
     * @param lessonIds
     * @return
     */
    @GetMapping(value = "/getByIds")
    Map<Integer,String> getByIds(@RequestParam("lessonIds") List<Integer> lessonIds);
}
