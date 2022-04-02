package com.lagou.edu.course.api;

import com.lagou.edu.course.api.dto.SectionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/**
 * @author mkp
 */
@FeignClient(name = "${remote.feign.edu-course-boot.name:edu-course-boot}", path = "/course/section")
public interface SectionRemoteService {
    /**
     * 保存课程
     * @param sectionDTO
     * @return
     */
    @PostMapping(value = "/saveOrUpdateSection",consumes = "application/json")
    boolean saveOrUpdateSection(@RequestBody SectionDTO sectionDTO);



    /**
     * 通过课程Id获取章节和课时
     * @param courseId
     * @return
     */
    @GetMapping(value = "/getSectionAndLesson")
    List<SectionDTO> getSectionAndLesson(@RequestParam("courseId") Integer courseId);


    /**
     * 获取章节信息
     * @param sectionId
     * @return
     */
    @GetMapping(value = "/getBySectionId",consumes = "application/json")
    SectionDTO getBySectionId(@RequestParam("sectionId") Integer sectionId);
}
